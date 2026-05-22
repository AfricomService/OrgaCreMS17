package com.orgacare.app.web.rest;

import static com.orgacare.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.Organigramme;
import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.repository.OrganigrammeRepository;
import com.orgacare.app.service.dto.OrganigrammeDTO;
import com.orgacare.app.service.mapper.OrganigrammeMapper;
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
 * Integration tests for the {@link OrganigrammeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganigrammeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_ACTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ACTION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_EXPIRATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_EXPIRATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Etat DEFAULT_ETAT = Etat.DRAFT;
    private static final Etat UPDATED_ETAT = Etat.ACTIF;

    private static final String ENTITY_API_URL = "/api/organigrammes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganigrammeRepository organigrammeRepository;

    @Autowired
    private OrganigrammeMapper organigrammeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganigrammeMockMvc;

    private Organigramme organigramme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organigramme createEntity(EntityManager em) {
        Organigramme organigramme = new Organigramme()
            .code(DEFAULT_CODE)
            .nom(DEFAULT_NOM)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateAction(DEFAULT_DATE_ACTION)
            .dateExpiration(DEFAULT_DATE_EXPIRATION)
            .etat(DEFAULT_ETAT);
        return organigramme;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organigramme createUpdatedEntity(EntityManager em) {
        Organigramme organigramme = new Organigramme()
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateAction(UPDATED_DATE_ACTION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .etat(UPDATED_ETAT);
        return organigramme;
    }

    @BeforeEach
    public void initTest() {
        organigramme = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganigramme() throws Exception {
        int databaseSizeBeforeCreate = organigrammeRepository.findAll().size();
        // Create the Organigramme
        OrganigrammeDTO organigrammeDTO = organigrammeMapper.toDto(organigramme);
        restOrganigrammeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organigrammeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Organigramme in the database
        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeCreate + 1);
        Organigramme testOrganigramme = organigrammeList.get(organigrammeList.size() - 1);
        assertThat(testOrganigramme.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrganigramme.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testOrganigramme.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testOrganigramme.getDateAction()).isEqualTo(DEFAULT_DATE_ACTION);
        assertThat(testOrganigramme.getDateExpiration()).isEqualTo(DEFAULT_DATE_EXPIRATION);
        assertThat(testOrganigramme.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void createOrganigrammeWithExistingId() throws Exception {
        // Create the Organigramme with an existing ID
        organigramme.setId(1L);
        OrganigrammeDTO organigrammeDTO = organigrammeMapper.toDto(organigramme);

        int databaseSizeBeforeCreate = organigrammeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganigrammeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organigrammeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organigramme in the database
        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEtatIsRequired() throws Exception {
        int databaseSizeBeforeTest = organigrammeRepository.findAll().size();
        // set the field null
        organigramme.setEtat(null);

        // Create the Organigramme, which fails.
        OrganigrammeDTO organigrammeDTO = organigrammeMapper.toDto(organigramme);

        restOrganigrammeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organigrammeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganigrammes() throws Exception {
        // Initialize the database
        organigrammeRepository.saveAndFlush(organigramme);

        // Get all the organigrammeList
        restOrganigrammeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organigramme.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))))
            .andExpect(jsonPath("$.[*].dateAction").value(hasItem(sameInstant(DEFAULT_DATE_ACTION))))
            .andExpect(jsonPath("$.[*].dateExpiration").value(hasItem(sameInstant(DEFAULT_DATE_EXPIRATION))))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    void getOrganigramme() throws Exception {
        // Initialize the database
        organigrammeRepository.saveAndFlush(organigramme);

        // Get the organigramme
        restOrganigrammeMockMvc
            .perform(get(ENTITY_API_URL_ID, organigramme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organigramme.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)))
            .andExpect(jsonPath("$.dateAction").value(sameInstant(DEFAULT_DATE_ACTION)))
            .andExpect(jsonPath("$.dateExpiration").value(sameInstant(DEFAULT_DATE_EXPIRATION)))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrganigramme() throws Exception {
        // Get the organigramme
        restOrganigrammeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrganigramme() throws Exception {
        // Initialize the database
        organigrammeRepository.saveAndFlush(organigramme);

        int databaseSizeBeforeUpdate = organigrammeRepository.findAll().size();

        // Update the organigramme
        Organigramme updatedOrganigramme = organigrammeRepository.findById(organigramme.getId()).get();
        // Disconnect from session so that the updates on updatedOrganigramme are not directly saved in db
        em.detach(updatedOrganigramme);
        updatedOrganigramme
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateAction(UPDATED_DATE_ACTION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .etat(UPDATED_ETAT);
        OrganigrammeDTO organigrammeDTO = organigrammeMapper.toDto(updatedOrganigramme);

        restOrganigrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organigrammeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organigrammeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Organigramme in the database
        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeUpdate);
        Organigramme testOrganigramme = organigrammeList.get(organigrammeList.size() - 1);
        assertThat(testOrganigramme.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganigramme.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testOrganigramme.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testOrganigramme.getDateAction()).isEqualTo(UPDATED_DATE_ACTION);
        assertThat(testOrganigramme.getDateExpiration()).isEqualTo(UPDATED_DATE_EXPIRATION);
        assertThat(testOrganigramme.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void putNonExistingOrganigramme() throws Exception {
        int databaseSizeBeforeUpdate = organigrammeRepository.findAll().size();
        organigramme.setId(count.incrementAndGet());

        // Create the Organigramme
        OrganigrammeDTO organigrammeDTO = organigrammeMapper.toDto(organigramme);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganigrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organigrammeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organigrammeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organigramme in the database
        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganigramme() throws Exception {
        int databaseSizeBeforeUpdate = organigrammeRepository.findAll().size();
        organigramme.setId(count.incrementAndGet());

        // Create the Organigramme
        OrganigrammeDTO organigrammeDTO = organigrammeMapper.toDto(organigramme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganigrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organigrammeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organigramme in the database
        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganigramme() throws Exception {
        int databaseSizeBeforeUpdate = organigrammeRepository.findAll().size();
        organigramme.setId(count.incrementAndGet());

        // Create the Organigramme
        OrganigrammeDTO organigrammeDTO = organigrammeMapper.toDto(organigramme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganigrammeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organigrammeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organigramme in the database
        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganigrammeWithPatch() throws Exception {
        // Initialize the database
        organigrammeRepository.saveAndFlush(organigramme);

        int databaseSizeBeforeUpdate = organigrammeRepository.findAll().size();

        // Update the organigramme using partial update
        Organigramme partialUpdatedOrganigramme = new Organigramme();
        partialUpdatedOrganigramme.setId(organigramme.getId());

        partialUpdatedOrganigramme
            .dateCreation(UPDATED_DATE_CREATION)
            .dateAction(UPDATED_DATE_ACTION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .etat(UPDATED_ETAT);

        restOrganigrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganigramme.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganigramme))
            )
            .andExpect(status().isOk());

        // Validate the Organigramme in the database
        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeUpdate);
        Organigramme testOrganigramme = organigrammeList.get(organigrammeList.size() - 1);
        assertThat(testOrganigramme.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrganigramme.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testOrganigramme.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testOrganigramme.getDateAction()).isEqualTo(UPDATED_DATE_ACTION);
        assertThat(testOrganigramme.getDateExpiration()).isEqualTo(UPDATED_DATE_EXPIRATION);
        assertThat(testOrganigramme.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void fullUpdateOrganigrammeWithPatch() throws Exception {
        // Initialize the database
        organigrammeRepository.saveAndFlush(organigramme);

        int databaseSizeBeforeUpdate = organigrammeRepository.findAll().size();

        // Update the organigramme using partial update
        Organigramme partialUpdatedOrganigramme = new Organigramme();
        partialUpdatedOrganigramme.setId(organigramme.getId());

        partialUpdatedOrganigramme
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateAction(UPDATED_DATE_ACTION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .etat(UPDATED_ETAT);

        restOrganigrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganigramme.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganigramme))
            )
            .andExpect(status().isOk());

        // Validate the Organigramme in the database
        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeUpdate);
        Organigramme testOrganigramme = organigrammeList.get(organigrammeList.size() - 1);
        assertThat(testOrganigramme.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganigramme.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testOrganigramme.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testOrganigramme.getDateAction()).isEqualTo(UPDATED_DATE_ACTION);
        assertThat(testOrganigramme.getDateExpiration()).isEqualTo(UPDATED_DATE_EXPIRATION);
        assertThat(testOrganigramme.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void patchNonExistingOrganigramme() throws Exception {
        int databaseSizeBeforeUpdate = organigrammeRepository.findAll().size();
        organigramme.setId(count.incrementAndGet());

        // Create the Organigramme
        OrganigrammeDTO organigrammeDTO = organigrammeMapper.toDto(organigramme);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganigrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organigrammeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organigrammeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organigramme in the database
        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganigramme() throws Exception {
        int databaseSizeBeforeUpdate = organigrammeRepository.findAll().size();
        organigramme.setId(count.incrementAndGet());

        // Create the Organigramme
        OrganigrammeDTO organigrammeDTO = organigrammeMapper.toDto(organigramme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganigrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organigrammeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organigramme in the database
        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganigramme() throws Exception {
        int databaseSizeBeforeUpdate = organigrammeRepository.findAll().size();
        organigramme.setId(count.incrementAndGet());

        // Create the Organigramme
        OrganigrammeDTO organigrammeDTO = organigrammeMapper.toDto(organigramme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganigrammeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organigrammeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organigramme in the database
        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganigramme() throws Exception {
        // Initialize the database
        organigrammeRepository.saveAndFlush(organigramme);

        int databaseSizeBeforeDelete = organigrammeRepository.findAll().size();

        // Delete the organigramme
        restOrganigrammeMockMvc
            .perform(delete(ENTITY_API_URL_ID, organigramme.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Organigramme> organigrammeList = organigrammeRepository.findAll();
        assertThat(organigrammeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
