package com.orgacare.app.web.rest;

import static com.orgacare.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.Affectation;
import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.domain.enumeration.TypeAffectation;
import com.orgacare.app.repository.AffectationRepository;
import com.orgacare.app.service.dto.AffectationDTO;
import com.orgacare.app.service.mapper.AffectationMapper;
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
 * Integration tests for the {@link AffectationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AffectationResourceIT {

    private static final TypeAffectation DEFAULT_TYPE = TypeAffectation.MEMBRE;
    private static final TypeAffectation UPDATED_TYPE = TypeAffectation.CHEF;

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_ACTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ACTION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Etat DEFAULT_ETAT = Etat.DRAFT;
    private static final Etat UPDATED_ETAT = Etat.ACTIF;

    private static final String ENTITY_API_URL = "/api/affectations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AffectationRepository affectationRepository;

    @Autowired
    private AffectationMapper affectationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAffectationMockMvc;

    private Affectation affectation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affectation createEntity(EntityManager em) {
        Affectation affectation = new Affectation()
            .type(DEFAULT_TYPE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateAction(DEFAULT_DATE_ACTION)
            .dateFin(DEFAULT_DATE_FIN)
            .etat(DEFAULT_ETAT);
        return affectation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affectation createUpdatedEntity(EntityManager em) {
        Affectation affectation = new Affectation()
            .type(UPDATED_TYPE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateAction(UPDATED_DATE_ACTION)
            .dateFin(UPDATED_DATE_FIN)
            .etat(UPDATED_ETAT);
        return affectation;
    }

    @BeforeEach
    public void initTest() {
        affectation = createEntity(em);
    }

    @Test
    @Transactional
    void createAffectation() throws Exception {
        int databaseSizeBeforeCreate = affectationRepository.findAll().size();
        // Create the Affectation
        AffectationDTO affectationDTO = affectationMapper.toDto(affectation);
        restAffectationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeCreate + 1);
        Affectation testAffectation = affectationList.get(affectationList.size() - 1);
        assertThat(testAffectation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAffectation.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testAffectation.getDateAction()).isEqualTo(DEFAULT_DATE_ACTION);
        assertThat(testAffectation.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testAffectation.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void createAffectationWithExistingId() throws Exception {
        // Create the Affectation with an existing ID
        affectation.setId(1L);
        AffectationDTO affectationDTO = affectationMapper.toDto(affectation);

        int databaseSizeBeforeCreate = affectationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAffectationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = affectationRepository.findAll().size();
        // set the field null
        affectation.setType(null);

        // Create the Affectation, which fails.
        AffectationDTO affectationDTO = affectationMapper.toDto(affectation);

        restAffectationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtatIsRequired() throws Exception {
        int databaseSizeBeforeTest = affectationRepository.findAll().size();
        // set the field null
        affectation.setEtat(null);

        // Create the Affectation, which fails.
        AffectationDTO affectationDTO = affectationMapper.toDto(affectation);

        restAffectationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAffectations() throws Exception {
        // Initialize the database
        affectationRepository.saveAndFlush(affectation);

        // Get all the affectationList
        restAffectationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affectation.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))))
            .andExpect(jsonPath("$.[*].dateAction").value(hasItem(sameInstant(DEFAULT_DATE_ACTION))))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(sameInstant(DEFAULT_DATE_FIN))))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    void getAffectation() throws Exception {
        // Initialize the database
        affectationRepository.saveAndFlush(affectation);

        // Get the affectation
        restAffectationMockMvc
            .perform(get(ENTITY_API_URL_ID, affectation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(affectation.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)))
            .andExpect(jsonPath("$.dateAction").value(sameInstant(DEFAULT_DATE_ACTION)))
            .andExpect(jsonPath("$.dateFin").value(sameInstant(DEFAULT_DATE_FIN)))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAffectation() throws Exception {
        // Get the affectation
        restAffectationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAffectation() throws Exception {
        // Initialize the database
        affectationRepository.saveAndFlush(affectation);

        int databaseSizeBeforeUpdate = affectationRepository.findAll().size();

        // Update the affectation
        Affectation updatedAffectation = affectationRepository.findById(affectation.getId()).get();
        // Disconnect from session so that the updates on updatedAffectation are not directly saved in db
        em.detach(updatedAffectation);
        updatedAffectation
            .type(UPDATED_TYPE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateAction(UPDATED_DATE_ACTION)
            .dateFin(UPDATED_DATE_FIN)
            .etat(UPDATED_ETAT);
        AffectationDTO affectationDTO = affectationMapper.toDto(updatedAffectation);

        restAffectationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affectationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
        Affectation testAffectation = affectationList.get(affectationList.size() - 1);
        assertThat(testAffectation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAffectation.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testAffectation.getDateAction()).isEqualTo(UPDATED_DATE_ACTION);
        assertThat(testAffectation.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testAffectation.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void putNonExistingAffectation() throws Exception {
        int databaseSizeBeforeUpdate = affectationRepository.findAll().size();
        affectation.setId(count.incrementAndGet());

        // Create the Affectation
        AffectationDTO affectationDTO = affectationMapper.toDto(affectation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affectationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAffectation() throws Exception {
        int databaseSizeBeforeUpdate = affectationRepository.findAll().size();
        affectation.setId(count.incrementAndGet());

        // Create the Affectation
        AffectationDTO affectationDTO = affectationMapper.toDto(affectation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAffectation() throws Exception {
        int databaseSizeBeforeUpdate = affectationRepository.findAll().size();
        affectation.setId(count.incrementAndGet());

        // Create the Affectation
        AffectationDTO affectationDTO = affectationMapper.toDto(affectation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAffectationWithPatch() throws Exception {
        // Initialize the database
        affectationRepository.saveAndFlush(affectation);

        int databaseSizeBeforeUpdate = affectationRepository.findAll().size();

        // Update the affectation using partial update
        Affectation partialUpdatedAffectation = new Affectation();
        partialUpdatedAffectation.setId(affectation.getId());

        partialUpdatedAffectation.type(UPDATED_TYPE).dateFin(UPDATED_DATE_FIN);

        restAffectationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffectation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffectation))
            )
            .andExpect(status().isOk());

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
        Affectation testAffectation = affectationList.get(affectationList.size() - 1);
        assertThat(testAffectation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAffectation.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testAffectation.getDateAction()).isEqualTo(DEFAULT_DATE_ACTION);
        assertThat(testAffectation.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testAffectation.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void fullUpdateAffectationWithPatch() throws Exception {
        // Initialize the database
        affectationRepository.saveAndFlush(affectation);

        int databaseSizeBeforeUpdate = affectationRepository.findAll().size();

        // Update the affectation using partial update
        Affectation partialUpdatedAffectation = new Affectation();
        partialUpdatedAffectation.setId(affectation.getId());

        partialUpdatedAffectation
            .type(UPDATED_TYPE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateAction(UPDATED_DATE_ACTION)
            .dateFin(UPDATED_DATE_FIN)
            .etat(UPDATED_ETAT);

        restAffectationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffectation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffectation))
            )
            .andExpect(status().isOk());

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
        Affectation testAffectation = affectationList.get(affectationList.size() - 1);
        assertThat(testAffectation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAffectation.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testAffectation.getDateAction()).isEqualTo(UPDATED_DATE_ACTION);
        assertThat(testAffectation.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testAffectation.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void patchNonExistingAffectation() throws Exception {
        int databaseSizeBeforeUpdate = affectationRepository.findAll().size();
        affectation.setId(count.incrementAndGet());

        // Create the Affectation
        AffectationDTO affectationDTO = affectationMapper.toDto(affectation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, affectationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affectationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAffectation() throws Exception {
        int databaseSizeBeforeUpdate = affectationRepository.findAll().size();
        affectation.setId(count.incrementAndGet());

        // Create the Affectation
        AffectationDTO affectationDTO = affectationMapper.toDto(affectation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affectationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAffectation() throws Exception {
        int databaseSizeBeforeUpdate = affectationRepository.findAll().size();
        affectation.setId(count.incrementAndGet());

        // Create the Affectation
        AffectationDTO affectationDTO = affectationMapper.toDto(affectation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affectationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Affectation in the database
        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAffectation() throws Exception {
        // Initialize the database
        affectationRepository.saveAndFlush(affectation);

        int databaseSizeBeforeDelete = affectationRepository.findAll().size();

        // Delete the affectation
        restAffectationMockMvc
            .perform(delete(ENTITY_API_URL_ID, affectation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Affectation> affectationList = affectationRepository.findAll();
        assertThat(affectationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
