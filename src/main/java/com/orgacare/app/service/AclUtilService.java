package com.orgacare.app.service;

import com.orgacare.app.domain.AclEntry;
import com.orgacare.app.domain.AclSid;
import com.orgacare.app.domain.enumeration.AclPermission;
import com.orgacare.app.repository.AclEntryRepository;
import com.orgacare.app.repository.AclSidRepository;
import com.orgacare.app.security.SecurityUtils;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AclUtilService {

    public static final String SID_USER = "USER";
    public static final String SID_ROLE = "ROLE";

    private final AclEntryRepository aclEntryRepository;
    private final AclSidRepository aclSidRepository;

    public AclUtilService(AclEntryRepository aclEntryRepository, AclSidRepository aclSidRepository) {
        this.aclEntryRepository = aclEntryRepository;
        this.aclSidRepository = aclSidRepository;
    }

    // ---------------------------------------------------------------------
    // SID management
    // ---------------------------------------------------------------------

    /** Get an existing SID or create it. */
    public AclSid getOrCreateSid(String sidType, String sidValue) {
        return aclSidRepository
            .findBySidTypeAndSidValue(sidType, sidValue)
            .orElseGet(() -> {
                AclSid sid = new AclSid();
                sid.setSidType(sidType);
                sid.setSidValue(sidValue);
                sid.setSidKey(sidType + ":" + sidValue);
                return aclSidRepository.save(sid);
            });
    }

    /** All SIDs that represent the current user: his USER sid + one sid per role. */
    @Transactional(readOnly = true)
    public List<AclSid> getCurrentUserSids() {
        List<AclSid> sids = new ArrayList<>();

        SecurityUtils.getCurrentUserLogin().map(login -> getOrCreateSid(SID_USER, login)).ifPresent(sids::add);

        //        SecurityUtils.getCurrentUserAuthorities()
        //            .forEach(auth -> sids.add(getOrCreateSid(SID_ROLE, auth)));

        return sids;
    }

    // ---------------------------------------------------------------------
    // GRANT
    // ---------------------------------------------------------------------

    /** Grant (or update) a permission for a sid on an object. */
    public AclEntry grantPermission(String objectType, Long objectId, String sidType, String sidValue, AclPermission permission) {
        AclSid sid = getOrCreateSid(sidType, sidValue);

        AclEntry entry = aclEntryRepository
            .findByObjectTypeAndObjectIdAndSidId(objectType, objectId, sid.getId())
            .orElseGet(() -> {
                AclEntry e = new AclEntry();
                e.setObjectType(objectType);
                e.setObjectId(objectId);
                e.setSidId(sid.getId());
                e.setCanRead(false);
                e.setCanWrite(false);
                return e;
            });

        applyPermission(entry, permission, true);
        return aclEntryRepository.save(entry);
    }

    public AclEntry grantToUser(String objectType, Long objectId, String login, AclPermission permission) {
        return grantPermission(objectType, objectId, SID_USER, login, permission);
    }

    public AclEntry grantToRole(String objectType, Long objectId, String role, AclPermission permission) {
        return grantPermission(objectType, objectId, SID_ROLE, role, permission);
    }

    /** Grant full access (read + write) — typically used for the creator/owner. */
    public AclEntry grantOwner(String objectType, Long objectId, String login) {
        AclEntry entry = grantToUser(objectType, objectId, login, AclPermission.READ);
        applyPermission(entry, AclPermission.WRITE, true);
        return aclEntryRepository.save(entry);
    }

    // ---------------------------------------------------------------------
    // REVOKE
    // ---------------------------------------------------------------------

    /** Revoke one permission. If no permission remains, the entry is deleted. */
    public void revokePermission(String objectType, Long objectId, String sidType, String sidValue, AclPermission permission) {
        aclSidRepository
            .findBySidTypeAndSidValue(sidType, sidValue)
            .ifPresent(sid ->
                aclEntryRepository
                    .findByObjectTypeAndObjectIdAndSidId(objectType, objectId, sid.getId())
                    .ifPresent(entry -> {
                        applyPermission(entry, permission, false);
                        if (!Boolean.TRUE.equals(entry.getCanRead()) && !Boolean.TRUE.equals(entry.getCanWrite())) {
                            aclEntryRepository.delete(entry);
                        } else {
                            aclEntryRepository.save(entry);
                        }
                    })
            );
    }

    /** Remove every permission entry a sid has on an object. */
    public void revokeAllForSid(String objectType, Long objectId, String sidType, String sidValue) {
        aclSidRepository
            .findBySidTypeAndSidValue(sidType, sidValue)
            .ifPresent(sid ->
                aclEntryRepository
                    .findByObjectTypeAndObjectIdAndSidId(objectType, objectId, sid.getId())
                    .ifPresent(aclEntryRepository::delete)
            );
    }

    /** Delete the whole ACL of an object — call this when the object itself is deleted. */
    public void deleteAcl(String objectType, Long objectId) {
        aclEntryRepository.deleteByObjectTypeAndObjectId(objectType, objectId);
    }

    // ---------------------------------------------------------------------
    // CHECK
    // ---------------------------------------------------------------------

    /** Does the CURRENT user have this permission on this object? (USER sid + ROLE sids) */
    @Transactional(readOnly = true)
    public boolean hasPermission(String objectType, Long objectId, AclPermission permission) {
        List<Long> sidIds = getCurrentUserSids().stream().map(AclSid::getId).collect(Collectors.toList());
        if (sidIds.isEmpty()) return false;

        return aclEntryRepository
            .findByObjectTypeAndObjectId(objectType, objectId)
            .stream()
            .filter(e -> sidIds.contains(e.getSidId()))
            .anyMatch(e -> checkPermission(e, permission));
    }

    /** Does a SPECIFIC login (plus its roles) have this permission? Useful for admin screens. */
    @Transactional(readOnly = true)
    public boolean hasPermission(String objectType, Long objectId, AclPermission permission, String login, Collection<String> roles) {
        List<Long> sidIds = new ArrayList<>();
        aclSidRepository.findBySidTypeAndSidValue(SID_USER, login).map(AclSid::getId).ifPresent(sidIds::add);
        for (String role : roles) {
            aclSidRepository.findBySidTypeAndSidValue(SID_ROLE, role).map(AclSid::getId).ifPresent(sidIds::add);
        }
        return aclEntryRepository
            .findByObjectTypeAndObjectId(objectType, objectId)
            .stream()
            .filter(e -> sidIds.contains(e.getSidId()))
            .anyMatch(e -> checkPermission(e, permission));
    }

    public boolean canRead(String objectType, Long objectId) {
        return hasPermission(objectType, objectId, AclPermission.READ);
    }

    public boolean canWrite(String objectType, Long objectId) {
        return hasPermission(objectType, objectId, AclPermission.WRITE);
    }

    // ---------------------------------------------------------------------
    // READ / LIST
    // ---------------------------------------------------------------------

    /** Full ACL of one object (for a "permissions" tab in the UI). */
    @Transactional(readOnly = true)
    public List<AclEntry> getAcl(String objectType, Long objectId) {
        return aclEntryRepository.findByObjectTypeAndObjectId(objectType, objectId);
    }

    /**
     * IDs of all objects of a type the current user can access with the given permission.
     * Use this to filter list endpoints (join in the service or pass to a repository IN query).
     */
    @Transactional(readOnly = true)
    public Set<Long> getAccessibleObjectIds(String objectType, AclPermission permission) {
        List<Long> sidIds = getCurrentUserSids().stream().map(AclSid::getId).collect(Collectors.toList());
        if (sidIds.isEmpty()) return Collections.emptySet();

        return aclEntryRepository
            .findByObjectTypeAndSidIdIn(objectType, sidIds)
            .stream()
            .filter(e -> checkPermission(e, permission))
            .map(AclEntry::getObjectId)
            .collect(Collectors.toSet());
    }

    /** Copy the ACL when duplicating an object. */
    public void copyAcl(String objectType, Long sourceId, Long targetId) {
        aclEntryRepository
            .findByObjectTypeAndObjectId(objectType, sourceId)
            .forEach(e -> {
                AclEntry copy = new AclEntry();
                copy.setObjectType(objectType);
                copy.setObjectId(targetId);
                copy.setSidId(e.getSidId());
                copy.setCanRead(e.getCanRead());
                copy.setCanWrite(e.getCanWrite());
                aclEntryRepository.save(copy);
            });
    }

    /** Downgrade every entry of an object to READ only (removes all WRITE rights). */
    public void makeReadOnlyForAll(String objectType, Long objectId) {
        aclEntryRepository
            .findByObjectTypeAndObjectId(objectType, objectId)
            .forEach(e -> {
                e.setCanRead(true);
                e.setCanWrite(false);
                aclEntryRepository.save(e);
            });
    }

    // ---------------------------------------------------------------------
    // internals
    // ---------------------------------------------------------------------

    private void applyPermission(AclEntry entry, AclPermission permission, boolean value) {
        switch (permission) {
            case READ -> entry.setCanRead(value);
            case WRITE -> entry.setCanWrite(value);
        }
    }

    private boolean checkPermission(AclEntry entry, AclPermission permission) {
        // WRITE implies READ, like in classic ACLs
        return switch (permission) {
            case READ -> Boolean.TRUE.equals(entry.getCanRead()) || Boolean.TRUE.equals(entry.getCanWrite());
            case WRITE -> Boolean.TRUE.equals(entry.getCanWrite());
        };
    }
}
