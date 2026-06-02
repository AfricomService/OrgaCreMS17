package com.orgacare.app.service;

import com.orgacare.app.domain.Affectation;
import com.orgacare.app.domain.Departement;
import com.orgacare.app.domain.Organigramme;
import com.orgacare.app.domain.Personne;
import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.repository.AffectationRepository;
import com.orgacare.app.repository.DepartementRepository;
import com.orgacare.app.repository.OrganigrammeRepository;
import com.orgacare.app.repository.PersonneRepository;
import com.orgacare.app.service.dto.AffectationDTO;
import com.orgacare.app.service.dto.DepartementDTO;
import com.orgacare.app.service.dto.DepartementTreeDTO;
import com.orgacare.app.service.dto.PersonneDTO;
import com.orgacare.app.service.mapper.DepartementMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Departement}.
 */
@Service
@Transactional
public class DepartementService {

    private final Logger log = LoggerFactory.getLogger(DepartementService.class);
    private static final String DEFAULT_CODE_PREFIX = "Dept-";
    private static final String CODE_FORMAT = "%s%05d";

    private final DepartementRepository departementRepository;
    private final AffectationRepository affectationRepository;
    private final DepartementMapper departementMapper;
    private final OrganigrammeRepository organigrammeRepository;
    private final AffectationService affectationService;
    private final PersonneRepository personneRepository;

    public DepartementService(
        DepartementRepository departementRepository,
        AffectationRepository affectationRepository,
        DepartementMapper departementMapper,
        OrganigrammeRepository organigrammeRepository,
        @Lazy AffectationService affectationService,
        PersonneRepository personneRepository
    ) {
        this.departementRepository = departementRepository;
        this.affectationRepository = affectationRepository;
        this.departementMapper = departementMapper;
        this.organigrammeRepository = organigrammeRepository;
        this.affectationService = affectationService;
        this.personneRepository = personneRepository;
    }

    // ── CRUD de base ────────────────────────────────────────────────────────────

    public DepartementDTO save(DepartementDTO departementDTO) {
        log.debug("Request to save Departement : {}", departementDTO);
        Departement departement = departementMapper.toEntity(departementDTO);

        if (departement.getCode() == null || departement.getCode().isEmpty()) {
            departement.setCode(generateNextCode(DEFAULT_CODE_PREFIX));
        } else if (departementRepository.existsByCode(departement.getCode())) {
            throw new IllegalArgumentException("Le code fourni existe déjà : " + departement.getCode());
        }

        departement = departementRepository.save(departement);
        return departementMapper.toDto(departement);
    }

    public String generateNextCode(String prefix) {
        int nextNumber = departementRepository.findMaxCodeNumber(prefix) + 1;
        return String.format(CODE_FORMAT, prefix, nextNumber);
    }

