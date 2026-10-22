package com.orgacare.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.SessionFormationPersonne;
import com.orgacare.app.repository.SessionFormationPersonneRepository;
import com.orgacare.app.service.dto.SessionFormationPersonneDTO;
import com.orgacare.app.service.mapper.SessionFormationPersonneMapper;
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
 * Integration tests for the {@link SessionFormationPersonneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SessionFormationPersonneResourceIT {

    private static final Long DEFAULT_SESSION_ID = 1L;
    private static final Long UPDATED_SESSION_ID = 2L;

    private static final Long DEFAULT_PERSONNE_ID = 1L;
    private static final Long UPDATED_PERSONNE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/session-formation-personnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SessionFormationPersonneRepository sessionFormationPersonneRepository;

    @Autowired
    private SessionFormationPersonneMapper sessionFormationPersonneMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionFormationPersonneMockMvc;

    private SessionFormationPersonne sessionFormationPersonne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionFormationPersonne createEntity(EntityManager em) {
        SessionFormationPersonne sessionFormationPersonne = new SessionFormationPersonne()
            .sessionId(DEFAULT_SESSION_ID)
            .personneId(DEFAULT_PERSONNE_ID);
        return sessionFormationPersonne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionFormationPersonne createUpdatedEntity(EntityManager em) {
        SessionFormationPersonne sessionFormationPersonne = new SessionFormationPersonne()
            .sessionId(UPDATED_SESSION_ID)
            .personneId(UPDATED_PERSONNE_ID);
        return sessionFormationPersonne;
    }

    @BeforeEach
    public void initTest() {
        sessionFormationPersonne = createEntity(em);
    }

    @Test
    @Transactional
    void createSessionFormationPersonne() throws Exception {
        int databaseSizeBeforeCreate = sessionFormationPersonneRepository.findAll().size();
        // Create the SessionFormationPersonne
        SessionFormationPersonneDTO sessionFormationPersonneDTO = sessionFormationPersonneMapper.toDto(sessionFormationPersonne);
        restSessionFormationPersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationPersonneDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SessionFormationPersonne in the database
        List<SessionFormationPersonne> sessionFormationPersonneList = sessionFormationPersonneRepository.findAll();
        assertThat(sessionFormationPersonneList).hasSize(databaseSizeBeforeCreate + 1);
        SessionFormationPersonne testSessionFormationPersonne = sessionFormationPersonneList.get(sessionFormationPersonneList.size() - 1);
        assertThat(testSessionFormationPersonne.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
        assertThat(testSessionFormationPersonne.getPersonneId()).isEqualTo(DEFAULT_PERSONNE_ID);
    }

    @Test
    @Transactional
    void createSessionFormationPersonneWithExistingId() throws Exception {
        // Create the SessionFormationPersonne with an existing ID
        sessionFormationPersonne.setId(1L);
        SessionFormationPersonneDTO sessionFormationPersonneDTO = sessionFormationPersonneMapper.toDto(sessionFormationPersonne);

        int databaseSizeBeforeCreate = sessionFormationPersonneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionFormationPersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationPersonneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormationPersonne in the database
        List<SessionFormationPersonne> sessionFormationPersonneList = sessionFormationPersonneRepository.findAll();
        assertThat(sessionFormationPersonneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSessionFormationPersonnes() throws Exception {
        // Initialize the database
        sessionFormationPersonneRepository.saveAndFlush(sessionFormationPersonne);

        // Get all the sessionFormationPersonneList
        restSessionFormationPersonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionFormationPersonne.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionId").value(hasItem(DEFAULT_SESSION_ID.intValue())))
            .andExpect(jsonPath("$.[*].personneId").value(hasItem(DEFAULT_PERSONNE_ID.intValue())));
    }

    @Test
    @Transactional
    void getSessionFormationPersonne() throws Exception {
        // Initialize the database
        sessionFormationPersonneRepository.saveAndFlush(sessionFormationPersonne);

        // Get the sessionFormationPersonne
        restSessionFormationPersonneMockMvc
            .perform(get(ENTITY_API_URL_ID, sessionFormationPersonne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sessionFormationPersonne.getId().intValue()))
            .andExpect(jsonPath("$.sessionId").value(DEFAULT_SESSION_ID.intValue()))
            .andExpect(jsonPath("$.personneId").value(DEFAULT_PERSONNE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSessionFormationPersonne() throws Exception {
        // Get the sessionFormationPersonne
        restSessionFormationPersonneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSessionFormationPersonne() throws Exception {
        // Initialize the database
        sessionFormationPersonneRepository.saveAndFlush(sessionFormationPersonne);

        int databaseSizeBeforeUpdate = sessionFormationPersonneRepository.findAll().size();

        // Update the sessionFormationPersonne
        SessionFormationPersonne updatedSessionFormationPersonne = sessionFormationPersonneRepository
            .findById(sessionFormationPersonne.getId())
            .get();
        // Disconnect from session so that the updates on updatedSessionFormationPersonne are not directly saved in db
        em.detach(updatedSessionFormationPersonne);
        updatedSessionFormationPersonne.sessionId(UPDATED_SESSION_ID).personneId(UPDATED_PERSONNE_ID);
        SessionFormationPersonneDTO sessionFormationPersonneDTO = sessionFormationPersonneMapper.toDto(updatedSessionFormationPersonne);

        restSessionFormationPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionFormationPersonneDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationPersonneDTO))
            )
            .andExpect(status().isOk());

        // Validate the SessionFormationPersonne in the database
        List<SessionFormationPersonne> sessionFormationPersonneList = sessionFormationPersonneRepository.findAll();
        assertThat(sessionFormationPersonneList).hasSize(databaseSizeBeforeUpdate);
        SessionFormationPersonne testSessionFormationPersonne = sessionFormationPersonneList.get(sessionFormationPersonneList.size() - 1);
        assertThat(testSessionFormationPersonne.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testSessionFormationPersonne.getPersonneId()).isEqualTo(UPDATED_PERSONNE_ID);
    }

    @Test
    @Transactional
    void putNonExistingSessionFormationPersonne() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationPersonneRepository.findAll().size();
        sessionFormationPersonne.setId(count.incrementAndGet());

        // Create the SessionFormationPersonne
        SessionFormationPersonneDTO sessionFormationPersonneDTO = sessionFormationPersonneMapper.toDto(sessionFormationPersonne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionFormationPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionFormationPersonneDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationPersonneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormationPersonne in the database
        List<SessionFormationPersonne> sessionFormationPersonneList = sessionFormationPersonneRepository.findAll();
        assertThat(sessionFormationPersonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSessionFormationPersonne() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationPersonneRepository.findAll().size();
        sessionFormationPersonne.setId(count.incrementAndGet());

        // Create the SessionFormationPersonne
        SessionFormationPersonneDTO sessionFormationPersonneDTO = sessionFormationPersonneMapper.toDto(sessionFormationPersonne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionFormationPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationPersonneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormationPersonne in the database
        List<SessionFormationPersonne> sessionFormationPersonneList = sessionFormationPersonneRepository.findAll();
        assertThat(sessionFormationPersonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSessionFormationPersonne() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationPersonneRepository.findAll().size();
        sessionFormationPersonne.setId(count.incrementAndGet());

        // Create the SessionFormationPersonne
        SessionFormationPersonneDTO sessionFormationPersonneDTO = sessionFormationPersonneMapper.toDto(sessionFormationPersonne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionFormationPersonneMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationPersonneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionFormationPersonne in the database
        List<SessionFormationPersonne> sessionFormationPersonneList = sessionFormationPersonneRepository.findAll();
        assertThat(sessionFormationPersonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSessionFormationPersonneWithPatch() throws Exception {
        // Initialize the database
        sessionFormationPersonneRepository.saveAndFlush(sessionFormationPersonne);

        int databaseSizeBeforeUpdate = sessionFormationPersonneRepository.findAll().size();

        // Update the sessionFormationPersonne using partial update
        SessionFormationPersonne partialUpdatedSessionFormationPersonne = new SessionFormationPersonne();
        partialUpdatedSessionFormationPersonne.setId(sessionFormationPersonne.getId());

        partialUpdatedSessionFormationPersonne.personneId(UPDATED_PERSONNE_ID);

        restSessionFormationPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionFormationPersonne.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionFormationPersonne))
            )
            .andExpect(status().isOk());

        // Validate the SessionFormationPersonne in the database
        List<SessionFormationPersonne> sessionFormationPersonneList = sessionFormationPersonneRepository.findAll();
        assertThat(sessionFormationPersonneList).hasSize(databaseSizeBeforeUpdate);
        SessionFormationPersonne testSessionFormationPersonne = sessionFormationPersonneList.get(sessionFormationPersonneList.size() - 1);
        assertThat(testSessionFormationPersonne.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
        assertThat(testSessionFormationPersonne.getPersonneId()).isEqualTo(UPDATED_PERSONNE_ID);
    }

    @Test
    @Transactional
    void fullUpdateSessionFormationPersonneWithPatch() throws Exception {
        // Initialize the database
        sessionFormationPersonneRepository.saveAndFlush(sessionFormationPersonne);

        int databaseSizeBeforeUpdate = sessionFormationPersonneRepository.findAll().size();

        // Update the sessionFormationPersonne using partial update
        SessionFormationPersonne partialUpdatedSessionFormationPersonne = new SessionFormationPersonne();
        partialUpdatedSessionFormationPersonne.setId(sessionFormationPersonne.getId());

        partialUpdatedSessionFormationPersonne.sessionId(UPDATED_SESSION_ID).personneId(UPDATED_PERSONNE_ID);

        restSessionFormationPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionFormationPersonne.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionFormationPersonne))
            )
            .andExpect(status().isOk());

        // Validate the SessionFormationPersonne in the database
        List<SessionFormationPersonne> sessionFormationPersonneList = sessionFormationPersonneRepository.findAll();
        assertThat(sessionFormationPersonneList).hasSize(databaseSizeBeforeUpdate);
        SessionFormationPersonne testSessionFormationPersonne = sessionFormationPersonneList.get(sessionFormationPersonneList.size() - 1);
        assertThat(testSessionFormationPersonne.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testSessionFormationPersonne.getPersonneId()).isEqualTo(UPDATED_PERSONNE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSessionFormationPersonne() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationPersonneRepository.findAll().size();
        sessionFormationPersonne.setId(count.incrementAndGet());

        // Create the SessionFormationPersonne
        SessionFormationPersonneDTO sessionFormationPersonneDTO = sessionFormationPersonneMapper.toDto(sessionFormationPersonne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionFormationPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionFormationPersonneDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationPersonneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormationPersonne in the database
        List<SessionFormationPersonne> sessionFormationPersonneList = sessionFormationPersonneRepository.findAll();
        assertThat(sessionFormationPersonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSessionFormationPersonne() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationPersonneRepository.findAll().size();
        sessionFormationPersonne.setId(count.incrementAndGet());

        // Create the SessionFormationPersonne
        SessionFormationPersonneDTO sessionFormationPersonneDTO = sessionFormationPersonneMapper.toDto(sessionFormationPersonne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionFormationPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationPersonneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionFormationPersonne in the database
        List<SessionFormationPersonne> sessionFormationPersonneList = sessionFormationPersonneRepository.findAll();
        assertThat(sessionFormationPersonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSessionFormationPersonne() throws Exception {
        int databaseSizeBeforeUpdate = sessionFormationPersonneRepository.findAll().size();
        sessionFormationPersonne.setId(count.incrementAndGet());

        // Create the SessionFormationPersonne
        SessionFormationPersonneDTO sessionFormationPersonneDTO = sessionFormationPersonneMapper.toDto(sessionFormationPersonne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionFormationPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionFormationPersonneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionFormationPersonne in the database
        List<SessionFormationPersonne> sessionFormationPersonneList = sessionFormationPersonneRepository.findAll();
        assertThat(sessionFormationPersonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSessionFormationPersonne() throws Exception {
        // Initialize the database
        sessionFormationPersonneRepository.saveAndFlush(sessionFormationPersonne);

        int databaseSizeBeforeDelete = sessionFormationPersonneRepository.findAll().size();

        // Delete the sessionFormationPersonne
        restSessionFormationPersonneMockMvc
            .perform(delete(ENTITY_API_URL_ID, sessionFormationPersonne.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SessionFormationPersonne> sessionFormationPersonneList = sessionFormationPersonneRepository.findAll();
        assertThat(sessionFormationPersonneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
