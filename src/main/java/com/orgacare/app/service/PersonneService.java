package com.orgacare.app.service;

import com.orgacare.app.client.UserCCRestClient;
import com.orgacare.app.domain.Contrat;
import com.orgacare.app.domain.Personne;
import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.domain.enumeration.EtatContractuelle;
import com.orgacare.app.repository.ContratRepository;
import com.orgacare.app.repository.PersonneRepository;
import com.orgacare.app.service.dto.ContratDTO;
import com.orgacare.app.service.dto.PersonneDTO;
import com.orgacare.app.service.dto.TypeContratDTO;
import com.orgacare.app.service.dto.UserDTO;
import com.orgacare.app.service.mapper.PersonneMapper;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Personne}.
 */
@Service
@Transactional
public class PersonneService {

    private final Logger log = LoggerFactory.getLogger(PersonneService.class);
    private static final String DEFAULT_MATRICULE_PREFIX = "Emp-";
    private static final String MATRICULE_FORMAT = "%s%05d";
    private final PersonneRepository personneRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final PersonneMapper personneMapper;
    private final TypeContratService typeContratService;
    private final ContratService contratService;
    private final ContratRepository contratRepository;

    @Autowired
    //    private UserRestClient userRestClient;
    private UserCCRestClient userRestClient;

    public PersonneService(
        PersonneRepository personneRepository,
        PersonneMapper personneMapper,
        TypeContratService typeContratService,
        ContratService contratService,
        ContratRepository contratRepository,
        UserCCRestClient userRestClient
    ) {
        this.personneRepository = personneRepository;
        this.personneMapper = personneMapper;
        this.typeContratService = typeContratService;
        this.contratService = contratService;
        this.contratRepository = contratRepository;
        this.userRestClient = userRestClient;
    }

    /**
     * Save a personne.
     *
     * @param personneDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonneDTO save(PersonneDTO personneDTO) {
        log.debug("Request to save Personne : {}", personneDTO);

        // Valeurs par défaut si non renseignées (champs cachés dans le formulaire)
        if (personneDTO.getEtat() == null) {
            personneDTO.setEtat(Etat.ACTIF);
        }
        if (personneDTO.getEtatContractuelle() == null) {
            personneDTO.setEtatContractuelle(EtatContractuelle.ACTIF);
        }

        Personne personne = personneMapper.toEntity(personneDTO);

        Optional<ContratDTO> contratActifOpt = contratService.findContratActifByPersonneId(personne.getId());
        if (contratActifOpt.isPresent()) {
            ContratDTO contratActif = contratActifOpt.get();
            log.debug("Contrat actif trouvé pour la personne avec id {} : {}", personne.getId(), contratActif);
            personne.setIdContratActif(contratActif.getId());
            personne.setIdTypeContratActif(contratActif.getTypeContratId());
        } else {
            log.debug("Aucun contrat actif trouvé pour la personne avec id {}", personne.getId());
            personne.setIdContratActif(null);
            personne.setIdTypeContratActif(null);
        }

        log.debug(
            "Avant sauvegarde, idContratActif: {}, idTypeContratActif: {}",
            personne.getIdContratActif(),
            personne.getIdTypeContratActif()
        );

        if (personne.getMatricule() == null || personne.getMatricule().isEmpty()) {
            String prefix = DEFAULT_MATRICULE_PREFIX;
            personne.setMatricule(generateNextMatricule(prefix));
        } else {
            Optional<Personne> existingPersonne = personneRepository.findByMatricule(personne.getMatricule());
            if (existingPersonne.isPresent() && !existingPersonne.get().getId().equals(personne.getId())) {
                throw new IllegalArgumentException("Le matricule fourni existe déjà : " + personne.getMatricule());
            }
        }

        personne = personneRepository.save(personne);
        return personneMapper.toDto(personne);
    }

    //    public Personne createPersonne(Personne personne) {
    //        Personne savedPersonne = personneRepository.save(personne);
    //
    //
    //        UserDTO userDTO = new UserDTO(personne);
    //
    //
    //        String hashedPassword = passwordEncoder.encode("defaultPassword123");
    //        userDTO.setPassword(hashedPassword);
    //
    //
    //        userRestClient.createUser(userDTO);
    //
    //        return savedPersonne;
    //    }
    public String generateNextMatricule(String prefix) {
        int nextNumber = personneRepository.findMaxMatriculeNumber(prefix) + 1;
        return String.format(MATRICULE_FORMAT, prefix, nextNumber);
    }

    /**
     * Get all the personnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */

