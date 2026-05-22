package com.orgacare.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.EmployeCreatedEvent;
import com.orgacare.app.repository.EmployeCreatedEventRepository;
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
 * Integration tests for the {@link EmployeCreatedEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeCreatedEventResourceIT {

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/employe-created-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeCreatedEventRepository employeCreatedEventRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeCreatedEventMockMvc;

    private EmployeCreatedEvent employeCreatedEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeCreatedEvent createEntity(EntityManager em) {
        EmployeCreatedEvent employeCreatedEvent = new EmployeCreatedEvent()
            .matricule(DEFAULT_MATRICULE)
            .nomPrenom(DEFAULT_NOM_PRENOM)
            .email(DEFAULT_EMAIL)
            .userId(DEFAULT_USER_ID);
        return employeCreatedEvent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeCreatedEvent createUpdatedEntity(EntityManager em) {
        EmployeCreatedEvent employeCreatedEvent = new EmployeCreatedEvent()
            .matricule(UPDATED_MATRICULE)
            .nomPrenom(UPDATED_NOM_PRENOM)
            .email(UPDATED_EMAIL)
            .userId(UPDATED_USER_ID);
        return employeCreatedEvent;
    }

    @BeforeEach
    public void initTest() {
        employeCreatedEvent = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeCreatedEvent() throws Exception {
        int databaseSizeBeforeCreate = employeCreatedEventRepository.findAll().size();
        // Create the EmployeCreatedEvent
        restEmployeCreatedEventMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeCreatedEvent))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeCreatedEvent in the database
        List<EmployeCreatedEvent> employeCreatedEventList = employeCreatedEventRepository.findAll();
        assertThat(employeCreatedEventList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeCreatedEvent testEmployeCreatedEvent = employeCreatedEventList.get(employeCreatedEventList.size() - 1);
        assertThat(testEmployeCreatedEvent.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testEmployeCreatedEvent.getNomPrenom()).isEqualTo(DEFAULT_NOM_PRENOM);
        assertThat(testEmployeCreatedEvent.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployeCreatedEvent.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createEmployeCreatedEventWithExistingId() throws Exception {
        // Create the EmployeCreatedEvent with an existing ID
        employeCreatedEvent.setId(1L);

        int databaseSizeBeforeCreate = employeCreatedEventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeCreatedEventMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeCreatedEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeCreatedEvent in the database
        List<EmployeCreatedEvent> employeCreatedEventList = employeCreatedEventRepository.findAll();
        assertThat(employeCreatedEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployeCreatedEvents() throws Exception {
        // Initialize the database
        employeCreatedEventRepository.saveAndFlush(employeCreatedEvent);

        // Get all the employeCreatedEventList
        restEmployeCreatedEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeCreatedEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].nomPrenom").value(hasItem(DEFAULT_NOM_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getEmployeCreatedEvent() throws Exception {
        // Initialize the database
        employeCreatedEventRepository.saveAndFlush(employeCreatedEvent);

        // Get the employeCreatedEvent
        restEmployeCreatedEventMockMvc
            .perform(get(ENTITY_API_URL_ID, employeCreatedEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeCreatedEvent.getId().intValue()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
            .andExpect(jsonPath("$.nomPrenom").value(DEFAULT_NOM_PRENOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEmployeCreatedEvent() throws Exception {
        // Get the employeCreatedEvent
        restEmployeCreatedEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmployeCreatedEvent() throws Exception {
        // Initialize the database
        employeCreatedEventRepository.saveAndFlush(employeCreatedEvent);

        int databaseSizeBeforeUpdate = employeCreatedEventRepository.findAll().size();

        // Update the employeCreatedEvent
        EmployeCreatedEvent updatedEmployeCreatedEvent = employeCreatedEventRepository.findById(employeCreatedEvent.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeCreatedEvent are not directly saved in db
        em.detach(updatedEmployeCreatedEvent);
        updatedEmployeCreatedEvent.matricule(UPDATED_MATRICULE).nomPrenom(UPDATED_NOM_PRENOM).email(UPDATED_EMAIL).userId(UPDATED_USER_ID);

        restEmployeCreatedEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeCreatedEvent.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmployeCreatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the EmployeCreatedEvent in the database
        List<EmployeCreatedEvent> employeCreatedEventList = employeCreatedEventRepository.findAll();
        assertThat(employeCreatedEventList).hasSize(databaseSizeBeforeUpdate);
        EmployeCreatedEvent testEmployeCreatedEvent = employeCreatedEventList.get(employeCreatedEventList.size() - 1);
        assertThat(testEmployeCreatedEvent.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testEmployeCreatedEvent.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
        assertThat(testEmployeCreatedEvent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployeCreatedEvent.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingEmployeCreatedEvent() throws Exception {
        int databaseSizeBeforeUpdate = employeCreatedEventRepository.findAll().size();
        employeCreatedEvent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeCreatedEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeCreatedEvent.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeCreatedEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeCreatedEvent in the database
        List<EmployeCreatedEvent> employeCreatedEventList = employeCreatedEventRepository.findAll();
        assertThat(employeCreatedEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeCreatedEvent() throws Exception {
        int databaseSizeBeforeUpdate = employeCreatedEventRepository.findAll().size();
        employeCreatedEvent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeCreatedEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeCreatedEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeCreatedEvent in the database
        List<EmployeCreatedEvent> employeCreatedEventList = employeCreatedEventRepository.findAll();
        assertThat(employeCreatedEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeCreatedEvent() throws Exception {
        int databaseSizeBeforeUpdate = employeCreatedEventRepository.findAll().size();
        employeCreatedEvent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeCreatedEventMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeCreatedEvent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeCreatedEvent in the database
        List<EmployeCreatedEvent> employeCreatedEventList = employeCreatedEventRepository.findAll();
        assertThat(employeCreatedEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeCreatedEventWithPatch() throws Exception {
        // Initialize the database
        employeCreatedEventRepository.saveAndFlush(employeCreatedEvent);

        int databaseSizeBeforeUpdate = employeCreatedEventRepository.findAll().size();

        // Update the employeCreatedEvent using partial update
        EmployeCreatedEvent partialUpdatedEmployeCreatedEvent = new EmployeCreatedEvent();
        partialUpdatedEmployeCreatedEvent.setId(employeCreatedEvent.getId());

        partialUpdatedEmployeCreatedEvent.nomPrenom(UPDATED_NOM_PRENOM).email(UPDATED_EMAIL);

        restEmployeCreatedEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeCreatedEvent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeCreatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the EmployeCreatedEvent in the database
        List<EmployeCreatedEvent> employeCreatedEventList = employeCreatedEventRepository.findAll();
        assertThat(employeCreatedEventList).hasSize(databaseSizeBeforeUpdate);
        EmployeCreatedEvent testEmployeCreatedEvent = employeCreatedEventList.get(employeCreatedEventList.size() - 1);
        assertThat(testEmployeCreatedEvent.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testEmployeCreatedEvent.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
        assertThat(testEmployeCreatedEvent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployeCreatedEvent.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateEmployeCreatedEventWithPatch() throws Exception {
        // Initialize the database
        employeCreatedEventRepository.saveAndFlush(employeCreatedEvent);

        int databaseSizeBeforeUpdate = employeCreatedEventRepository.findAll().size();

        // Update the employeCreatedEvent using partial update
        EmployeCreatedEvent partialUpdatedEmployeCreatedEvent = new EmployeCreatedEvent();
        partialUpdatedEmployeCreatedEvent.setId(employeCreatedEvent.getId());

        partialUpdatedEmployeCreatedEvent
            .matricule(UPDATED_MATRICULE)
            .nomPrenom(UPDATED_NOM_PRENOM)
            .email(UPDATED_EMAIL)
            .userId(UPDATED_USER_ID);

        restEmployeCreatedEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeCreatedEvent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeCreatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the EmployeCreatedEvent in the database
        List<EmployeCreatedEvent> employeCreatedEventList = employeCreatedEventRepository.findAll();
        assertThat(employeCreatedEventList).hasSize(databaseSizeBeforeUpdate);
        EmployeCreatedEvent testEmployeCreatedEvent = employeCreatedEventList.get(employeCreatedEventList.size() - 1);
        assertThat(testEmployeCreatedEvent.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testEmployeCreatedEvent.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
        assertThat(testEmployeCreatedEvent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployeCreatedEvent.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeCreatedEvent() throws Exception {
        int databaseSizeBeforeUpdate = employeCreatedEventRepository.findAll().size();
        employeCreatedEvent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeCreatedEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeCreatedEvent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeCreatedEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeCreatedEvent in the database
        List<EmployeCreatedEvent> employeCreatedEventList = employeCreatedEventRepository.findAll();
        assertThat(employeCreatedEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeCreatedEvent() throws Exception {
        int databaseSizeBeforeUpdate = employeCreatedEventRepository.findAll().size();
        employeCreatedEvent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeCreatedEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeCreatedEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeCreatedEvent in the database
        List<EmployeCreatedEvent> employeCreatedEventList = employeCreatedEventRepository.findAll();
        assertThat(employeCreatedEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeCreatedEvent() throws Exception {
        int databaseSizeBeforeUpdate = employeCreatedEventRepository.findAll().size();
        employeCreatedEvent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeCreatedEventMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeCreatedEvent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeCreatedEvent in the database
        List<EmployeCreatedEvent> employeCreatedEventList = employeCreatedEventRepository.findAll();
        assertThat(employeCreatedEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeCreatedEvent() throws Exception {
        // Initialize the database
        employeCreatedEventRepository.saveAndFlush(employeCreatedEvent);

        int databaseSizeBeforeDelete = employeCreatedEventRepository.findAll().size();

        // Delete the employeCreatedEvent
        restEmployeCreatedEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeCreatedEvent.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeCreatedEvent> employeCreatedEventList = employeCreatedEventRepository.findAll();
        assertThat(employeCreatedEventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
