package com.orgacare.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.SessionFormationChefProjet;
import com.orgacare.app.repository.SessionFormationChefProjetRepository;
import com.orgacare.app.service.dto.SessionFormationChefProjetDTO;
import com.orgacare.app.service.mapper.SessionFormationChefProjetMapper;
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
 * Integration tests for the {@link SessionFormationChefProjetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SessionFormationChefProjetResourceIT {

    private static final Long DEFAULT_SESSION_ID = 1L;
    private static final Long UPDATED_SESSION_ID = 2L;

    private static final Long DEFAULT_CHEF_PROJET_ID = 1L;
    private static final Long UPDATED_CHEF_PROJET_ID = 2L;

    private static final String ENTITY_API_URL = "/api/session-formation-chef-projets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SessionFormationChefProjetRepository sessionFormationChefProjetRepository;

    @Autowired
    private SessionFormationChefProjetMapper sessionFormationChefProjetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionFormationChefProjetMockMvc;

    private SessionFormationChefProjet sessionFormationChefProjet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionFormationChefProjet createEntity(EntityManager em) {
        SessionFormationChefProjet sessionFormationChefProjet = new SessionFormationChefProjet()
            .sessionId(DEFAULT_SESSION_ID)
            .chefProjetId(DEFAULT_CHEF_PROJET_ID);
        return sessionFormationChefProjet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionFormationChefProjet createUpdatedEntity(EntityManager em) {
        SessionFormationChefProjet sessionFormationChefProjet = new SessionFormationChefProjet()
            .sessionId(UPDATED_SESSION_ID)
            .chefProjetId(UPDATED_CHEF_PROJET_ID);
        return sessionFormationChefProjet;
    }

    @BeforeEach
    public void initTest() {
        sessionFormationChefProjet = createEntity(em);
    }

    @Test
    @Transactional
    void createSessionFormationChefProjet() throws Exception {
        int databaseSizeBeforeCreate = sessionFormationChefProjetRepository.findAll().size();
        // Create the SessionFormationChefProjet
        SessionFormationChefProjetDTO sessionFormationChefProjetDTO = sessionFormationChefProjetMapper.toDto(sessionFormationChefProjet);
        restSessionFormationChefProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationChefProjetDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SessionFormationChefProjet in the database
        List<SessionFormationChefProjet> sessionFormationChefProjetList = sessionFormationChefProjetRepository.findAll();
        assertThat(sessionFormationChefProjetList).hasSize(databaseSizeBeforeCreate + 1);
        SessionFormationChefProjet testSessionFormationChefProjet = sessionFormationChefProjetList.get(
            sessionFormationChefProjetList.size() - 1
        );
        assertThat(testSessionFormationChefProjet.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
        assertThat(testSessionFormationChefProjet.getChefProjetId()).isEqualTo(DEFAULT_CHEF_PROJET_ID);
    }

    @Test
    @Transactional
    void createSessionFormationChefProjetWithExistingId() throws Exception {
        // Create the SessionFormationChefProjet with an existing ID
        sessionFormationChefProjet.setId(1L);
        SessionFormationChefProjetDTO sessionFormationChefProjetDTO = sessionFormationChefProjetMapper.toDto(sessionFormationChefProjet);

        int databaseSizeBeforeCreate = sessionFormationChefProjetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionFormationChefProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationChefProjetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormationChefProjet in the database
        List<SessionFormationChefProjet> sessionFormationChefProjetList = sessionFormationChefProjetRepository.findAll();
        assertThat(sessionFormationChefProjetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSessionFormationChefProjets() throws Exception {
        // Initialize the database
        sessionFormationChefProjetRepository.saveAndFlush(sessionFormationChefProjet);

        // Get all the sessionFormationChefProjetList
        restSessionFormationChefProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionFormationChefProjet.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionId").value(hasItem(DEFAULT_SESSION_ID.intValue())))
            .andExpect(jsonPath("$.[*].chefProjetId").value(hasItem(DEFAULT_CHEF_PROJET_ID.intValue())));
    }

    @Test
    @Transactional
    void getSessionFormationChefProjet() throws Exception {
        // Initialize the database
        sessionFormationChefProjetRepository.saveAndFlush(sessionFormationChefProjet);

        // Get the sessionFormationChefProjet
        restSessionFormationChefProjetMockMvc
            .perform(get(ENTITY_API_URL_ID, sessionFormationChefProjet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sessionFormationChefProjet.getId().intValue()))
            .andExpect(jsonPath("$.sessionId").value(DEFAULT_SESSION_ID.intValue()))
            .andExpect(jsonPath("$.chefProjetId").value(DEFAULT_CHEF_PROJET_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSessionFormationChefProjet() throws Exception {
        // Get the sessionFormationChefProjet
        restSessionFormationChefProjetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSessionFormationChefProjet() throws Exception {
        // Initialize the database
        sessionFormationChefProjetRepository.saveAndFlush(sessionFormationChefProjet);

        int databaseSizeBeforeUpdate = sessionFormationChefProjetRepository.findAll().size();

        // Update the sessionFormationChefProjet
        SessionFormationChefProjet updatedSessionFormationChefProjet = sessionFormationChefProjetRepository
            .findById(sessionFormationChefProjet.getId())
            .get();
        // Disconnect from session so that the updates on updatedSessionFormationChefProjet are not directly saved in db
        em.detach(updatedSessionFormationChefProjet);
        updatedSessionFormationChefProjet.sessionId(UPDATED_SESSION_ID).chefProjetId(UPDATED_CHEF_PROJET_ID);
        SessionFormationChefProjetDTO sessionFormationChefProjetDTO = sessionFormationChefProjetMapper.toDto(
            updatedSessionFormationChefProjet
        );

        restSessionFormationChefProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionFormationChefProjetDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationChefProjetDTO))
            )
            .andExpect(status().isOk());

        // Validate the SessionFormationChefProjet in the database
        List<SessionFormationChefProjet> sessionFormationChefProjetList = sessionFormationChefProjetRepository.findAll();
        assertThat(sessionFormationChefProjetList).hasSize(databaseSizeBeforeUpdate);
        SessionFormationChefProjet testSessionFormationChefProjet = sessionFormationChefProjetList.get(
            sessionFormationChefProjetList.size() - 1
        );
        assertThat(testSessionFormationChefProjet.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testSessionFormationChefProjet.getChefProjetId()).isEqualTo(UPDATED_CHEF_PROJET_ID);
    }

    @Test
    @Transactional
    void putNonExistingSessionFormationChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationChefProjetRepository.findAll().size();
        sessionFormationChefProjet.setId(count.incrementAndGet());

        // Create the SessionFormationChefProjet
        SessionFormationChefProjetDTO sessionFormationChefProjetDTO = sessionFormationChefProjetMapper.toDto(sessionFormationChefProjet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionFormationChefProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionFormationChefProjetDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationChefProjetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormationChefProjet in the database
        List<SessionFormationChefProjet> sessionFormationChefProjetList = sessionFormationChefProjetRepository.findAll();
        assertThat(sessionFormationChefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSessionFormationChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationChefProjetRepository.findAll().size();
        sessionFormationChefProjet.setId(count.incrementAndGet());

        // Create the SessionFormationChefProjet
        SessionFormationChefProjetDTO sessionFormationChefProjetDTO = sessionFormationChefProjetMapper.toDto(sessionFormationChefProjet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionFormationChefProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationChefProjetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormationChefProjet in the database
        List<SessionFormationChefProjet> sessionFormationChefProjetList = sessionFormationChefProjetRepository.findAll();
        assertThat(sessionFormationChefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSessionFormationChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationChefProjetRepository.findAll().size();
        sessionFormationChefProjet.setId(count.incrementAndGet());

        // Create the SessionFormationChefProjet
        SessionFormationChefProjetDTO sessionFormationChefProjetDTO = sessionFormationChefProjetMapper.toDto(sessionFormationChefProjet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionFormationChefProjetMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationChefProjetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionFormationChefProjet in the database
        List<SessionFormationChefProjet> sessionFormationChefProjetList = sessionFormationChefProjetRepository.findAll();
        assertThat(sessionFormationChefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSessionFormationChefProjetWithPatch() throws Exception {
        // Initialize the database
        sessionFormationChefProjetRepository.saveAndFlush(sessionFormationChefProjet);

        int databaseSizeBeforeUpdate = sessionFormationChefProjetRepository.findAll().size();

        // Update the sessionFormationChefProjet using partial update
        SessionFormationChefProjet partialUpdatedSessionFormationChefProjet = new SessionFormationChefProjet();
        partialUpdatedSessionFormationChefProjet.setId(sessionFormationChefProjet.getId());

        restSessionFormationChefProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionFormationChefProjet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionFormationChefProjet))
            )
            .andExpect(status().isOk());

        // Validate the SessionFormationChefProjet in the database
        List<SessionFormationChefProjet> sessionFormationChefProjetList = sessionFormationChefProjetRepository.findAll();
        assertThat(sessionFormationChefProjetList).hasSize(databaseSizeBeforeUpdate);
        SessionFormationChefProjet testSessionFormationChefProjet = sessionFormationChefProjetList.get(
            sessionFormationChefProjetList.size() - 1
        );
        assertThat(testSessionFormationChefProjet.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
        assertThat(testSessionFormationChefProjet.getChefProjetId()).isEqualTo(DEFAULT_CHEF_PROJET_ID);
    }

    @Test
    @Transactional
    void fullUpdateSessionFormationChefProjetWithPatch() throws Exception {
        // Initialize the database
        sessionFormationChefProjetRepository.saveAndFlush(sessionFormationChefProjet);

        int databaseSizeBeforeUpdate = sessionFormationChefProjetRepository.findAll().size();

        // Update the sessionFormationChefProjet using partial update
        SessionFormationChefProjet partialUpdatedSessionFormationChefProjet = new SessionFormationChefProjet();
        partialUpdatedSessionFormationChefProjet.setId(sessionFormationChefProjet.getId());

        partialUpdatedSessionFormationChefProjet.sessionId(UPDATED_SESSION_ID).chefProjetId(UPDATED_CHEF_PROJET_ID);

        restSessionFormationChefProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionFormationChefProjet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionFormationChefProjet))
            )
            .andExpect(status().isOk());

        // Validate the SessionFormationChefProjet in the database
        List<SessionFormationChefProjet> sessionFormationChefProjetList = sessionFormationChefProjetRepository.findAll();
        assertThat(sessionFormationChefProjetList).hasSize(databaseSizeBeforeUpdate);
        SessionFormationChefProjet testSessionFormationChefProjet = sessionFormationChefProjetList.get(
            sessionFormationChefProjetList.size() - 1
        );
        assertThat(testSessionFormationChefProjet.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testSessionFormationChefProjet.getChefProjetId()).isEqualTo(UPDATED_CHEF_PROJET_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSessionFormationChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationChefProjetRepository.findAll().size();
        sessionFormationChefProjet.setId(count.incrementAndGet());

        // Create the SessionFormationChefProjet
        SessionFormationChefProjetDTO sessionFormationChefProjetDTO = sessionFormationChefProjetMapper.toDto(sessionFormationChefProjet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionFormationChefProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionFormationChefProjetDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationChefProjetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormationChefProjet in the database
        List<SessionFormationChefProjet> sessionFormationChefProjetList = sessionFormationChefProjetRepository.findAll();
        assertThat(sessionFormationChefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSessionFormationChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationChefProjetRepository.findAll().size();
        sessionFormationChefProjet.setId(count.incrementAndGet());

        // Create the SessionFormationChefProjet
        SessionFormationChefProjetDTO sessionFormationChefProjetDTO = sessionFormationChefProjetMapper.toDto(sessionFormationChefProjet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionFormationChefProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationChefProjetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormationChefProjet in the database
        List<SessionFormationChefProjet> sessionFormationChefProjetList = sessionFormationChefProjetRepository.findAll();
        assertThat(sessionFormationChefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSessionFormationChefProjet() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationChefProjetRepository.findAll().size();
        sessionFormationChefProjet.setId(count.incrementAndGet());

        // Create the SessionFormationChefProjet
        SessionFormationChefProjetDTO sessionFormationChefProjetDTO = sessionFormationChefProjetMapper.toDto(sessionFormationChefProjet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionFormationChefProjetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationChefProjetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionFormationChefProjet in the database
        List<SessionFormationChefProjet> sessionFormationChefProjetList = sessionFormationChefProjetRepository.findAll();
        assertThat(sessionFormationChefProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSessionFormationChefProjet() throws Exception {
        // Initialize the database
        sessionFormationChefProjetRepository.saveAndFlush(sessionFormationChefProjet);

        int databaseSizeBeforeDelete = sessionFormationChefProjetRepository.findAll().size();

        // Delete the sessionFormationChefProjet
        restSessionFormationChefProjetMockMvc
            .perform(delete(ENTITY_API_URL_ID, sessionFormationChefProjet.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SessionFormationChefProjet> sessionFormationChefProjetList = sessionFormationChefProjetRepository.findAll();
        assertThat(sessionFormationChefProjetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