    @Transactional(readOnly = true)
    public Page<PersonneDTO> findAll(
        Pageable pageable,
        String matricule,
        String nomPrenom,
        String numTelephone,
        String cin,
        String typeContratId
    ) {
        Specification<Personne> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by matricule
            if (matricule != null && !matricule.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("matricule")), "%" + matricule.toLowerCase() + "%"));
            }

            if (nomPrenom != null && !nomPrenom.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nomPrenom")), "%" + nomPrenom.toLowerCase() + "%"));
            }

            if (numTelephone != null && !numTelephone.isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("numTelephone")), "%" + numTelephone.toLowerCase() + "%")
                );
            }

            if (cin != null && !cin.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("cin")), "%" + cin.toLowerCase() + "%"));
            }

            //
            //            if (etatContractuelle != null && !etatContractuelle.isEmpty()) {
            //                predicates.add(criteriaBuilder.like(
            //                    criteriaBuilder.lower(root.get("etatContractuelle")),
            //                    "%" + etatContractuelle.toLowerCase() + "%"
            //                ));
            //            }

            if (StringUtils.hasText(typeContratId)) {
                log.debug("Filtrage sur le type de contrat : {}", typeContratId);

                List<Long> typeContratIds = typeContratService
                    .findByNomContainingIgnoreCase(typeContratId)
                    .stream()
                    .map(TypeContratDTO::getId)
                    .collect(Collectors.toList());

                log.debug("IDs des contrats trouvés : {}", typeContratIds);

                if (!typeContratIds.isEmpty()) {
                    predicates.add(root.get("idTypeContratActif").in(typeContratIds));
                } else {
                    log.debug("Aucun type de contrat trouvé pour {}", typeContratId);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return personneRepository.findAll(spec, pageable).map(personneMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PersonneDTO> searchByKeyword(Pageable pageable, String keyword) {
        Specification<Personne> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";

                // Recherche dans plusieurs champs
                Predicate matriculePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("matricule")), likePattern);
                Predicate nomPrenomPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("nomPrenom")), likePattern);
                Predicate numTelephonePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("numTelephone")), likePattern);
                Predicate cinPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("cin")), likePattern);

                // Combine les prédicats avec un OR
                predicates.add(criteriaBuilder.or(matriculePredicate, nomPrenomPredicate, numTelephonePredicate, cinPredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return personneRepository.findAll(spec, pageable).map(personneMapper::toDto);
    }

    public void updatePersonStatus(Long personneId) {
        List<Contrat> contratsActifs = contratRepository.findByPersonneIdAndStatus(personneId, "ACTIF");

        Personne personne = personneRepository.findById(personneId).orElse(null);
        if (personne != null) {
            if (contratsActifs.isEmpty()) {
                personne.setEtatContractuelle(EtatContractuelle.PASSIF);
                personne.setIdContratActif(null);
                personne.setIdTypeContratActif(null);
                personne.setEtat(Etat.CANCELED);
            } else {
                Contrat contratActif = contratsActifs.stream().findFirst().orElse(null);
                personne.setEtatContractuelle(EtatContractuelle.ACTIF);
                personne.setIdContratActif(contratActif.getId());
                personne.setIdTypeContratActif(contratActif.getTypeContrat().getId());
                personne.setEtat(Etat.ACTIF);
            }
            personneRepository.save(personne);
            log.info("Mise à jour de l'état de la personne {}.", personneId);
        }
    }

    //    @Transactional(readOnly = true)
    //    public Page<PersonneDTO> getByTypeContrat(String typeContrat) {
    //
    //        Pageable pageable = PageRequest.of(0, 100, Sort.by("id").descending());
    //
    //        Specification<Personne> spec = (root, query, criteriaBuilder) -> {
    //            List<Predicate> predicates = new ArrayList<>();
    //
    //
    //            List<TypeContratDTO> typeContratDTOS = typeContratService.findAll();
    //
    //            List<TypeContratDTO> typeContratFiltred = typeContratDTOS.stream()
    //                .filter(usr -> usr.getNom().trim().toUpperCase().contains(typeContrat.trim().toUpperCase()))
    //                .collect(Collectors.toList());
    //
    //            List<Long> typeContratIds = new LinkedList<>();
    //
    //            typeContratFiltred.forEach(f -> typeContratIds.add(f.getId()));
    //
    //            List<ContratDTO> contratDTOS = contratService.getContratByTypeContrat(typeContratIds);
    //
    //            List<Long> personneIds = new LinkedList<>();
    //
    //            contratDTOS.forEach(contrat -> personneIds.add(contrat.getPersonneId()));
    //
    //            predicates.add(root.get("id").in(personneIds));
    //
    //            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    //        };
    //
    //        return personneRepository.findAll(spec, pageable)
    //            .map(personneMapper::toDto);
    //    }

    //    @Transactional(readOnly = true)
    //    public Page<PersonneDTO> findAll(Pageable pageable, String matricule, String nomPrenom) {
    //        Specification<Personne> spec = (root, query, criteriaBuilder) -> {
    //            List<Predicate> predicates = new ArrayList<>();
    //
    //            // Filter by matricule
    //            if (matricule != null && !matricule.isEmpty()) {
    //                predicates.add(criteriaBuilder.like(
    //                    criteriaBuilder.lower(root.get("matricule")),
    //                    "%" + matricule.toLowerCase() + "%"
    //                ));
    //            }
    //
    //            // Filter by nomPrenom
    //            if (nomPrenom != null && !nomPrenom.isEmpty()) {
    //                predicates.add(criteriaBuilder.like(
    //                    criteriaBuilder.lower(root.get("nomPrenom")),
    //                    "%" + nomPrenom.toLowerCase() + "%"
    //                ));
    //            }
    //
    //
    //
    //
    //            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    //        };
    //
    //        return personneRepository.findAll(spec, pageable)
    //            .map(personneMapper::toDto);
    //    }

    //    /**
    //     * Get all the personnes.
    //     *
    //     * @param pageable the pagination information.
    //     * @return the list of entities.
    //     */
    //    @Transactional(readOnly = true)
    //    public Page<PersonneDTO> findAll(Pageable pageable) {
    //        log.debug("Request to get all Personnes");
    //        return personneRepository.findAll(pageable)
    //            .map(personneMapper::toDto);
    //    }

    /**
     * Get one personne by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonneDTO> findOne(Long id) {
        log.debug("Request to get Personne : {}", id);
        return personneRepository.findById(id).map(personneMapper::toDto);
    }

    /**
     * Delete the personne by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Personne : {}", id);
        personneRepository.deleteById(id);
    }

    public List<PersonneDTO> importFromExcel(MultipartFile file) throws IOException {
        List<PersonneDTO> personnesToSave = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            log.debug("Nombre total de lignes dans la feuille: {}", sheet.getPhysicalNumberOfRows());

            for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row == null) {
                    log.debug("Ligne {} est vide, passage à la ligne suivante.", rowIndex);
                    continue;
                }

                PersonneDTO personneDTO = new PersonneDTO();
                personneDTO.setMatricule(getCellValueAsString(row.getCell(0)));
                personneDTO.setNomPrenom(getCellValueAsString(row.getCell(1)));
                personneDTO.setEmail(getCellValueAsString(row.getCell(2)));
                personneDTO.setGenre(getCellValueAsString(row.getCell(3)));
                personneDTO.setCin(getCellValueAsString(row.getCell(4)));
                personneDTO.setNumTelephone(getCellValueAsString(row.getCell(5)));

                // Initialisation des champs obligatoires avec valeurs par défaut si null
                String etatValue = getCellValueAsString(row.getCell(6));
                personneDTO.setEtat(etatValue != null ? Etat.valueOf(etatValue.toUpperCase()) : Etat.ACTIF);

                String etatContractuelleValue = getCellValueAsString(row.getCell(7));
                personneDTO.setEtatContractuelle(
                    etatContractuelleValue != null
                        ? EtatContractuelle.valueOf(etatContractuelleValue.toUpperCase())
                        : EtatContractuelle.ACTIF
                );

                log.debug("PersonneDTO créée pour la ligne {}: {}", rowIndex, personneDTO);
                personnesToSave.add(personneDTO);
            }

            try {
                personnesToSave.forEach(this::save);
            } catch (Exception e) {
                log.error("Erreur lors de la sauvegarde des données : ", e);
                throw e;
            }
        }

        log.debug("Importation terminée avec succès.");
        return personnesToSave;
    }

    /**
     * Helper method to get cell value as a String.
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    public List<PersonneDTO> findAllListPersonne() {
        return personneRepository.findAll().stream().map(personneMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PersonneDTO> findByEmail(String email) {
        log.debug("Request to get Personne by email : {}", email);
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        return personneRepository.findByEmailIgnoreCase(email.trim()).map(personneMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<PersonneDTO> findByMatricule(String matricule) {
        log.debug("Request to get Personne by matricule : {}", matricule);
        if (matricule == null || matricule.trim().isEmpty()) {
            return Optional.empty();
        }
        return personneRepository.findByMatricule(matricule.trim()).map(personneMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<PersonneDTO> findByUserId(String userId) {
        log.debug("Recherche Personne par userId via Feign: {}", userId);
        try {
            // Récupère l'utilisateur distant via Feign
            UserDTO user = userRestClient.getUserById(userId);
            if (user == null || user.getLogin() == null || user.getLogin().trim().isEmpty()) {
                log.debug("User introuvable ou login vide pour id {}", userId);
                return Optional.empty();
            }

            String login = user.getLogin().trim();
            // cherche la personne dont le matricule == login
            return personneRepository.findByMatriculeIgnoreCase(login).map(personneMapper::toDto);
        } catch (feign.FeignException fe) {
            log.warn("Erreur Feign lors de la récupération de l'user {} : {}", userId, fe.getMessage());
            return Optional.empty();
        } catch (Exception ex) {
            log.error("Erreur inattendue lors de findByUserId {} : {}", userId, ex.getMessage());
            return Optional.empty();
        }
    }

    //    @Transactional(readOnly = true)
    //    public Map<String,String> findPersonneNamesByMatricules(List<String> matricules) {
    //        if (matricules == null || matricules.isEmpty()) return Collections.emptyMap();
    //        List<Personne> personnes = personneRepository.findByMatriculeIn(matricules);
    //        return personnes.stream()
    //            .filter(p -> p.getMatricule() != null)
    //            .collect(Collectors.toMap(Personne::getMatricule, Personne::getNomPrenom, (a,b)->a));
    //    }

    @Transactional
    public Optional<PersonneDTO> updateNomPrenomAndEmailByMatricule(String matricule, String nomPrenom, String email) {
        if (matricule == null || matricule.trim().isEmpty()) {
            return Optional.empty();
        }
        Optional<Personne> personneOpt = personneRepository.findByMatriculeIgnoreCase(matricule.trim());
        if (!personneOpt.isPresent()) {
            return Optional.empty();
        }

        Personne personne = personneOpt.get();

        // Vérifier unicité de l'email si fourni
        if (email != null && !email.trim().isEmpty()) {
            Optional<Personne> otherByEmail = personneRepository.findByEmailIgnoreCase(email.trim());
            if (otherByEmail.isPresent() && !otherByEmail.get().getId().equals(personne.getId())) {
                throw new IllegalArgumentException("Email déjà utilisé par une autre personne");
            }
            personne.setEmail(email.trim());
        } else {
            // si l'appel veut supprimer l'email, on peut accepter email=="" ou null -> pas de suppression automatique,
            // adapte selon besoin : ici on laisse tel quel si email==null, mais si tu veux supprimer quand "" tu peux:
            // personne.setEmail(null);
        }

        if (nomPrenom != null && !nomPrenom.trim().isEmpty()) {
            personne.setNomPrenom(nomPrenom.trim());
        }

        Personne saved = personneRepository.save(personne);
        return Optional.of(personneMapper.toDto(saved));
    }

    @Transactional(readOnly = true)
    public Map<String, String> findPersonneNamesByMatricules(List<String> matricules) {
        if (matricules == null || matricules.isEmpty()) return Collections.emptyMap();

        // Normaliser en lowercase pour la recherche insensible à la casse
        List<String> normalizedInput = matricules
            .stream()
            .filter(Objects::nonNull)
            .map(m -> m.trim().toLowerCase())
            .distinct()
            .collect(Collectors.toList());

        // Utiliser la requête insensible à la casse
        List<Personne> personnes = personneRepository.findByMatriculeInIgnoreCase(normalizedInput);

        log.debug(
            "findPersonneNamesByMatricules: input={}, found={}",
            normalizedInput,
            personnes.stream().map(Personne::getMatricule).collect(Collectors.toList())
        );

        // Retourner la map avec clés en lowercase pour que correspmanage retrouve la valeur
        return personnes
            .stream()
            .filter(p -> p.getMatricule() != null && p.getNomPrenom() != null)
            .collect(Collectors.toMap(p -> p.getMatricule().trim().toLowerCase(), Personne::getNomPrenom, (a, b) -> a));
    }

    @Transactional
    public PersonneDTO assignUser(Long personneId, String userId) {
        log.debug("Assigning userId {} to personne {}", userId, personneId);
        Personne personne = personneRepository
            .findById(personneId)
            .orElseThrow(() -> new IllegalArgumentException("Personne introuvable: " + personneId));
        personne.setUserId(userId);
        return personneMapper.toDto(personneRepository.save(personne));
    }

    @Transactional
    public PersonneDTO unassignUser(Long personneId) {
        log.debug("Removing userId from personne {}", personneId);
        Personne personne = personneRepository
            .findById(personneId)
            .orElseThrow(() -> new IllegalArgumentException("Personne introuvable: " + personneId));
        personne.setUserId(null);
        return personneMapper.toDto(personneRepository.save(personne));
    }
}
