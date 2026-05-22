package com.orgacare.app.web.rest;

import static com.orgacare.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.StatusHistory;
import com.orgacare.app.repository.StatusHistoryRepository;
import com.orgacare.app.service.dto.StatusHistoryDTO;
import com.orgacare.app.service.mapper.StatusHistoryMapper;
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
 * Integration tests for the {@link StatusHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StatusHistoryResourceIT {

    private static final ZonedDateTime DEFAULT_DATE_TRANSACTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TRANSACTION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LOGIN_USER = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN_USER = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_OBJECT = "AAAAAAAAAA";
    private static final String UPDATED_DATA_OBJECT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/status-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StatusHistoryRepository statusHistoryRepository;

    @Autowired
    private StatusHistoryMapper statusHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatusHistoryMockMvc;

    private StatusHistory statusHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusHistory createEntity(EntityManager em) {
        StatusHistory statusHistory = new StatusHistory()
            .dateTransaction(DEFAULT_DATE_TRANSACTION)
            .dateFin(DEFAULT_DATE_FIN)
            .loginUser(DEFAULT_LOGIN_USER)
            .transaction(DEFAULT_TRANSACTION)
            .transactionReference(DEFAULT_TRANSACTION_REFERENCE)
            .dataObject(DEFAULT_DATA_OBJECT);
        return statusHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusHistory createUpdatedEntity(EntityManager em) {
        StatusHistory statusHistory = new StatusHistory()
            .dateTransaction(UPDATED_DATE_TRANSACTION)
            .dateFin(UPDATED_DATE_FIN)
            .loginUser(UPDATED_LOGIN_USER)
            .transaction(UPDATED_TRANSACTION)
            .transactionReference(UPDATED_TRANSACTION_REFERENCE)
            .dataObject(UPDATED_DATA_OBJECT);
        return statusHistory;
    }

    @BeforeEach
    public void initTest() {
        statusHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createStatusHistory() throws Exception {
        int databaseSizeBeforeCreate = statusHistoryRepository.findAll().size();
        // Create the StatusHistory
        StatusHistoryDTO statusHistoryDTO = statusHistoryMapper.toDto(statusHistory);
        restStatusHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistoryList = statusHistoryRepository.findAll();
        assertThat(statusHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        StatusHistory testStatusHistory = statusHistoryList.get(statusHistoryList.size() - 1);
        assertThat(testStatusHistory.getDateTransaction()).isEqualTo(DEFAULT_DATE_TRANSACTION);
        assertThat(testStatusHistory.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testStatusHistory.getLoginUser()).isEqualTo(DEFAULT_LOGIN_USER);
        assertThat(testStatusHistory.getTransaction()).isEqualTo(DEFAULT_TRANSACTION);
        assertThat(testStatusHistory.getTransactionReference()).isEqualTo(DEFAULT_TRANSACTION_REFERENCE);
        assertThat(testStatusHistory.getDataObject()).isEqualTo(DEFAULT_DATA_OBJECT);
    }

    @Test
    @Transactional
    void createStatusHistoryWithExistingId() throws Exception {
        // Create the StatusHistory with an existing ID
        statusHistory.setId(1L);
        StatusHistoryDTO statusHistoryDTO = statusHistoryMapper.toDto(statusHistory);

        int databaseSizeBeforeCreate = statusHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatusHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistoryList = statusHistoryRepository.findAll();
        assertThat(statusHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStatusHistories() throws Exception {
        // Initialize the database
        statusHistoryRepository.saveAndFlush(statusHistory);

        // Get all the statusHistoryList
        restStatusHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTransaction").value(hasItem(sameInstant(DEFAULT_DATE_TRANSACTION))))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(sameInstant(DEFAULT_DATE_FIN))))
            .andExpect(jsonPath("$.[*].loginUser").value(hasItem(DEFAULT_LOGIN_USER)))
            .andExpect(jsonPath("$.[*].transaction").value(hasItem(DEFAULT_TRANSACTION)))
            .andExpect(jsonPath("$.[*].transactionReference").value(hasItem(DEFAULT_TRANSACTION_REFERENCE)))
            .andExpect(jsonPath("$.[*].dataObject").value(hasItem(DEFAULT_DATA_OBJECT)));
    }

    @Test
    @Transactional
    void getStatusHistory() throws Exception {
        // Initialize the database
        statusHistoryRepository.saveAndFlush(statusHistory);

        // Get the statusHistory
        restStatusHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, statusHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statusHistory.getId().intValue()))
            .andExpect(jsonPath("$.dateTransaction").value(sameInstant(DEFAULT_DATE_TRANSACTION)))
            .andExpect(jsonPath("$.dateFin").value(sameInstant(DEFAULT_DATE_FIN)))
            .andExpect(jsonPath("$.loginUser").value(DEFAULT_LOGIN_USER))
            .andExpect(jsonPath("$.transaction").value(DEFAULT_TRANSACTION))
            .andExpect(jsonPath("$.transactionReference").value(DEFAULT_TRANSACTION_REFERENCE))
            .andExpect(jsonPath("$.dataObject").value(DEFAULT_DATA_OBJECT));
    }

    @Test
    @Transactional
    void getNonExistingStatusHistory() throws Exception {
        // Get the statusHistory
        restStatusHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStatusHistory() throws Exception {
        // Initialize the database
        statusHistoryRepository.saveAndFlush(statusHistory);

        int databaseSizeBeforeUpdate = statusHistoryRepository.findAll().size();

        // Update the statusHistory
        StatusHistory updatedStatusHistory = statusHistoryRepository.findById(statusHistory.getId()).get();
        // Disconnect from session so that the updates on updatedStatusHistory are not directly saved in db
        em.detach(updatedStatusHistory);
        updatedStatusHistory
            .dateTransaction(UPDATED_DATE_TRANSACTION)
            .dateFin(UPDATED_DATE_FIN)
            .loginUser(UPDATED_LOGIN_USER)
            .transaction(UPDATED_TRANSACTION)
            .transactionReference(UPDATED_TRANSACTION_REFERENCE)
            .dataObject(UPDATED_DATA_OBJECT);
        StatusHistoryDTO statusHistoryDTO = statusHistoryMapper.toDto(updatedStatusHistory);

        restStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statusHistoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistoryList = statusHistoryRepository.findAll();
        assertThat(statusHistoryList).hasSize(databaseSizeBeforeUpdate);
        StatusHistory testStatusHistory = statusHistoryList.get(statusHistoryList.size() - 1);
        assertThat(testStatusHistory.getDateTransaction()).isEqualTo(UPDATED_DATE_TRANSACTION);
        assertThat(testStatusHistory.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testStatusHistory.getLoginUser()).isEqualTo(UPDATED_LOGIN_USER);
        assertThat(testStatusHistory.getTransaction()).isEqualTo(UPDATED_TRANSACTION);
        assertThat(testStatusHistory.getTransactionReference()).isEqualTo(UPDATED_TRANSACTION_REFERENCE);
        assertThat(testStatusHistory.getDataObject()).isEqualTo(UPDATED_DATA_OBJECT);
    }

    @Test
    @Transactional
    void putNonExistingStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = statusHistoryRepository.findAll().size();
        statusHistory.setId(count.incrementAndGet());

        // Create the StatusHistory
        StatusHistoryDTO statusHistoryDTO = statusHistoryMapper.toDto(statusHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statusHistoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistoryList = statusHistoryRepository.findAll();
        assertThat(statusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = statusHistoryRepository.findAll().size();
        statusHistory.setId(count.incrementAndGet());

        // Create the StatusHistory
        StatusHistoryDTO statusHistoryDTO = statusHistoryMapper.toDto(statusHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistoryList = statusHistoryRepository.findAll();
        assertThat(statusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = statusHistoryRepository.findAll().size();
        statusHistory.setId(count.incrementAndGet());

        // Create the StatusHistory
        StatusHistoryDTO statusHistoryDTO = statusHistoryMapper.toDto(statusHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistoryList = statusHistoryRepository.findAll();
        assertThat(statusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatusHistoryWithPatch() throws Exception {
        // Initialize the database
        statusHistoryRepository.saveAndFlush(statusHistory);

        int databaseSizeBeforeUpdate = statusHistoryRepository.findAll().size();

        // Update the statusHistory using partial update
        StatusHistory partialUpdatedStatusHistory = new StatusHistory();
        partialUpdatedStatusHistory.setId(statusHistory.getId());

        partialUpdatedStatusHistory
            .dateFin(UPDATED_DATE_FIN)
            .loginUser(UPDATED_LOGIN_USER)
            .transactionReference(UPDATED_TRANSACTION_REFERENCE)
            .dataObject(UPDATED_DATA_OBJECT);

        restStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatusHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStatusHistory))
            )
            .andExpect(status().isOk());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistoryList = statusHistoryRepository.findAll();
        assertThat(statusHistoryList).hasSize(databaseSizeBeforeUpdate);
        StatusHistory testStatusHistory = statusHistoryList.get(statusHistoryList.size() - 1);
        assertThat(testStatusHistory.getDateTransaction()).isEqualTo(DEFAULT_DATE_TRANSACTION);
        assertThat(testStatusHistory.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testStatusHistory.getLoginUser()).isEqualTo(UPDATED_LOGIN_USER);
        assertThat(testStatusHistory.getTransaction()).isEqualTo(DEFAULT_TRANSACTION);
        assertThat(testStatusHistory.getTransactionReference()).isEqualTo(UPDATED_TRANSACTION_REFERENCE);
        assertThat(testStatusHistory.getDataObject()).isEqualTo(UPDATED_DATA_OBJECT);
    }

    @Test
    @Transactional
    void fullUpdateStatusHistoryWithPatch() throws Exception {
        // Initialize the database
        statusHistoryRepository.saveAndFlush(statusHistory);

        int databaseSizeBeforeUpdate = statusHistoryRepository.findAll().size();

        // Update the statusHistory using partial update
        StatusHistory partialUpdatedStatusHistory = new StatusHistory();
        partialUpdatedStatusHistory.setId(statusHistory.getId());

        partialUpdatedStatusHistory
            .dateTransaction(UPDATED_DATE_TRANSACTION)
            .dateFin(UPDATED_DATE_FIN)
            .loginUser(UPDATED_LOGIN_USER)
            .transaction(UPDATED_TRANSACTION)
            .transactionReference(UPDATED_TRANSACTION_REFERENCE)
            .dataObject(UPDATED_DATA_OBJECT);

        restStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatusHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStatusHistory))
            )
            .andExpect(status().isOk());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistoryList = statusHistoryRepository.findAll();
        assertThat(statusHistoryList).hasSize(databaseSizeBeforeUpdate);
        StatusHistory testStatusHistory = statusHistoryList.get(statusHistoryList.size() - 1);
        assertThat(testStatusHistory.getDateTransaction()).isEqualTo(UPDATED_DATE_TRANSACTION);
        assertThat(testStatusHistory.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testStatusHistory.getLoginUser()).isEqualTo(UPDATED_LOGIN_USER);
        assertThat(testStatusHistory.getTransaction()).isEqualTo(UPDATED_TRANSACTION);
        assertThat(testStatusHistory.getTransactionReference()).isEqualTo(UPDATED_TRANSACTION_REFERENCE);
        assertThat(testStatusHistory.getDataObject()).isEqualTo(UPDATED_DATA_OBJECT);
    }

    @Test
    @Transactional
    void patchNonExistingStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = statusHistoryRepository.findAll().size();
        statusHistory.setId(count.incrementAndGet());

        // Create the StatusHistory
        StatusHistoryDTO statusHistoryDTO = statusHistoryMapper.toDto(statusHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, statusHistoryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistoryList = statusHistoryRepository.findAll();
        assertThat(statusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = statusHistoryRepository.findAll().size();
        statusHistory.setId(count.incrementAndGet());

        // Create the StatusHistory
        StatusHistoryDTO statusHistoryDTO = statusHistoryMapper.toDto(statusHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistoryList = statusHistoryRepository.findAll();
        assertThat(statusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = statusHistoryRepository.findAll().size();
        statusHistory.setId(count.incrementAndGet());

        // Create the StatusHistory
        StatusHistoryDTO statusHistoryDTO = statusHistoryMapper.toDto(statusHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statusHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistoryList = statusHistoryRepository.findAll();
        assertThat(statusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStatusHistory() throws Exception {
        // Initialize the database
        statusHistoryRepository.saveAndFlush(statusHistory);

        int databaseSizeBeforeDelete = statusHistoryRepository.findAll().size();

        // Delete the statusHistory
        restStatusHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, statusHistory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StatusHistory> statusHistoryList = statusHistoryRepository.findAll();
        assertThat(statusHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
