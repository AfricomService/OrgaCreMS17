package com.orgacare.app.web.rest;

import static com.orgacare.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.PieceJointe;
import com.orgacare.app.repository.PieceJointeRepository;
import com.orgacare.app.service.dto.PieceJointeDTO;
import com.orgacare.app.service.mapper.PieceJointeMapper;
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
 * Integration tests for the {@link PieceJointeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PieceJointeResourceIT {

    private static final String DEFAULT_NOM_FICHIER = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FICHIER = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FICHIER_URL = "AAAAAAAAAA";
    private static final String UPDATED_FICHIER_URL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_UPLOAD = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_UPLOAD = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_FORMATEUR_ID = 1L;
    private static final Long UPDATED_FORMATEUR_ID = 2L;

    private static final Long DEFAULT_SESSION_ID = 1L;
    private static final Long UPDATED_SESSION_ID = 2L;

    private static final Long DEFAULT_FORMATION_ID = 1L;
    private static final Long UPDATED_FORMATION_ID = 2L;

    private static final String ENTITY_API_URL = "/api/piece-jointes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PieceJointeRepository pieceJointeRepository;

    @Autowired
    private PieceJointeMapper pieceJointeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPieceJointeMockMvc;

    private PieceJointe pieceJointe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PieceJointe createEntity(EntityManager em) {
        PieceJointe pieceJointe = new PieceJointe()
            .nomFichier(DEFAULT_NOM_FICHIER)
            .type(DEFAULT_TYPE)
            .fichierURL(DEFAULT_FICHIER_URL)
            .dateUpload(DEFAULT_DATE_UPLOAD)
            .formateurId(DEFAULT_FORMATEUR_ID)
            .sessionId(DEFAULT_SESSION_ID)
            .formationId(DEFAULT_FORMATION_ID);
        return pieceJointe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PieceJointe createUpdatedEntity(EntityManager em) {
        PieceJointe pieceJointe = new PieceJointe()
            .nomFichier(UPDATED_NOM_FICHIER)
            .type(UPDATED_TYPE)
            .fichierURL(UPDATED_FICHIER_URL)
            .dateUpload(UPDATED_DATE_UPLOAD)
            .formateurId(UPDATED_FORMATEUR_ID)
            .sessionId(UPDATED_SESSION_ID)
            .formationId(UPDATED_FORMATION_ID);
        return pieceJointe;
    }

    @BeforeEach
    public void initTest() {
        pieceJointe = createEntity(em);
    }

    @Test
    @Transactional
    void createPieceJointe() throws Exception {
        int databaseSizeBeforeCreate = pieceJointeRepository.findAll().size();
        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);
        restPieceJointeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeCreate + 1);
        PieceJointe testPieceJointe = pieceJointeList.get(pieceJointeList.size() - 1);
        assertThat(testPieceJointe.getNomFichier()).isEqualTo(DEFAULT_NOM_FICHIER);
        assertThat(testPieceJointe.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPieceJointe.getFichierURL()).isEqualTo(DEFAULT_FICHIER_URL);
        assertThat(testPieceJointe.getDateUpload()).isEqualTo(DEFAULT_DATE_UPLOAD);
        assertThat(testPieceJointe.getFormateurId()).isEqualTo(DEFAULT_FORMATEUR_ID);
        assertThat(testPieceJointe.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
        assertThat(testPieceJointe.getFormationId()).isEqualTo(DEFAULT_FORMATION_ID);
    }

    @Test
    @Transactional
    void createPieceJointeWithExistingId() throws Exception {
        // Create the PieceJointe with an existing ID
        pieceJointe.setId(1L);
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        int databaseSizeBeforeCreate = pieceJointeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPieceJointeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPieceJointes() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList
        restPieceJointeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pieceJointe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFichier").value(hasItem(DEFAULT_NOM_FICHIER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].fichierURL").value(hasItem(DEFAULT_FICHIER_URL)))
            .andExpect(jsonPath("$.[*].dateUpload").value(hasItem(sameInstant(DEFAULT_DATE_UPLOAD))))
            .andExpect(jsonPath("$.[*].formateurId").value(hasItem(DEFAULT_FORMATEUR_ID.intValue())))
            .andExpect(jsonPath("$.[*].sessionId").value(hasItem(DEFAULT_SESSION_ID.intValue())))
            .andExpect(jsonPath("$.[*].formationId").value(hasItem(DEFAULT_FORMATION_ID.intValue())));
    }

    @Test
    @Transactional
    void getPieceJointe() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get the pieceJointe
        restPieceJointeMockMvc
            .perform(get(ENTITY_API_URL_ID, pieceJointe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pieceJointe.getId().intValue()))
            .andExpect(jsonPath("$.nomFichier").value(DEFAULT_NOM_FICHIER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.fichierURL").value(DEFAULT_FICHIER_URL))
            .andExpect(jsonPath("$.dateUpload").value(sameInstant(DEFAULT_DATE_UPLOAD)))
            .andExpect(jsonPath("$.formateurId").value(DEFAULT_FORMATEUR_ID.intValue()))
            .andExpect(jsonPath("$.sessionId").value(DEFAULT_SESSION_ID.intValue()))
            .andExpect(jsonPath("$.formationId").value(DEFAULT_FORMATION_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPieceJointe() throws Exception {
        // Get the pieceJointe
        restPieceJointeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPieceJointe() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();

        // Update the pieceJointe
        PieceJointe updatedPieceJointe = pieceJointeRepository.findById(pieceJointe.getId()).get();
        // Disconnect from session so that the updates on updatedPieceJointe are not directly saved in db
        em.detach(updatedPieceJointe);
        updatedPieceJointe
            .nomFichier(UPDATED_NOM_FICHIER)
            .type(UPDATED_TYPE)
            .fichierURL(UPDATED_FICHIER_URL)
            .dateUpload(UPDATED_DATE_UPLOAD)
            .formateurId(UPDATED_FORMATEUR_ID)
            .sessionId(UPDATED_SESSION_ID)
            .formationId(UPDATED_FORMATION_ID);
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(updatedPieceJointe);

        restPieceJointeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pieceJointeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isOk());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
        PieceJointe testPieceJointe = pieceJointeList.get(pieceJointeList.size() - 1);
        assertThat(testPieceJointe.getNomFichier()).isEqualTo(UPDATED_NOM_FICHIER);
        assertThat(testPieceJointe.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPieceJointe.getFichierURL()).isEqualTo(UPDATED_FICHIER_URL);
        assertThat(testPieceJointe.getDateUpload()).isEqualTo(UPDATED_DATE_UPLOAD);
        assertThat(testPieceJointe.getFormateurId()).isEqualTo(UPDATED_FORMATEUR_ID);
        assertThat(testPieceJointe.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testPieceJointe.getFormationId()).isEqualTo(UPDATED_FORMATION_ID);
    }

    @Test
    @Transactional
    void putNonExistingPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(count.incrementAndGet());

        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pieceJointeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(count.incrementAndGet());

        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(count.incrementAndGet());

        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePieceJointeWithPatch() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();

        // Update the pieceJointe using partial update
        PieceJointe partialUpdatedPieceJointe = new PieceJointe();
        partialUpdatedPieceJointe.setId(pieceJointe.getId());

        partialUpdatedPieceJointe
            .fichierURL(UPDATED_FICHIER_URL)
            .dateUpload(UPDATED_DATE_UPLOAD)
            .sessionId(UPDATED_SESSION_ID)
            .formationId(UPDATED_FORMATION_ID);

        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPieceJointe.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPieceJointe))
            )
            .andExpect(status().isOk());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
        PieceJointe testPieceJointe = pieceJointeList.get(pieceJointeList.size() - 1);
        assertThat(testPieceJointe.getNomFichier()).isEqualTo(DEFAULT_NOM_FICHIER);
        assertThat(testPieceJointe.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPieceJointe.getFichierURL()).isEqualTo(UPDATED_FICHIER_URL);
        assertThat(testPieceJointe.getDateUpload()).isEqualTo(UPDATED_DATE_UPLOAD);
        assertThat(testPieceJointe.getFormateurId()).isEqualTo(DEFAULT_FORMATEUR_ID);
        assertThat(testPieceJointe.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testPieceJointe.getFormationId()).isEqualTo(UPDATED_FORMATION_ID);
    }

    @Test
    @Transactional
    void fullUpdatePieceJointeWithPatch() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();

        // Update the pieceJointe using partial update
        PieceJointe partialUpdatedPieceJointe = new PieceJointe();
        partialUpdatedPieceJointe.setId(pieceJointe.getId());

        partialUpdatedPieceJointe
            .nomFichier(UPDATED_NOM_FICHIER)
            .type(UPDATED_TYPE)
            .fichierURL(UPDATED_FICHIER_URL)
            .dateUpload(UPDATED_DATE_UPLOAD)
            .formateurId(UPDATED_FORMATEUR_ID)
            .sessionId(UPDATED_SESSION_ID)
            .formationId(UPDATED_FORMATION_ID);

        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPieceJointe.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPieceJointe))
            )
            .andExpect(status().isOk());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
        PieceJointe testPieceJointe = pieceJointeList.get(pieceJointeList.size() - 1);
        assertThat(testPieceJointe.getNomFichier()).isEqualTo(UPDATED_NOM_FICHIER);
        assertThat(testPieceJointe.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPieceJointe.getFichierURL()).isEqualTo(UPDATED_FICHIER_URL);
        assertThat(testPieceJointe.getDateUpload()).isEqualTo(UPDATED_DATE_UPLOAD);
        assertThat(testPieceJointe.getFormateurId()).isEqualTo(UPDATED_FORMATEUR_ID);
        assertThat(testPieceJointe.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testPieceJointe.getFormationId()).isEqualTo(UPDATED_FORMATION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(count.incrementAndGet());

        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pieceJointeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(count.incrementAndGet());

        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(count.incrementAndGet());

        // Create the PieceJointe
        PieceJointeDTO pieceJointeDTO = pieceJointeMapper.toDto(pieceJointe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePieceJointe() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        int databaseSizeBeforeDelete = pieceJointeRepository.findAll().size();

        // Delete the pieceJointe
        restPieceJointeMockMvc
            .perform(delete(ENTITY_API_URL_ID, pieceJointe.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
