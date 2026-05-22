package com.orgacare.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.Fonction;
import com.orgacare.app.repository.FonctionRepository;
import com.orgacare.app.service.dto.FonctionDTO;
import com.orgacare.app.service.mapper.FonctionMapper;
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
 * Integration tests for the {@link FonctionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FonctionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fonctions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FonctionRepository fonctionRepository;

    @Autowired
    private FonctionMapper fonctionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFonctionMockMvc;

    private Fonction fonction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fonction createEntity(EntityManager em) {
        Fonction fonction = new Fonction().code(DEFAULT_CODE).label(DEFAULT_LABEL);
        return fonction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fonction createUpdatedEntity(EntityManager em) {
        Fonction fonction = new Fonction().code(UPDATED_CODE).label(UPDATED_LABEL);
        return fonction;
    }

    @BeforeEach
    public void initTest() {
        fonction = createEntity(em);
    }

    @Test
    @Transactional
    void createFonction() throws Exception {
        int databaseSizeBeforeCreate = fonctionRepository.findAll().size();
        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);
        restFonctionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeCreate + 1);
        Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
        assertThat(testFonction.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testFonction.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void createFonctionWithExistingId() throws Exception {
        // Create the Fonction with an existing ID
        fonction.setId(1L);
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        int databaseSizeBeforeCreate = fonctionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFonctionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFonctions() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList
        restFonctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fonction.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }

    @Test
    @Transactional
    void getFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get the fonction
        restFonctionMockMvc
            .perform(get(ENTITY_API_URL_ID, fonction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fonction.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }

    @Test
    @Transactional
    void getNonExistingFonction() throws Exception {
        // Get the fonction
        restFonctionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();

        // Update the fonction
        Fonction updatedFonction = fonctionRepository.findById(fonction.getId()).get();
        // Disconnect from session so that the updates on updatedFonction are not directly saved in db
        em.detach(updatedFonction);
        updatedFonction.code(UPDATED_CODE).label(UPDATED_LABEL);
        FonctionDTO fonctionDTO = fonctionMapper.toDto(updatedFonction);

        restFonctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fonctionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
        Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
        assertThat(testFonction.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFonction.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void putNonExistingFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
        fonction.setId(count.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fonctionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
        fonction.setId(count.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
        fonction.setId(count.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFonctionWithPatch() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();

        // Update the fonction using partial update
        Fonction partialUpdatedFonction = new Fonction();
        partialUpdatedFonction.setId(fonction.getId());

        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFonction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFonction))
            )
            .andExpect(status().isOk());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
        Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
        assertThat(testFonction.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testFonction.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void fullUpdateFonctionWithPatch() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();

        // Update the fonction using partial update
        Fonction partialUpdatedFonction = new Fonction();
        partialUpdatedFonction.setId(fonction.getId());

        partialUpdatedFonction.code(UPDATED_CODE).label(UPDATED_LABEL);

        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFonction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFonction))
            )
            .andExpect(status().isOk());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
        Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
        assertThat(testFonction.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFonction.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void patchNonExistingFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
        fonction.setId(count.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fonctionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
        fonction.setId(count.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
        fonction.setId(count.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        int databaseSizeBeforeDelete = fonctionRepository.findAll().size();

        // Delete the fonction
        restFonctionMockMvc
            .perform(delete(ENTITY_API_URL_ID, fonction.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
