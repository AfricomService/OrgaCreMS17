package com.orgacare.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.ThemeFormation;
import com.orgacare.app.repository.ThemeFormationRepository;
import com.orgacare.app.service.dto.ThemeFormationDTO;
import com.orgacare.app.service.mapper.ThemeFormationMapper;
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
 * Integration tests for the {@link ThemeFormationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ThemeFormationResourceIT {

    private static final String DEFAULT_THEME = "AAAAAAAAAA";
    private static final String UPDATED_THEME = "BBBBBBBBBB";

    private static final Double DEFAULT_COUT = 1D;
    private static final Double UPDATED_COUT = 2D;

    private static final Integer DEFAULT_DUREE_VALIDITE_CERTIFICAT = 1;
    private static final Integer UPDATED_DUREE_VALIDITE_CERTIFICAT = 2;

    private static final Integer DEFAULT_NOTIFIER_EXPIRATION_AVANT = 1;
    private static final Integer UPDATED_NOTIFIER_EXPIRATION_AVANT = 2;

    private static final Integer DEFAULT_DUREE_ANTICIPATION_UTILISATEUR = 1;
    private static final Integer UPDATED_DUREE_ANTICIPATION_UTILISATEUR = 2;

    private static final Double DEFAULT_COUT_FORMATION_INDIVIDUELLE = 1D;
    private static final Double UPDATED_COUT_FORMATION_INDIVIDUELLE = 2D;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/theme-formations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ThemeFormationRepository themeFormationRepository;

    @Autowired
    private ThemeFormationMapper themeFormationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restThemeFormationMockMvc;

    private ThemeFormation themeFormation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThemeFormation createEntity(EntityManager em) {
        ThemeFormation themeFormation = new ThemeFormation()
            .theme(DEFAULT_THEME)
            .cout(DEFAULT_COUT)
            .duree_validite_certificat(DEFAULT_DUREE_VALIDITE_CERTIFICAT)
            .notifier_expiration_avant(DEFAULT_NOTIFIER_EXPIRATION_AVANT)
            .duree_anticipation_utilisateur(DEFAULT_DUREE_ANTICIPATION_UTILISATEUR)
            .cout_formation_individuelle(DEFAULT_COUT_FORMATION_INDIVIDUELLE)
            .status(DEFAULT_STATUS)
            .comments(DEFAULT_COMMENTS);
        return themeFormation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThemeFormation createUpdatedEntity(EntityManager em) {
        ThemeFormation themeFormation = new ThemeFormation()
            .theme(UPDATED_THEME)
            .cout(UPDATED_COUT)
            .duree_validite_certificat(UPDATED_DUREE_VALIDITE_CERTIFICAT)
            .notifier_expiration_avant(UPDATED_NOTIFIER_EXPIRATION_AVANT)
            .duree_anticipation_utilisateur(UPDATED_DUREE_ANTICIPATION_UTILISATEUR)
            .cout_formation_individuelle(UPDATED_COUT_FORMATION_INDIVIDUELLE)
            .status(UPDATED_STATUS)
            .comments(UPDATED_COMMENTS);
        return themeFormation;
    }

    @BeforeEach
    public void initTest() {
        themeFormation = createEntity(em);
    }

    @Test
    @Transactional
    void createThemeFormation() throws Exception {
        int databaseSizeBeforeCreate = themeFormationRepository.findAll().size();
        // Create the ThemeFormation
        ThemeFormationDTO themeFormationDTO = themeFormationMapper.toDto(themeFormation);
        restThemeFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeFormationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ThemeFormation in the database
        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeCreate + 1);
        ThemeFormation testThemeFormation = themeFormationList.get(themeFormationList.size() - 1);
        assertThat(testThemeFormation.getTheme()).isEqualTo(DEFAULT_THEME);
        assertThat(testThemeFormation.getCout()).isEqualTo(DEFAULT_COUT);
        assertThat(testThemeFormation.getDuree_validite_certificat()).isEqualTo(DEFAULT_DUREE_VALIDITE_CERTIFICAT);
        assertThat(testThemeFormation.getNotifier_expiration_avant()).isEqualTo(DEFAULT_NOTIFIER_EXPIRATION_AVANT);
        assertThat(testThemeFormation.getDuree_anticipation_utilisateur()).isEqualTo(DEFAULT_DUREE_ANTICIPATION_UTILISATEUR);
        assertThat(testThemeFormation.getCout_formation_individuelle()).isEqualTo(DEFAULT_COUT_FORMATION_INDIVIDUELLE);
        assertThat(testThemeFormation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testThemeFormation.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void createThemeFormationWithExistingId() throws Exception {
        // Create the ThemeFormation with an existing ID
        themeFormation.setId(1L);
        ThemeFormationDTO themeFormationDTO = themeFormationMapper.toDto(themeFormation);

        int databaseSizeBeforeCreate = themeFormationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restThemeFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFormation in the database
        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkThemeIsRequired() throws Exception {
        int databaseSizeBeforeTest = themeFormationRepository.findAll().size();
        // set the field null
        themeFormation.setTheme(null);

        // Create the ThemeFormation, which fails.
        ThemeFormationDTO themeFormationDTO = themeFormationMapper.toDto(themeFormation);

        restThemeFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllThemeFormations() throws Exception {
        // Initialize the database
        themeFormationRepository.saveAndFlush(themeFormation);

        // Get all the themeFormationList
        restThemeFormationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(themeFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].theme").value(hasItem(DEFAULT_THEME)))
            .andExpect(jsonPath("$.[*].cout").value(hasItem(DEFAULT_COUT.doubleValue())))
            .andExpect(jsonPath("$.[*].duree_validite_certificat").value(hasItem(DEFAULT_DUREE_VALIDITE_CERTIFICAT)))
            .andExpect(jsonPath("$.[*].notifier_expiration_avant").value(hasItem(DEFAULT_NOTIFIER_EXPIRATION_AVANT)))
            .andExpect(jsonPath("$.[*].duree_anticipation_utilisateur").value(hasItem(DEFAULT_DUREE_ANTICIPATION_UTILISATEUR)))
            .andExpect(jsonPath("$.[*].cout_formation_individuelle").value(hasItem(DEFAULT_COUT_FORMATION_INDIVIDUELLE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getThemeFormation() throws Exception {
        // Initialize the database
        themeFormationRepository.saveAndFlush(themeFormation);

        // Get the themeFormation
        restThemeFormationMockMvc
            .perform(get(ENTITY_API_URL_ID, themeFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(themeFormation.getId().intValue()))
            .andExpect(jsonPath("$.theme").value(DEFAULT_THEME))
            .andExpect(jsonPath("$.cout").value(DEFAULT_COUT.doubleValue()))
            .andExpect(jsonPath("$.duree_validite_certificat").value(DEFAULT_DUREE_VALIDITE_CERTIFICAT))
            .andExpect(jsonPath("$.notifier_expiration_avant").value(DEFAULT_NOTIFIER_EXPIRATION_AVANT))
            .andExpect(jsonPath("$.duree_anticipation_utilisateur").value(DEFAULT_DUREE_ANTICIPATION_UTILISATEUR))
            .andExpect(jsonPath("$.cout_formation_individuelle").value(DEFAULT_COUT_FORMATION_INDIVIDUELLE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingThemeFormation() throws Exception {
        // Get the themeFormation
        restThemeFormationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewThemeFormation() throws Exception {
        // Initialize the database
        themeFormationRepository.saveAndFlush(themeFormation);

        int databaseSizeBeforeUpdate = themeFormationRepository.findAll().size();

        // Update the themeFormation
        ThemeFormation updatedThemeFormation = themeFormationRepository.findById(themeFormation.getId()).get();
        // Disconnect from session so that the updates on updatedThemeFormation are not directly saved in db
        em.detach(updatedThemeFormation);
        updatedThemeFormation
            .theme(UPDATED_THEME)
            .cout(UPDATED_COUT)
            .duree_validite_certificat(UPDATED_DUREE_VALIDITE_CERTIFICAT)
            .notifier_expiration_avant(UPDATED_NOTIFIER_EXPIRATION_AVANT)
            .duree_anticipation_utilisateur(UPDATED_DUREE_ANTICIPATION_UTILISATEUR)
            .cout_formation_individuelle(UPDATED_COUT_FORMATION_INDIVIDUELLE)
            .status(UPDATED_STATUS)
            .comments(UPDATED_COMMENTS);
        ThemeFormationDTO themeFormationDTO = themeFormationMapper.toDto(updatedThemeFormation);

        restThemeFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, themeFormationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeFormationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ThemeFormation in the database
        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeUpdate);
        ThemeFormation testThemeFormation = themeFormationList.get(themeFormationList.size() - 1);
        assertThat(testThemeFormation.getTheme()).isEqualTo(UPDATED_THEME);
        assertThat(testThemeFormation.getCout()).isEqualTo(UPDATED_COUT);
        assertThat(testThemeFormation.getDuree_validite_certificat()).isEqualTo(UPDATED_DUREE_VALIDITE_CERTIFICAT);
        assertThat(testThemeFormation.getNotifier_expiration_avant()).isEqualTo(UPDATED_NOTIFIER_EXPIRATION_AVANT);
        assertThat(testThemeFormation.getDuree_anticipation_utilisateur()).isEqualTo(UPDATED_DUREE_ANTICIPATION_UTILISATEUR);
        assertThat(testThemeFormation.getCout_formation_individuelle()).isEqualTo(UPDATED_COUT_FORMATION_INDIVIDUELLE);
        assertThat(testThemeFormation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testThemeFormation.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void putNonExistingThemeFormation() throws Exception {
        int databaseSizeBeforeUpdate = themeFormationRepository.findAll().size();
        themeFormation.setId(count.incrementAndGet());

        // Create the ThemeFormation
        ThemeFormationDTO themeFormationDTO = themeFormationMapper.toDto(themeFormation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, themeFormationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFormation in the database
        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchThemeFormation() throws Exception {
        int databaseSizeBeforeUpdate = themeFormationRepository.findAll().size();
        themeFormation.setId(count.incrementAndGet());

        // Create the ThemeFormation
        ThemeFormationDTO themeFormationDTO = themeFormationMapper.toDto(themeFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFormation in the database
        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamThemeFormation() throws Exception {
        int databaseSizeBeforeUpdate = themeFormationRepository.findAll().size();
        themeFormation.setId(count.incrementAndGet());

        // Create the ThemeFormation
        ThemeFormationDTO themeFormationDTO = themeFormationMapper.toDto(themeFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeFormationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeFormationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ThemeFormation in the database
        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateThemeFormationWithPatch() throws Exception {
        // Initialize the database
        themeFormationRepository.saveAndFlush(themeFormation);

        int databaseSizeBeforeUpdate = themeFormationRepository.findAll().size();

        // Update the themeFormation using partial update
        ThemeFormation partialUpdatedThemeFormation = new ThemeFormation();
        partialUpdatedThemeFormation.setId(themeFormation.getId());

        partialUpdatedThemeFormation
            .duree_validite_certificat(UPDATED_DUREE_VALIDITE_CERTIFICAT)
            .notifier_expiration_avant(UPDATED_NOTIFIER_EXPIRATION_AVANT)
            .duree_anticipation_utilisateur(UPDATED_DUREE_ANTICIPATION_UTILISATEUR);

        restThemeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThemeFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedThemeFormation))
            )
            .andExpect(status().isOk());

        // Validate the ThemeFormation in the database
        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeUpdate);
        ThemeFormation testThemeFormation = themeFormationList.get(themeFormationList.size() - 1);
        assertThat(testThemeFormation.getTheme()).isEqualTo(DEFAULT_THEME);
        assertThat(testThemeFormation.getCout()).isEqualTo(DEFAULT_COUT);
        assertThat(testThemeFormation.getDuree_validite_certificat()).isEqualTo(UPDATED_DUREE_VALIDITE_CERTIFICAT);
        assertThat(testThemeFormation.getNotifier_expiration_avant()).isEqualTo(UPDATED_NOTIFIER_EXPIRATION_AVANT);
        assertThat(testThemeFormation.getDuree_anticipation_utilisateur()).isEqualTo(UPDATED_DUREE_ANTICIPATION_UTILISATEUR);
        assertThat(testThemeFormation.getCout_formation_individuelle()).isEqualTo(DEFAULT_COUT_FORMATION_INDIVIDUELLE);
        assertThat(testThemeFormation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testThemeFormation.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void fullUpdateThemeFormationWithPatch() throws Exception {
        // Initialize the database
        themeFormationRepository.saveAndFlush(themeFormation);

        int databaseSizeBeforeUpdate = themeFormationRepository.findAll().size();

        // Update the themeFormation using partial update
        ThemeFormation partialUpdatedThemeFormation = new ThemeFormation();
        partialUpdatedThemeFormation.setId(themeFormation.getId());

        partialUpdatedThemeFormation
            .theme(UPDATED_THEME)
            .cout(UPDATED_COUT)
            .duree_validite_certificat(UPDATED_DUREE_VALIDITE_CERTIFICAT)
            .notifier_expiration_avant(UPDATED_NOTIFIER_EXPIRATION_AVANT)
            .duree_anticipation_utilisateur(UPDATED_DUREE_ANTICIPATION_UTILISATEUR)
            .cout_formation_individuelle(UPDATED_COUT_FORMATION_INDIVIDUELLE)
            .status(UPDATED_STATUS)
            .comments(UPDATED_COMMENTS);

        restThemeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThemeFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedThemeFormation))
            )
            .andExpect(status().isOk());

        // Validate the ThemeFormation in the database
        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeUpdate);
        ThemeFormation testThemeFormation = themeFormationList.get(themeFormationList.size() - 1);
        assertThat(testThemeFormation.getTheme()).isEqualTo(UPDATED_THEME);
        assertThat(testThemeFormation.getCout()).isEqualTo(UPDATED_COUT);
        assertThat(testThemeFormation.getDuree_validite_certificat()).isEqualTo(UPDATED_DUREE_VALIDITE_CERTIFICAT);
        assertThat(testThemeFormation.getNotifier_expiration_avant()).isEqualTo(UPDATED_NOTIFIER_EXPIRATION_AVANT);
        assertThat(testThemeFormation.getDuree_anticipation_utilisateur()).isEqualTo(UPDATED_DUREE_ANTICIPATION_UTILISATEUR);
        assertThat(testThemeFormation.getCout_formation_individuelle()).isEqualTo(UPDATED_COUT_FORMATION_INDIVIDUELLE);
        assertThat(testThemeFormation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testThemeFormation.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingThemeFormation() throws Exception {
        int databaseSizeBeforeUpdate = themeFormationRepository.findAll().size();
        themeFormation.setId(count.incrementAndGet());

        // Create the ThemeFormation
        ThemeFormationDTO themeFormationDTO = themeFormationMapper.toDto(themeFormation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, themeFormationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFormation in the database
        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchThemeFormation() throws Exception {
        int databaseSizeBeforeUpdate = themeFormationRepository.findAll().size();
        themeFormation.setId(count.incrementAndGet());

        // Create the ThemeFormation
        ThemeFormationDTO themeFormationDTO = themeFormationMapper.toDto(themeFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFormation in the database
        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamThemeFormation() throws Exception {
        int databaseSizeBeforeUpdate = themeFormationRepository.findAll().size();
        themeFormation.setId(count.incrementAndGet());

        // Create the ThemeFormation
        ThemeFormationDTO themeFormationDTO = themeFormationMapper.toDto(themeFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeFormationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ThemeFormation in the database
        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteThemeFormation() throws Exception {
        // Initialize the database
        themeFormationRepository.saveAndFlush(themeFormation);

        int databaseSizeBeforeDelete = themeFormationRepository.findAll().size();

        // Delete the themeFormation
        restThemeFormationMockMvc
            .perform(delete(ENTITY_API_URL_ID, themeFormation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ThemeFormation> themeFormationList = themeFormationRepository.findAll();
        assertThat(themeFormationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
