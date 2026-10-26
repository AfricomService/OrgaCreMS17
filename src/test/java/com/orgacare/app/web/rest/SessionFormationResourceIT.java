package com.orgacare.app.web.rest;

import static com.orgacare.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.SessionFormation;
import com.orgacare.app.repository.SessionFormationRepository;
import com.orgacare.app.service.dto.SessionFormationDTO;
import com.orgacare.app.service.mapper.SessionFormationMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SessionFormationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SessionFormationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_PLANIFICATION_DU = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_PLANIFICATION_DU = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_PLANIFICATION_AU = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_PLANIFICATION_AU = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_EXECUTION_DU = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_EXECUTION_DU = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_EXECUTION_AU = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_EXECUTION_AU = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_REGLEMENTAIRE = false;
    private static final Boolean UPDATED_REGLEMENTAIRE = true;

    private static final String DEFAULT_TYPE_FORMATION = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_FORMATION = "BBBBBBBBBB";

    private static final String DEFAULT_BUDGET = "AAAAAAAAAA";
    private static final String UPDATED_BUDGET = "BBBBBBBBBB";

    private static final Double DEFAULT_COUT_SESSION = 1D;
    private static final Double UPDATED_COUT_SESSION = 2D;

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL_INFORMATIONS = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_INFORMATIONS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_THEME_ID = 1L;
    private static final Long UPDATED_THEME_ID = 2L;

    private static final Long DEFAULT_FORMATEUR_ID = 1L;
    private static final Long UPDATED_FORMATEUR_ID = 2L;

    private static final Long DEFAULT_SOCIETE_ID = 1L;
    private static final Long UPDATED_SOCIETE_ID = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/session-formations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SessionFormationRepository sessionFormationRepository;

    @Autowired
    private SessionFormationMapper sessionFormationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionFormationMockMvc;

    private SessionFormation sessionFormation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionFormation createEntity(EntityManager em) {
        SessionFormation sessionFormation = new SessionFormation()
            .title(DEFAULT_TITLE)
            .datePlanificationDu(DEFAULT_DATE_PLANIFICATION_DU)
            .datePlanificationAu(DEFAULT_DATE_PLANIFICATION_AU)
            .dateExecutionDu(DEFAULT_DATE_EXECUTION_DU)
            .dateExecutionAu(DEFAULT_DATE_EXECUTION_AU)
            .reglementaire(DEFAULT_REGLEMENTAIRE)
            .typeFormation(DEFAULT_TYPE_FORMATION)
            .budget(DEFAULT_BUDGET)
            .coutSession(DEFAULT_COUT_SESSION)
            .statut(DEFAULT_STATUT)
            .additionalInformations(DEFAULT_ADDITIONAL_INFORMATIONS)
            .dateCreation(DEFAULT_DATE_CREATION)
            .themeId(DEFAULT_THEME_ID)
            .formateurId(DEFAULT_FORMATEUR_ID)
            .societeId(DEFAULT_SOCIETE_ID)
            .description(DEFAULT_DESCRIPTION);
        return sessionFormation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionFormation createUpdatedEntity(EntityManager em) {
        SessionFormation sessionFormation = new SessionFormation()
            .title(UPDATED_TITLE)
            .datePlanificationDu(UPDATED_DATE_PLANIFICATION_DU)
            .datePlanificationAu(UPDATED_DATE_PLANIFICATION_AU)
            .dateExecutionDu(UPDATED_DATE_EXECUTION_DU)
            .dateExecutionAu(UPDATED_DATE_EXECUTION_AU)
            .reglementaire(UPDATED_REGLEMENTAIRE)
            .typeFormation(UPDATED_TYPE_FORMATION)
            .budget(UPDATED_BUDGET)
            .coutSession(UPDATED_COUT_SESSION)
            .statut(UPDATED_STATUT)
            .additionalInformations(UPDATED_ADDITIONAL_INFORMATIONS)
            .dateCreation(UPDATED_DATE_CREATION)
            .themeId(UPDATED_THEME_ID)
            .formateurId(UPDATED_FORMATEUR_ID)
            .societeId(UPDATED_SOCIETE_ID)
            .description(UPDATED_DESCRIPTION);
        return sessionFormation;
    }

    @BeforeEach
    public void initTest() {
        sessionFormation = createEntity(em);
    }

    @Test
    @Transactional
    void createSessionFormation() throws Exception {
        int databaseSizeBeforeCreate = sessionFormationRepository.findAll().size();
        // Create the SessionFormation
        SessionFormationDTO sessionFormationDTO = sessionFormationMapper.toDto(sessionFormation);
        restSessionFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SessionFormation in the database
        List<SessionFormation> sessionFormationList = sessionFormationRepository.findAll();
        assertThat(sessionFormationList).hasSize(databaseSizeBeforeCreate + 1);
        SessionFormation testSessionFormation = sessionFormationList.get(sessionFormationList.size() - 1);
        assertThat(testSessionFormation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSessionFormation.getDatePlanificationDu()).isEqualTo(DEFAULT_DATE_PLANIFICATION_DU);
        assertThat(testSessionFormation.getDatePlanificationAu()).isEqualTo(DEFAULT_DATE_PLANIFICATION_AU);
        assertThat(testSessionFormation.getDateExecutionDu()).isEqualTo(DEFAULT_DATE_EXECUTION_DU);
        assertThat(testSessionFormation.getDateExecutionAu()).isEqualTo(DEFAULT_DATE_EXECUTION_AU);
        assertThat(testSessionFormation.getReglementaire()).isEqualTo(DEFAULT_REGLEMENTAIRE);
        assertThat(testSessionFormation.getTypeFormation()).isEqualTo(DEFAULT_TYPE_FORMATION);
        assertThat(testSessionFormation.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testSessionFormation.getCoutSession()).isEqualTo(DEFAULT_COUT_SESSION);
        assertThat(testSessionFormation.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testSessionFormation.getAdditionalInformations()).isEqualTo(DEFAULT_ADDITIONAL_INFORMATIONS);
        assertThat(testSessionFormation.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testSessionFormation.getThemeId()).isEqualTo(DEFAULT_THEME_ID);
        assertThat(testSessionFormation.getFormateurId()).isEqualTo(DEFAULT_FORMATEUR_ID);
        assertThat(testSessionFormation.getSocieteId()).isEqualTo(DEFAULT_SOCIETE_ID);
        assertThat(testSessionFormation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createSessionFormationWithExistingId() throws Exception {
        // Create the SessionFormation with an existing ID
        sessionFormation.setId(1L);
        SessionFormationDTO sessionFormationDTO = sessionFormationMapper.toDto(sessionFormation);

        int databaseSizeBeforeCreate = sessionFormationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormation in the database
        List<SessionFormation> sessionFormationList = sessionFormationRepository.findAll();
        assertThat(sessionFormationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSessionFormations() throws Exception {
        // Initialize the database
        sessionFormationRepository.saveAndFlush(sessionFormation);

        // Get all the sessionFormationList
        restSessionFormationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].datePlanificationDu").value(hasItem(sameInstant(DEFAULT_DATE_PLANIFICATION_DU))))
            .andExpect(jsonPath("$.[*].datePlanificationAu").value(hasItem(sameInstant(DEFAULT_DATE_PLANIFICATION_AU))))
            .andExpect(jsonPath("$.[*].dateExecutionDu").value(hasItem(sameInstant(DEFAULT_DATE_EXECUTION_DU))))
            .andExpect(jsonPath("$.[*].dateExecutionAu").value(hasItem(sameInstant(DEFAULT_DATE_EXECUTION_AU))))
            .andExpect(jsonPath("$.[*].reglementaire").value(hasItem(DEFAULT_REGLEMENTAIRE.booleanValue())))
            .andExpect(jsonPath("$.[*].typeFormation").value(hasItem(DEFAULT_TYPE_FORMATION)))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET)))
            .andExpect(jsonPath("$.[*].coutSession").value(hasItem(DEFAULT_COUT_SESSION.doubleValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].additionalInformations").value(hasItem(DEFAULT_ADDITIONAL_INFORMATIONS)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))))
            .andExpect(jsonPath("$.[*].themeId").value(hasItem(DEFAULT_THEME_ID.intValue())))
            .andExpect(jsonPath("$.[*].formateurId").value(hasItem(DEFAULT_FORMATEUR_ID.intValue())))
            .andExpect(jsonPath("$.[*].societeId").value(hasItem(DEFAULT_SOCIETE_ID.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getSessionFormation() throws Exception {
        // Initialize the database
        sessionFormationRepository.saveAndFlush(sessionFormation);

        // Get the sessionFormation
        restSessionFormationMockMvc
            .perform(get(ENTITY_API_URL_ID, sessionFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sessionFormation.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.datePlanificationDu").value(sameInstant(DEFAULT_DATE_PLANIFICATION_DU)))
            .andExpect(jsonPath("$.datePlanificationAu").value(sameInstant(DEFAULT_DATE_PLANIFICATION_AU)))
            .andExpect(jsonPath("$.dateExecutionDu").value(sameInstant(DEFAULT_DATE_EXECUTION_DU)))
            .andExpect(jsonPath("$.dateExecutionAu").value(sameInstant(DEFAULT_DATE_EXECUTION_AU)))
            .andExpect(jsonPath("$.reglementaire").value(DEFAULT_REGLEMENTAIRE.booleanValue()))
            .andExpect(jsonPath("$.typeFormation").value(DEFAULT_TYPE_FORMATION))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET))
            .andExpect(jsonPath("$.coutSession").value(DEFAULT_COUT_SESSION.doubleValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT))
            .andExpect(jsonPath("$.additionalInformations").value(DEFAULT_ADDITIONAL_INFORMATIONS))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)))
            .andExpect(jsonPath("$.themeId").value(DEFAULT_THEME_ID.intValue()))
            .andExpect(jsonPath("$.formateurId").value(DEFAULT_FORMATEUR_ID.intValue()))
            .andExpect(jsonPath("$.societeId").value(DEFAULT_SOCIETE_ID.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingSessionFormation() throws Exception {
        // Get the sessionFormation
        restSessionFormationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSessionFormation() throws Exception {
        // Initialize the database
        sessionFormationRepository.saveAndFlush(sessionFormation);

        int databaseSizeBeforeUpdate = sessionFormationRepository.findAll().size();

        // Update the sessionFormation
        SessionFormation updatedSessionFormation = sessionFormationRepository.findById(sessionFormation.getId()).get();
        // Disconnect from session so that the updates on updatedSessionFormation are not directly saved in db
        em.detach(updatedSessionFormation);
        updatedSessionFormation
            .title(UPDATED_TITLE)
            .datePlanificationDu(UPDATED_DATE_PLANIFICATION_DU)
            .datePlanificationAu(UPDATED_DATE_PLANIFICATION_AU)
            .dateExecutionDu(UPDATED_DATE_EXECUTION_DU)
            .dateExecutionAu(UPDATED_DATE_EXECUTION_AU)
            .reglementaire(UPDATED_REGLEMENTAIRE)
            .typeFormation(UPDATED_TYPE_FORMATION)
            .budget(UPDATED_BUDGET)
            .coutSession(UPDATED_COUT_SESSION)
            .statut(UPDATED_STATUT)
            .additionalInformations(UPDATED_ADDITIONAL_INFORMATIONS)
            .dateCreation(UPDATED_DATE_CREATION)
            .themeId(UPDATED_THEME_ID)
            .formateurId(UPDATED_FORMATEUR_ID)
            .societeId(UPDATED_SOCIETE_ID)
            .description(UPDATED_DESCRIPTION);
        SessionFormationDTO sessionFormationDTO = sessionFormationMapper.toDto(updatedSessionFormation);

        restSessionFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionFormationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationDTO))
            )
            .andExpect(status().isOk());

        // Validate the SessionFormation in the database
        List<SessionFormation> sessionFormationList = sessionFormationRepository.findAll();
        assertThat(sessionFormationList).hasSize(databaseSizeBeforeUpdate);
        SessionFormation testSessionFormation = sessionFormationList.get(sessionFormationList.size() - 1);
        assertThat(testSessionFormation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSessionFormation.getDatePlanificationDu()).isEqualTo(UPDATED_DATE_PLANIFICATION_DU);
        assertThat(testSessionFormation.getDatePlanificationAu()).isEqualTo(UPDATED_DATE_PLANIFICATION_AU);
        assertThat(testSessionFormation.getDateExecutionDu()).isEqualTo(UPDATED_DATE_EXECUTION_DU);
        assertThat(testSessionFormation.getDateExecutionAu()).isEqualTo(UPDATED_DATE_EXECUTION_AU);
        assertThat(testSessionFormation.getReglementaire()).isEqualTo(UPDATED_REGLEMENTAIRE);
        assertThat(testSessionFormation.getTypeFormation()).isEqualTo(UPDATED_TYPE_FORMATION);
        assertThat(testSessionFormation.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testSessionFormation.getCoutSession()).isEqualTo(UPDATED_COUT_SESSION);
        assertThat(testSessionFormation.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testSessionFormation.getAdditionalInformations()).isEqualTo(UPDATED_ADDITIONAL_INFORMATIONS);
        assertThat(testSessionFormation.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testSessionFormation.getThemeId()).isEqualTo(UPDATED_THEME_ID);
        assertThat(testSessionFormation.getFormateurId()).isEqualTo(UPDATED_FORMATEUR_ID);
        assertThat(testSessionFormation.getSocieteId()).isEqualTo(UPDATED_SOCIETE_ID);
        assertThat(testSessionFormation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingSessionFormation() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationRepository.findAll().size();
        sessionFormation.setId(count.incrementAndGet());

        // Create the SessionFormation
        SessionFormationDTO sessionFormationDTO = sessionFormationMapper.toDto(sessionFormation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionFormationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormation in the database
        List<SessionFormation> sessionFormationList = sessionFormationRepository.findAll();
        assertThat(sessionFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSessionFormation() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationRepository.findAll().size();
        sessionFormation.setId(count.incrementAndGet());

        // Create the SessionFormation
        SessionFormationDTO sessionFormationDTO = sessionFormationMapper.toDto(sessionFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormation in the database
        List<SessionFormation> sessionFormationList = sessionFormationRepository.findAll();
        assertThat(sessionFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSessionFormation() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationRepository.findAll().size();
        sessionFormation.setId(count.incrementAndGet());

        // Create the SessionFormation
        SessionFormationDTO sessionFormationDTO = sessionFormationMapper.toDto(sessionFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionFormationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionFormation in the database
        List<SessionFormation> sessionFormationList = sessionFormationRepository.findAll();
        assertThat(sessionFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSessionFormationWithPatch() throws Exception {
        // Initialize the database
        sessionFormationRepository.saveAndFlush(sessionFormation);

        int databaseSizeBeforeUpdate = sessionFormationRepository.findAll().size();

        // Update the sessionFormation using partial update
        SessionFormation partialUpdatedSessionFormation = new SessionFormation();
        partialUpdatedSessionFormation.setId(sessionFormation.getId());

        partialUpdatedSessionFormation
            .typeFormation(UPDATED_TYPE_FORMATION)
            .additionalInformations(UPDATED_ADDITIONAL_INFORMATIONS)
            .themeId(UPDATED_THEME_ID);

        restSessionFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionFormation))
            )
            .andExpect(status().isOk());

        // Validate the SessionFormation in the database
        List<SessionFormation> sessionFormationList = sessionFormationRepository.findAll();
        assertThat(sessionFormationList).hasSize(databaseSizeBeforeUpdate);
        SessionFormation testSessionFormation = sessionFormationList.get(sessionFormationList.size() - 1);
        assertThat(testSessionFormation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSessionFormation.getDatePlanificationDu()).isEqualTo(DEFAULT_DATE_PLANIFICATION_DU);
        assertThat(testSessionFormation.getDatePlanificationAu()).isEqualTo(DEFAULT_DATE_PLANIFICATION_AU);
        assertThat(testSessionFormation.getDateExecutionDu()).isEqualTo(DEFAULT_DATE_EXECUTION_DU);
        assertThat(testSessionFormation.getDateExecutionAu()).isEqualTo(DEFAULT_DATE_EXECUTION_AU);
        assertThat(testSessionFormation.getReglementaire()).isEqualTo(DEFAULT_REGLEMENTAIRE);
        assertThat(testSessionFormation.getTypeFormation()).isEqualTo(UPDATED_TYPE_FORMATION);
        assertThat(testSessionFormation.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testSessionFormation.getCoutSession()).isEqualTo(DEFAULT_COUT_SESSION);
        assertThat(testSessionFormation.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testSessionFormation.getAdditionalInformations()).isEqualTo(UPDATED_ADDITIONAL_INFORMATIONS);
        assertThat(testSessionFormation.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testSessionFormation.getThemeId()).isEqualTo(UPDATED_THEME_ID);
        assertThat(testSessionFormation.getFormateurId()).isEqualTo(DEFAULT_FORMATEUR_ID);
        assertThat(testSessionFormation.getSocieteId()).isEqualTo(DEFAULT_SOCIETE_ID);
        assertThat(testSessionFormation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateSessionFormationWithPatch() throws Exception {
        // Initialize the database
        sessionFormationRepository.saveAndFlush(sessionFormation);

        int databaseSizeBeforeUpdate = sessionFormationRepository.findAll().size();

        // Update the sessionFormation using partial update
        SessionFormation partialUpdatedSessionFormation = new SessionFormation();
        partialUpdatedSessionFormation.setId(sessionFormation.getId());

        partialUpdatedSessionFormation
            .title(UPDATED_TITLE)
            .datePlanificationDu(UPDATED_DATE_PLANIFICATION_DU)
            .datePlanificationAu(UPDATED_DATE_PLANIFICATION_AU)
            .dateExecutionDu(UPDATED_DATE_EXECUTION_DU)
            .dateExecutionAu(UPDATED_DATE_EXECUTION_AU)
            .reglementaire(UPDATED_REGLEMENTAIRE)
            .typeFormation(UPDATED_TYPE_FORMATION)
            .budget(UPDATED_BUDGET)
            .coutSession(UPDATED_COUT_SESSION)
            .statut(UPDATED_STATUT)
            .additionalInformations(UPDATED_ADDITIONAL_INFORMATIONS)
            .dateCreation(UPDATED_DATE_CREATION)
            .themeId(UPDATED_THEME_ID)
            .formateurId(UPDATED_FORMATEUR_ID)
            .societeId(UPDATED_SOCIETE_ID)
            .description(UPDATED_DESCRIPTION);

        restSessionFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionFormation))
            )
            .andExpect(status().isOk());

        // Validate the SessionFormation in the database
        List<SessionFormation> sessionFormationList = sessionFormationRepository.findAll();
        assertThat(sessionFormationList).hasSize(databaseSizeBeforeUpdate);
        SessionFormation testSessionFormation = sessionFormationList.get(sessionFormationList.size() - 1);
        assertThat(testSessionFormation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSessionFormation.getDatePlanificationDu()).isEqualTo(UPDATED_DATE_PLANIFICATION_DU);
        assertThat(testSessionFormation.getDatePlanificationAu()).isEqualTo(UPDATED_DATE_PLANIFICATION_AU);
        assertThat(testSessionFormation.getDateExecutionDu()).isEqualTo(UPDATED_DATE_EXECUTION_DU);
        assertThat(testSessionFormation.getDateExecutionAu()).isEqualTo(UPDATED_DATE_EXECUTION_AU);
        assertThat(testSessionFormation.getReglementaire()).isEqualTo(UPDATED_REGLEMENTAIRE);
        assertThat(testSessionFormation.getTypeFormation()).isEqualTo(UPDATED_TYPE_FORMATION);
        assertThat(testSessionFormation.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testSessionFormation.getCoutSession()).isEqualTo(UPDATED_COUT_SESSION);
        assertThat(testSessionFormation.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testSessionFormation.getAdditionalInformations()).isEqualTo(UPDATED_ADDITIONAL_INFORMATIONS);
        assertThat(testSessionFormation.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testSessionFormation.getThemeId()).isEqualTo(UPDATED_THEME_ID);
        assertThat(testSessionFormation.getFormateurId()).isEqualTo(UPDATED_FORMATEUR_ID);
        assertThat(testSessionFormation.getSocieteId()).isEqualTo(UPDATED_SOCIETE_ID);
        assertThat(testSessionFormation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingSessionFormation() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationRepository.findAll().size();
        sessionFormation.setId(count.incrementAndGet());

        // Create the SessionFormation
        SessionFormationDTO sessionFormationDTO = sessionFormationMapper.toDto(sessionFormation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionFormationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormation in the database
        List<SessionFormation> sessionFormationList = sessionFormationRepository.findAll();
        assertThat(sessionFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSessionFormation() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationRepository.findAll().size();
        sessionFormation.setId(count.incrementAndGet());

        // Create the SessionFormation
        SessionFormationDTO sessionFormationDTO = sessionFormationMapper.toDto(sessionFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormation in the database
        List<SessionFormation> sessionFormationList = sessionFormationRepository.findAll();
        assertThat(sessionFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSessionFormation() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationRepository.findAll().size();
        sessionFormation.setId(count.incrementAndGet());

        // Create the SessionFormation
        SessionFormationDTO sessionFormationDTO = sessionFormationMapper.toDto(sessionFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionFormationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionFormation in the database
        List<SessionFormation> sessionFormationList = sessionFormationRepository.findAll();
        assertThat(sessionFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSessionFormation() throws Exception {
        // Initialize the database
        sessionFormationRepository.saveAndFlush(sessionFormation);

        int databaseSizeBeforeDelete = sessionFormationRepository.findAll().size();

        // Delete the sessionFormation
        restSessionFormationMockMvc
            .perform(delete(ENTITY_API_URL_ID, sessionFormation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SessionFormation> sessionFormationList = sessionFormationRepository.findAll();
        assertThat(sessionFormationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
