//package com.orgacare.app.web.rest;
//
//import static com.orgacare.app.web.rest.TestUtil.sameInstant;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.orgacare.app.IntegrationTest;
//import com.orgacare.app.domain.Personne;
//import com.orgacare.app.domain.enumeration.Etat;
//import com.orgacare.app.domain.enumeration.EtatContractuelle;
//import com.orgacare.app.repository.PersonneRepository;
//import com.orgacare.app.service.dto.PersonneDTO;
//import com.orgacare.app.service.mapper.PersonneMapper;
//import java.time.Instant;
//import java.time.ZoneId;
//import java.time.ZoneOffset;
//import java.time.ZonedDateTime;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.atomic.AtomicLong;
//import javax.persistence.EntityManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Integration tests for the {@link PersonneResource} REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class PersonneResourceIT {
//
//    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
//    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";
//
//    private static final String DEFAULT_NOM_PRENOM = "AAAAAAAAAA";
//    private static final String UPDATED_NOM_PRENOM = "BBBBBBBBBB";
//
//    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
//    private static final String UPDATED_EMAIL = "BBBBBBBBBB";
//
//    private static final String DEFAULT_NUM_TELEPHONE = "AAAAAAAAAA";
//    private static final String UPDATED_NUM_TELEPHONE = "BBBBBBBBBB";
//
//    private static final String DEFAULT_GENRE = "AAAAAAAAAA";
//    private static final String UPDATED_GENRE = "BBBBBBBBBB";
//
//    private static final String DEFAULT_CIN = "AAAAAAAAAA";
//    private static final String UPDATED_CIN = "BBBBBBBBBB";
//
//    private static final Etat DEFAULT_ETAT = Etat.DRAFT;
//    private static final Etat UPDATED_ETAT = Etat.ACTIF;
//
//    private static final EtatContractuelle DEFAULT_ETAT_CONTRACTUELLE = EtatContractuelle.ACTIF;
//    private static final EtatContractuelle UPDATED_ETAT_CONTRACTUELLE = EtatContractuelle.PASSIF;
//
//    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final ZonedDateTime DEFAULT_DATE_DEBUT_CONTRAT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_DATE_DEBUT_CONTRAT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final Long DEFAULT_ID_CONTRAT_ACTIF = 1L;
//    private static final Long UPDATED_ID_CONTRAT_ACTIF = 2L;
//
//    private static final Long DEFAULT_ID_TYPE_CONTRAT_ACTIF = 1L;
//    private static final Long UPDATED_ID_TYPE_CONTRAT_ACTIF = 2L;
//
//    private static final Long DEFAULT_USER_ID = 1L;
//    private static final Long UPDATED_USER_ID = 2L;
//
//    private static final String ENTITY_API_URL = "/api/personnes";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private PersonneRepository personneRepository;
//
//    @Autowired
//    private PersonneMapper personneMapper;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restPersonneMockMvc;
//
//    private Personne personne;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Personne createEntity(EntityManager em) {
//        Personne personne = new Personne()
//            .matricule(DEFAULT_MATRICULE)
//            .nomPrenom(DEFAULT_NOM_PRENOM)
//            .email(DEFAULT_EMAIL)
//            .numTelephone(DEFAULT_NUM_TELEPHONE)
//            .genre(DEFAULT_GENRE)
//            .cin(DEFAULT_CIN)
//            .etat(DEFAULT_ETAT)
//            .etatContractuelle(DEFAULT_ETAT_CONTRACTUELLE)
//            .dateCreation(DEFAULT_DATE_CREATION)
//            .dateDebutContrat(DEFAULT_DATE_DEBUT_CONTRAT)
//            .idContratActif(DEFAULT_ID_CONTRAT_ACTIF)
//            .idTypeContratActif(DEFAULT_ID_TYPE_CONTRAT_ACTIF)
//            .userId(DEFAULT_USER_ID);
//        return personne;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Personne createUpdatedEntity(EntityManager em) {
//        Personne personne = new Personne()
//            .matricule(UPDATED_MATRICULE)
//            .nomPrenom(UPDATED_NOM_PRENOM)
//            .email(UPDATED_EMAIL)
//            .numTelephone(UPDATED_NUM_TELEPHONE)
//            .genre(UPDATED_GENRE)
//            .cin(UPDATED_CIN)
//            .etat(UPDATED_ETAT)
//            .etatContractuelle(UPDATED_ETAT_CONTRACTUELLE)
//            .dateCreation(UPDATED_DATE_CREATION)
//            .dateDebutContrat(UPDATED_DATE_DEBUT_CONTRAT)
//            .idContratActif(UPDATED_ID_CONTRAT_ACTIF)
//            .idTypeContratActif(UPDATED_ID_TYPE_CONTRAT_ACTIF)
//            .userId(UPDATED_USER_ID);
//        return personne;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        personne = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    void createPersonne() throws Exception {
//        int databaseSizeBeforeCreate = personneRepository.findAll().size();
//        // Create the Personne
//        PersonneDTO personneDTO = personneMapper.toDto(personne);
//        restPersonneMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
//            )
//            .andExpect(status().isCreated());
//
//        // Validate the Personne in the database
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeCreate + 1);
//        Personne testPersonne = personneList.get(personneList.size() - 1);
//        assertThat(testPersonne.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
//        assertThat(testPersonne.getNomPrenom()).isEqualTo(DEFAULT_NOM_PRENOM);
//        assertThat(testPersonne.getEmail()).isEqualTo(DEFAULT_EMAIL);
//        assertThat(testPersonne.getNumTelephone()).isEqualTo(DEFAULT_NUM_TELEPHONE);
//        assertThat(testPersonne.getGenre()).isEqualTo(DEFAULT_GENRE);
//        assertThat(testPersonne.getCin()).isEqualTo(DEFAULT_CIN);
//        assertThat(testPersonne.getEtat()).isEqualTo(DEFAULT_ETAT);
//        assertThat(testPersonne.getEtatContractuelle()).isEqualTo(DEFAULT_ETAT_CONTRACTUELLE);
//        assertThat(testPersonne.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
//        assertThat(testPersonne.getDateDebutContrat()).isEqualTo(DEFAULT_DATE_DEBUT_CONTRAT);
//        assertThat(testPersonne.getIdContratActif()).isEqualTo(DEFAULT_ID_CONTRAT_ACTIF);
//        assertThat(testPersonne.getIdTypeContratActif()).isEqualTo(DEFAULT_ID_TYPE_CONTRAT_ACTIF);
//        assertThat(testPersonne.getUserId()).isEqualTo(DEFAULT_USER_ID);
//    }
//
//    @Test
//    @Transactional
//    void createPersonneWithExistingId() throws Exception {
//        // Create the Personne with an existing ID
//        personne.setId(1L);
//        PersonneDTO personneDTO = personneMapper.toDto(personne);
//
//        int databaseSizeBeforeCreate = personneRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restPersonneMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Personne in the database
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void checkEtatIsRequired() throws Exception {
//        int databaseSizeBeforeTest = personneRepository.findAll().size();
//        // set the field null
//        personne.setEtat(null);
//
//        // Create the Personne, which fails.
//        PersonneDTO personneDTO = personneMapper.toDto(personne);
//
//        restPersonneMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkEtatContractuelleIsRequired() throws Exception {
//        int databaseSizeBeforeTest = personneRepository.findAll().size();
//        // set the field null
//        personne.setEtatContractuelle(null);
//
//        // Create the Personne, which fails.
//        PersonneDTO personneDTO = personneMapper.toDto(personne);
//
//        restPersonneMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void getAllPersonnes() throws Exception {
//        // Initialize the database
//        personneRepository.saveAndFlush(personne);
//
//        // Get all the personneList
//        restPersonneMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(personne.getId().intValue())))
//            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
//            .andExpect(jsonPath("$.[*].nomPrenom").value(hasItem(DEFAULT_NOM_PRENOM)))
//            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
//            .andExpect(jsonPath("$.[*].numTelephone").value(hasItem(DEFAULT_NUM_TELEPHONE)))
//            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)))
//            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
//            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
//            .andExpect(jsonPath("$.[*].etatContractuelle").value(hasItem(DEFAULT_ETAT_CONTRACTUELLE.toString())))
//            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))))
//            .andExpect(jsonPath("$.[*].dateDebutContrat").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT_CONTRAT))))
//            .andExpect(jsonPath("$.[*].idContratActif").value(hasItem(DEFAULT_ID_CONTRAT_ACTIF.intValue())))
//            .andExpect(jsonPath("$.[*].idTypeContratActif").value(hasItem(DEFAULT_ID_TYPE_CONTRAT_ACTIF.intValue())))
//            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
//    }
//
//    @Test
//    @Transactional
//    void getPersonne() throws Exception {
//        // Initialize the database
//        personneRepository.saveAndFlush(personne);
//
//        // Get the personne
//        restPersonneMockMvc
//            .perform(get(ENTITY_API_URL_ID, personne.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(personne.getId().intValue()))
//            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
//            .andExpect(jsonPath("$.nomPrenom").value(DEFAULT_NOM_PRENOM))
//            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
//            .andExpect(jsonPath("$.numTelephone").value(DEFAULT_NUM_TELEPHONE))
//            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE))
//            .andExpect(jsonPath("$.cin").value(DEFAULT_CIN))
//            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
//            .andExpect(jsonPath("$.etatContractuelle").value(DEFAULT_ETAT_CONTRACTUELLE.toString()))
//            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)))
//            .andExpect(jsonPath("$.dateDebutContrat").value(sameInstant(DEFAULT_DATE_DEBUT_CONTRAT)))
//            .andExpect(jsonPath("$.idContratActif").value(DEFAULT_ID_CONTRAT_ACTIF.intValue()))
//            .andExpect(jsonPath("$.idTypeContratActif").value(DEFAULT_ID_TYPE_CONTRAT_ACTIF.intValue()))
//            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingPersonne() throws Exception {
//        // Get the personne
//        restPersonneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putNewPersonne() throws Exception {
//        // Initialize the database
//        personneRepository.saveAndFlush(personne);
//
//        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
//
//        // Update the personne
//        Personne updatedPersonne = personneRepository.findById(personne.getId()).get();
//        // Disconnect from session so that the updates on updatedPersonne are not directly saved in db
//        em.detach(updatedPersonne);
//        updatedPersonne
//            .matricule(UPDATED_MATRICULE)
//            .nomPrenom(UPDATED_NOM_PRENOM)
//            .email(UPDATED_EMAIL)
//            .numTelephone(UPDATED_NUM_TELEPHONE)
//            .genre(UPDATED_GENRE)
//            .cin(UPDATED_CIN)
//            .etat(UPDATED_ETAT)
//            .etatContractuelle(UPDATED_ETAT_CONTRACTUELLE)
//            .dateCreation(UPDATED_DATE_CREATION)
//            .dateDebutContrat(UPDATED_DATE_DEBUT_CONTRAT)
//            .idContratActif(UPDATED_ID_CONTRAT_ACTIF)
//            .idTypeContratActif(UPDATED_ID_TYPE_CONTRAT_ACTIF)
//            .userId(UPDATED_USER_ID);
//        PersonneDTO personneDTO = personneMapper.toDto(updatedPersonne);
//
//        restPersonneMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, personneDTO.getId())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Personne in the database
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
//        Personne testPersonne = personneList.get(personneList.size() - 1);
//        assertThat(testPersonne.getMatricule()).isEqualTo(UPDATED_MATRICULE);
//        assertThat(testPersonne.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
//        assertThat(testPersonne.getEmail()).isEqualTo(UPDATED_EMAIL);
//        assertThat(testPersonne.getNumTelephone()).isEqualTo(UPDATED_NUM_TELEPHONE);
//        assertThat(testPersonne.getGenre()).isEqualTo(UPDATED_GENRE);
//        assertThat(testPersonne.getCin()).isEqualTo(UPDATED_CIN);
//        assertThat(testPersonne.getEtat()).isEqualTo(UPDATED_ETAT);
//        assertThat(testPersonne.getEtatContractuelle()).isEqualTo(UPDATED_ETAT_CONTRACTUELLE);
//        assertThat(testPersonne.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
//        assertThat(testPersonne.getDateDebutContrat()).isEqualTo(UPDATED_DATE_DEBUT_CONTRAT);
//        assertThat(testPersonne.getIdContratActif()).isEqualTo(UPDATED_ID_CONTRAT_ACTIF);
//        assertThat(testPersonne.getIdTypeContratActif()).isEqualTo(UPDATED_ID_TYPE_CONTRAT_ACTIF);
//        assertThat(testPersonne.getUserId()).isEqualTo(UPDATED_USER_ID);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingPersonne() throws Exception {
//        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
//        personne.setId(count.incrementAndGet());
//
//        // Create the Personne
//        PersonneDTO personneDTO = personneMapper.toDto(personne);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restPersonneMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, personneDTO.getId())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Personne in the database
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchPersonne() throws Exception {
//        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
//        personne.setId(count.incrementAndGet());
//
//        // Create the Personne
//        PersonneDTO personneDTO = personneMapper.toDto(personne);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restPersonneMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Personne in the database
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamPersonne() throws Exception {
//        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
//        personne.setId(count.incrementAndGet());
//
//        // Create the Personne
//        PersonneDTO personneDTO = personneMapper.toDto(personne);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restPersonneMockMvc
//            .perform(
//                put(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Personne in the database
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdatePersonneWithPatch() throws Exception {
//        // Initialize the database
//        personneRepository.saveAndFlush(personne);
//
//        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
//
//        // Update the personne using partial update
//        Personne partialUpdatedPersonne = new Personne();
//        partialUpdatedPersonne.setId(personne.getId());
//
//        partialUpdatedPersonne
//            .matricule(UPDATED_MATRICULE)
//            .genre(UPDATED_GENRE)
//            .etatContractuelle(UPDATED_ETAT_CONTRACTUELLE)
//            .dateCreation(UPDATED_DATE_CREATION)
//            .idContratActif(UPDATED_ID_CONTRAT_ACTIF)
//            .idTypeContratActif(UPDATED_ID_TYPE_CONTRAT_ACTIF);
//
//        restPersonneMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedPersonne.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonne))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Personne in the database
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
//        Personne testPersonne = personneList.get(personneList.size() - 1);
//        assertThat(testPersonne.getMatricule()).isEqualTo(UPDATED_MATRICULE);
//        assertThat(testPersonne.getNomPrenom()).isEqualTo(DEFAULT_NOM_PRENOM);
//        assertThat(testPersonne.getEmail()).isEqualTo(DEFAULT_EMAIL);
//        assertThat(testPersonne.getNumTelephone()).isEqualTo(DEFAULT_NUM_TELEPHONE);
//        assertThat(testPersonne.getGenre()).isEqualTo(UPDATED_GENRE);
//        assertThat(testPersonne.getCin()).isEqualTo(DEFAULT_CIN);
//        assertThat(testPersonne.getEtat()).isEqualTo(DEFAULT_ETAT);
//        assertThat(testPersonne.getEtatContractuelle()).isEqualTo(UPDATED_ETAT_CONTRACTUELLE);
//        assertThat(testPersonne.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
//        assertThat(testPersonne.getDateDebutContrat()).isEqualTo(DEFAULT_DATE_DEBUT_CONTRAT);
//        assertThat(testPersonne.getIdContratActif()).isEqualTo(UPDATED_ID_CONTRAT_ACTIF);
//        assertThat(testPersonne.getIdTypeContratActif()).isEqualTo(UPDATED_ID_TYPE_CONTRAT_ACTIF);
//        assertThat(testPersonne.getUserId()).isEqualTo(DEFAULT_USER_ID);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdatePersonneWithPatch() throws Exception {
//        // Initialize the database
//        personneRepository.saveAndFlush(personne);
//
//        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
//
//        // Update the personne using partial update
//        Personne partialUpdatedPersonne = new Personne();
//        partialUpdatedPersonne.setId(personne.getId());
//
//        partialUpdatedPersonne
//            .matricule(UPDATED_MATRICULE)
//            .nomPrenom(UPDATED_NOM_PRENOM)
//            .email(UPDATED_EMAIL)
//            .numTelephone(UPDATED_NUM_TELEPHONE)
//            .genre(UPDATED_GENRE)
//            .cin(UPDATED_CIN)
//            .etat(UPDATED_ETAT)
//            .etatContractuelle(UPDATED_ETAT_CONTRACTUELLE)
//            .dateCreation(UPDATED_DATE_CREATION)
//            .dateDebutContrat(UPDATED_DATE_DEBUT_CONTRAT)
//            .idContratActif(UPDATED_ID_CONTRAT_ACTIF)
//            .idTypeContratActif(UPDATED_ID_TYPE_CONTRAT_ACTIF)
//            .userId(UPDATED_USER_ID);
//
//        restPersonneMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedPersonne.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonne))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Personne in the database
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
//        Personne testPersonne = personneList.get(personneList.size() - 1);
//        assertThat(testPersonne.getMatricule()).isEqualTo(UPDATED_MATRICULE);
//        assertThat(testPersonne.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
//        assertThat(testPersonne.getEmail()).isEqualTo(UPDATED_EMAIL);
//        assertThat(testPersonne.getNumTelephone()).isEqualTo(UPDATED_NUM_TELEPHONE);
//        assertThat(testPersonne.getGenre()).isEqualTo(UPDATED_GENRE);
//        assertThat(testPersonne.getCin()).isEqualTo(UPDATED_CIN);
//        assertThat(testPersonne.getEtat()).isEqualTo(UPDATED_ETAT);
//        assertThat(testPersonne.getEtatContractuelle()).isEqualTo(UPDATED_ETAT_CONTRACTUELLE);
//        assertThat(testPersonne.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
//        assertThat(testPersonne.getDateDebutContrat()).isEqualTo(UPDATED_DATE_DEBUT_CONTRAT);
//        assertThat(testPersonne.getIdContratActif()).isEqualTo(UPDATED_ID_CONTRAT_ACTIF);
//        assertThat(testPersonne.getIdTypeContratActif()).isEqualTo(UPDATED_ID_TYPE_CONTRAT_ACTIF);
//        assertThat(testPersonne.getUserId()).isEqualTo(UPDATED_USER_ID);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingPersonne() throws Exception {
//        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
//        personne.setId(count.incrementAndGet());
//
//        // Create the Personne
//        PersonneDTO personneDTO = personneMapper.toDto(personne);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restPersonneMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, personneDTO.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Personne in the database
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchPersonne() throws Exception {
//        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
//        personne.setId(count.incrementAndGet());
//
//        // Create the Personne
//        PersonneDTO personneDTO = personneMapper.toDto(personne);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restPersonneMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Personne in the database
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamPersonne() throws Exception {
//        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
//        personne.setId(count.incrementAndGet());
//
//        // Create the Personne
//        PersonneDTO personneDTO = personneMapper.toDto(personne);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restPersonneMockMvc
//            .perform(
//                patch(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Personne in the database
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deletePersonne() throws Exception {
//        // Initialize the database
//        personneRepository.saveAndFlush(personne);
//
//        int databaseSizeBeforeDelete = personneRepository.findAll().size();
//
//        // Delete the personne
//        restPersonneMockMvc
//            .perform(delete(ENTITY_API_URL_ID, personne.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<Personne> personneList = personneRepository.findAll();
//        assertThat(personneList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