    @Transactional(readOnly = true)
    public Page<DepartementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Departements");
        return departementRepository.findAll(pageable).map(departementMapper::toDto);
    }

    public Page<DepartementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return departementRepository.findAllWithEagerRelationships(pageable).map(departementMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<DepartementDTO> findOne(Long id) {
        log.debug("Request to get Departement : {}", id);
        return departementRepository.findOneWithEagerRelationships(id).map(departementMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete Departement : {}", id);
        departementRepository.deleteById(id);
    }

    // ── Méthodes métier ─────────────────────────────────────────────────────────

    public List<DepartementDTO> findByOrganigrammeId(Long organigrammeId) {
        return departementRepository
            .findByOrganigrammeId(organigrammeId)
            .stream()
            .map(departement ->
                new DepartementDTO(
                    departement.getId(),
                    departement.getCode(),
                    departement.getNom(),
                    departement.getStatus(),
                    departement.getEmail(),
                    departement.getSite() != null ? departement.getSite().getId() : null,
                    departement.getOrganigramme() != null ? departement.getOrganigramme().getId() : null,
                    departement.getDepartementParent() != null ? departement.getDepartementParent().getId() : null,
                    new HashSet<>()
                )
            )
            .collect(Collectors.toList());
    }

    public Departement deplacerDepartement(Long departementId, Long nouveauParentId) {
        Departement departement = departementRepository
            .findById(departementId)
            .orElseThrow(() -> new IllegalArgumentException("Département non trouvé : " + departementId));
        Departement nouveauParent = departementRepository
            .findById(nouveauParentId)
            .orElseThrow(() -> new IllegalArgumentException("Parent non trouvé : " + nouveauParentId));
        departement.setDepartementParent(nouveauParent);
        return departementRepository.save(departement);
    }

    public void renameDepartement(Long id, String newName) {
        Departement departement = departementRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Département non trouvé avec l'ID: " + id));
        departement.setNom(newName);
        departementRepository.save(departement);
    }

    /**
     * Récupère les départements avec leurs affectations (chefs, membres, assistants).
     * La personne est retrouvée via PersonneRepository.findByAffectationId car
     * Affectation n'a pas de relation directe vers Personne (c'est Personne -> Affectation).
     */
    public List<DepartementDTO> getDepartementsWithAffectations(Long societeId, Long organigrammeId) {
        try {
            List<Departement> departements = departementRepository.findBySocieteIdAndOrganigrammeId(societeId, organigrammeId);

            if (departements == null || departements.isEmpty()) {
                return Collections.emptyList();
            }

            return departements
                .stream()
                .map(departement -> {
                    List<Affectation> affectations = affectationRepository.findByDepartementId(departement.getId());

                    Set<PersonneDTO> chefs = new HashSet<>();
                    Set<PersonneDTO> membres = new HashSet<>();
                    Set<PersonneDTO> assistants = new HashSet<>();

                    if (affectations != null) {
                        affectations.forEach(a -> {
                            if (a.getEtat() == Etat.CANCELED) return;

                            // Personne retrouvée via la relation inverse
                            Personne p = personneRepository.findByAffectationId(a.getId()).orElse(null);
                            if (p == null) return;

                            PersonneDTO personneDTO = new PersonneDTO(p.getId(), p.getNomPrenom(), a.getType().name());

                            switch (a.getType()) {
                                case CHEF:
                                    chefs.add(personneDTO);
                                    break;
                                case MEMBRE:
                                    membres.add(personneDTO);
                                    break;
                                case ASSISTANT:
                                    assistants.add(personneDTO);
                                    break;
                                default:
                                    break;
                            }
                        });
                    }

                    DepartementDTO dto = new DepartementDTO(
                        departement.getId(),
                        departement.getCode(),
                        departement.getNom(),
                        departement.getStatus(),
                        departement.getEmail(),
                        departement.getSite() != null ? departement.getSite().getId() : null,
                        departement.getOrganigramme() != null ? departement.getOrganigramme().getId() : null,
                        departement.getDepartementParent() != null ? departement.getDepartementParent().getId() : null,
                        new HashSet<>()
                    );

                    dto.setChefs(chefs.stream().map(PersonneDTO::getNomPrenom).collect(Collectors.joining(", ")));
                    dto.setMembres(membres.stream().map(PersonneDTO::getNomPrenom).collect(Collectors.joining(", ")));
                    dto.setAssistants(assistants.stream().map(PersonneDTO::getNomPrenom).collect(Collectors.joining(", ")));

                    // societeId via organigramme (Departement n'a pas de lien direct vers Societe)
                    if (departement.getOrganigramme() != null && departement.getOrganigramme().getSociete() != null) {
                        dto.setSocieteId(departement.getOrganigramme().getSociete().getId());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
        } catch (RuntimeException e) {
            log.error("Erreur lors de la récupération des départements et affectations", e);
            throw e;
        }
    }

    // ── Arbre des départements ───────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<DepartementTreeDTO> getDepartementTreeByOrganigramme(String organigrammeCode) {
        Organigramme organigramme = organigrammeRepository.findByCode(organigrammeCode);
        if (organigramme == null) return Collections.emptyList();

        List<Departement> allDeps = departementRepository.findByOrganigramme_Id(organigramme.getId());
        if (allDeps == null || allDeps.isEmpty()) return Collections.emptyList();

        // 1) map id -> DTO
        Map<Long, DepartementTreeDTO> dtoMap = new HashMap<>();
        for (Departement dep : allDeps) {
            dtoMap.put(dep.getId(), new DepartementTreeDTO(dep.getId(), dep.getCode(), dep.getNom()));
        }

        // 2) construire l'arbre
        List<DepartementTreeDTO> roots = new ArrayList<>();
        for (Departement dep : allDeps) {
            DepartementTreeDTO dto = dtoMap.get(dep.getId());
            if (dep.getDepartementParent() != null) {
                DepartementTreeDTO parentDto = dtoMap.get(dep.getDepartementParent().getId());
                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                } else {
                    roots.add(dto);
                }
            } else {
                roots.add(dto);
            }
        }

        // 3) toutes les affectations en une requête
        List<Long> depIds = allDeps.stream().map(Departement::getId).collect(Collectors.toList());
        List<Affectation> affects = affectationRepository.findByDepartementIdIn(depIds);
        if (affects == null || affects.isEmpty()) return roots;

        // 4) grouper : departementId -> typeName -> liste PersonneDTO
        Map<Long, Map<String, List<PersonneDTO>>> grouped = new HashMap<>();

        for (Affectation a : affects) {
            if (a == null) continue;
            if (a.getEtat() == null || a.getEtat() == Etat.CANCELED) continue;
            if (a.getDepartement() == null) continue;

            // Personne retrouvée via la relation inverse (Personne -> Affectation)
            Personne personne = personneRepository.findByAffectationId(a.getId()).orElse(null);
            if (personne == null) continue;

            Long dId = a.getDepartement().getId();
            if (dId == null) continue;

            String typeName = a.getType() != null ? a.getType().name() : "UNKNOWN";

            PersonneDTO pDto = new PersonneDTO(personne.getId(), personne.getNomPrenom());
            pDto.setMatricule(personne.getMatricule());
            pDto.setEmail(personne.getEmail());
            pDto.setNumTelephone(personne.getNumTelephone());
            pDto.setCin(personne.getCin());
            pDto.setEtat(personne.getEtat());
            pDto.setEtatContractuelle(personne.getEtatContractuelle());
            pDto.setDateCreation(personne.getDateCreation());
            pDto.setDateDebutContrat(personne.getDateDebutContrat());
            pDto.setIdContratActif(personne.getIdContratActif());
            pDto.setIdTypeContratActif(personne.getIdTypeContratActif());
            if (a.getType() != null) pDto.setRole(a.getType().name());

            log.debug(
                "Mapping personne -> DTO id={}, nom={}, matricule={}, type={}",
                personne.getId(),
                personne.getNomPrenom(),
                personne.getMatricule(),
                a.getType()
            );

            grouped.computeIfAbsent(dId, k -> new HashMap<>()).computeIfAbsent(typeName, k -> new ArrayList<>()).add(pDto);
        }

        // 5) attacher les affectations aux nœuds
        for (Map.Entry<Long, Map<String, List<PersonneDTO>>> entry : grouped.entrySet()) {
            DepartementTreeDTO node = dtoMap.get(entry.getKey());
            if (node != null) node.setAffectationsByType(entry.getValue());
        }

        return roots;
    }

    @Transactional(readOnly = true)
    public List<DepartementTreeDTO> getDepartementTreeOnlyByOrganigramme(String organigrammeCode) {
        Organigramme organigramme = organigrammeRepository.findByCode(organigrammeCode);
        if (organigramme == null) return Collections.emptyList();

        List<Departement> allDeps = departementRepository.findByOrganigramme_Id(organigramme.getId());
        if (allDeps == null || allDeps.isEmpty()) return Collections.emptyList();

        Map<Long, DepartementTreeDTO> dtoMap = new HashMap<>();
        for (Departement dep : allDeps) {
            DepartementTreeDTO dto = new DepartementTreeDTO();
            dto.setId(dep.getId());
            dto.setCode(dep.getCode());
            dto.setNom(dep.getNom());
            dto.setEmail(dep.getEmail());
            dto.setChildren(new ArrayList<>());
            dtoMap.put(dep.getId(), dto);
        }

        List<DepartementTreeDTO> roots = new ArrayList<>();
        for (Departement dep : allDeps) {
            DepartementTreeDTO dto = dtoMap.get(dep.getId());
            if (dep.getDepartementParent() != null) {
                DepartementTreeDTO parentDto = dtoMap.get(dep.getDepartementParent().getId());
                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                } else {
                    roots.add(dto);
                }
            } else {
                roots.add(dto);
            }
        }

        Comparator<DepartementTreeDTO> byName = Comparator.comparing(d -> d.getNom() == null ? "" : d.getNom());
        voidSortTree(roots, byName);

        return roots;
    }

    private void voidSortTree(List<DepartementTreeDTO> nodes, Comparator<DepartementTreeDTO> cmp) {
        if (nodes == null || nodes.isEmpty()) return;
        nodes.sort(cmp);
        for (DepartementTreeDTO n : nodes) voidSortTree(n.getChildren(), cmp);
    }

    // ── Codes / hiérarchie ───────────────────────────────────────────────────────

    public List<String> getDepartementAndChildrenCodes(String code) {
        Departement root = departementRepository
            .findByCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Departement not found with code: " + code));
        List<String> result = new ArrayList<>();
        collectDepartementCodes(root, result);
        return result;
    }

    private void collectDepartementCodes(Departement departement, List<String> result) {
        result.add(departement.getCode());
        List<Departement> children = departementRepository.findByDepartementParent(departement);
        for (Departement child : children) collectDepartementCodes(child, result);
    }

    public List<String> getDepartementAndParentsCodes(String code) {
        Departement departement = departementRepository
            .findByCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Departement not found with code: " + code));
        List<String> result = new ArrayList<>();
        collectParentCodes(departement, result);
        return result;
    }

    private void collectParentCodes(Departement departement, List<String> result) {
        result.add(departement.getCode());
        Departement parent = departement.getDepartementParent();
        if (parent != null) collectParentCodes(parent, result);
    }

    @Transactional(readOnly = true)
    public DepartementDTO getDepartementByCodeOrThrow(String code) {
        return departementRepository
            .findByCode(code)
            .map(departementMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Departement not found with code: " + code));
    }

    public Map<String, List<String>> getDepartementAndParentsCodesByNom(String codeDept) {
        Departement departement = departementRepository
            .findByCode(codeDept)
            .orElseThrow(() -> new IllegalArgumentException("Departement not found with nom: " + codeDept));
        List<String> parentCodes = new ArrayList<>();
        collectParentCodes(departement, parentCodes);
        Map<String, List<String>> result = new HashMap<>();
        result.put("code", Collections.singletonList(departement.getCode()));
        result.put("parents", parentCodes);
        return result;
    }

    @Transactional(readOnly = true)
    public Optional<DepartementDTO> findOnByCode(String code) {
        log.debug("Request to get Departement : {}", code);
        return departementRepository.findByCode(code).map(departementMapper::toDto);
    }

    public String findOrgaByDepartementCode(String deptCode) {
        DepartementDTO departementDTO = findOnByCode(deptCode)
            .orElseThrow(() -> new IllegalArgumentException("Departement not found with code: " + deptCode));
        Organigramme organigramme = organigrammeRepository
            .findById(departementDTO.getOrganigrammeId())
            .orElseThrow(() -> new IllegalArgumentException("Organigramme not found"));
        return organigramme.getCode();
    }

    @Transactional(readOnly = true)
    public List<DepartementDTO> findByPersonneId(Long personneId) {
        return departementRepository
            .findDistinctByPersonneId(personneId)
            .stream()
            .map(departementMapper::toDto)
            .collect(Collectors.toList());
    }

    // ── Affectations par userId ──────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Map<String, Object>> findDepartementNamesAndAffectationTypesByUserId(Long userId) {
        log.debug("Récupération des départements (code + nom) + types d'affectation pour userId {}", userId);

        List<AffectationDTO> affectations = affectationService.findAffectationPersonneByUserId(userId).orElse(Collections.emptyList());

        if (affectations.isEmpty()) return Collections.emptyList();

        List<Long> departementIds = affectations
            .stream()
            .map(AffectationDTO::getDepartementId)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());

        if (departementIds.isEmpty()) return Collections.emptyList();

        Map<Long, Departement> departementMap = departementRepository
            .findAllById(departementIds)
            .stream()
            .collect(Collectors.toMap(Departement::getId, d -> d));

        Map<String, Map<String, Object>> resultMap = new LinkedHashMap<>();

        for (AffectationDTO a : affectations) {
            Long depId = a.getDepartementId();
            if (depId == null) continue;
            if (a.getEtat() != null && a.getEtat() == Etat.CANCELED) continue;

            Departement dep = departementMap.get(depId);
            if (dep == null) continue;

            String code = dep.getCode();
            String nom = dep.getNom();
            String key = (code != null && !code.trim().isEmpty()) ? code.trim() : ("NAME::" + (nom != null ? nom.trim() : depId));

            Map<String, Object> entry = resultMap.computeIfAbsent(
                key,
                k -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("departementCode", code);
                    m.put("departementName", nom);
                    m.put("types", new LinkedHashSet<String>());
                    return m;
                }
            );

            if (a.getType() != null) {
                @SuppressWarnings("unchecked")
                Set<String> types = (Set<String>) entry.get("types");
                types.add(a.getType().name());
            }
        }

        return new ArrayList<>(resultMap.values());
    }

    @Transactional(readOnly = true)
    public Map<String, String> findDepartementNamesByCodes(List<String> codes) {
        if (codes == null || codes.isEmpty()) return Collections.emptyMap();
        return departementRepository
            .findByCodeIn(codes)
            .stream()
            .filter(d -> d.getCode() != null)
            .collect(Collectors.toMap(Departement::getCode, Departement::getNom, (a, b) -> a));
    }

    // ── Recherche par matricule ───────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public String findFirstDepartementNomByMatricule(String matricule) {
        if (matricule == null || matricule.trim().isEmpty()) return null;

        Optional<Personne> personneOpt = personneRepository.findByMatriculeIgnoreCase(matricule.trim());
        if (!personneOpt.isPresent()) {
            log.debug("Aucune personne trouvée pour le matricule '{}'", matricule);
            return null;
        }

        // Les affectations de la personne sont trouvées via AffectationRepository.findByPersonneId
        // qui fait la jointure Affectation -> Personne côté BDD
        List<Affectation> affectations = affectationRepository.findByPersonneId(personneOpt.get().getId());
        if (affectations == null || affectations.isEmpty()) return null;

        return affectations
            .stream()
            .filter(a -> a.getEtat() != null && a.getEtat() != Etat.CANCELED)
            .filter(a -> a.getDepartement() != null)
            .map(a -> a.getDepartement().getNom())
            .filter(nom -> nom != null && !nom.trim().isEmpty())
            .findFirst()
            .orElse(null);
    }

    @Transactional(readOnly = true)
    public Optional<DepartementDTO> findFirstDepartementByMatricule(String matricule) {
        if (matricule == null || matricule.trim().isEmpty()) return Optional.empty();

        Optional<Personne> personneOpt = personneRepository.findByMatriculeIgnoreCase(matricule.trim());
        if (!personneOpt.isPresent()) {
            log.debug("Aucune personne trouvée pour le matricule '{}'", matricule);
            return Optional.empty();
        }

        List<Affectation> affectations = affectationRepository.findByPersonneId(personneOpt.get().getId());
        if (affectations == null || affectations.isEmpty()) return Optional.empty();

        return affectations
            .stream()
            .filter(a -> a.getEtat() != null && a.getEtat() != Etat.CANCELED)
            .filter(a -> a.getDepartement() != null)
            .map(a -> departementMapper.toDto(a.getDepartement()))
            .findFirst();
    }
}
