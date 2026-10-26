package com.orgacare.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.AclEntry;
import com.orgacare.app.repository.AclEntryRepository;
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
 * Integration tests for the {@link AclEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AclEntryResourceIT {

    private static final String DEFAULT_OBJECT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_OBJECT_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_OBJECT_ID = 1L;
    private static final Long UPDATED_OBJECT_ID = 2L;

    private static final Long DEFAULT_SID_ID = 1L;
    private static final Long UPDATED_SID_ID = 2L;

    private static final Boolean DEFAULT_CAN_READ = false;
    private static final Boolean UPDATED_CAN_READ = true;

    private static final Boolean DEFAULT_CAN_WRITE = false;
    private static final Boolean UPDATED_CAN_WRITE = true;

    private static final String ENTITY_API_URL = "/api/acl-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AclEntryRepository aclEntryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAclEntryMockMvc;

    private AclEntry aclEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclEntry createEntity(EntityManager em) {
        AclEntry aclEntry = new AclEntry()
            .objectType(DEFAULT_OBJECT_TYPE)
            .objectId(DEFAULT_OBJECT_ID)
            .sidId(DEFAULT_SID_ID)
            .canRead(DEFAULT_CAN_READ)
            .canWrite(DEFAULT_CAN_WRITE);
        return aclEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclEntry createUpdatedEntity(EntityManager em) {
        AclEntry aclEntry = new AclEntry()
            .objectType(UPDATED_OBJECT_TYPE)
            .objectId(UPDATED_OBJECT_ID)
            .sidId(UPDATED_SID_ID)
            .canRead(UPDATED_CAN_READ)
            .canWrite(UPDATED_CAN_WRITE);
        return aclEntry;
    }

    @BeforeEach
    public void initTest() {
        aclEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createAclEntry() throws Exception {
        int databaseSizeBeforeCreate = aclEntryRepository.findAll().size();
        // Create the AclEntry
        restAclEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntry))
            )
            .andExpect(status().isCreated());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeCreate + 1);
        AclEntry testAclEntry = aclEntryList.get(aclEntryList.size() - 1);
        assertThat(testAclEntry.getObjectType()).isEqualTo(DEFAULT_OBJECT_TYPE);
        assertThat(testAclEntry.getObjectId()).isEqualTo(DEFAULT_OBJECT_ID);
        assertThat(testAclEntry.getSidId()).isEqualTo(DEFAULT_SID_ID);
        assertThat(testAclEntry.getCanRead()).isEqualTo(DEFAULT_CAN_READ);
        assertThat(testAclEntry.getCanWrite()).isEqualTo(DEFAULT_CAN_WRITE);
    }

    @Test
    @Transactional
    void createAclEntryWithExistingId() throws Exception {
        // Create the AclEntry with an existing ID
        aclEntry.setId(1L);

        int databaseSizeBeforeCreate = aclEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAclEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAclEntries() throws Exception {
        // Initialize the database
        aclEntryRepository.saveAndFlush(aclEntry);

        // Get all the aclEntryList
        restAclEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aclEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].objectType").value(hasItem(DEFAULT_OBJECT_TYPE)))
            .andExpect(jsonPath("$.[*].objectId").value(hasItem(DEFAULT_OBJECT_ID.intValue())))
            .andExpect(jsonPath("$.[*].sidId").value(hasItem(DEFAULT_SID_ID.intValue())))
            .andExpect(jsonPath("$.[*].canRead").value(hasItem(DEFAULT_CAN_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].canWrite").value(hasItem(DEFAULT_CAN_WRITE.booleanValue())));
    }

    @Test
    @Transactional
    void getAclEntry() throws Exception {
        // Initialize the database
        aclEntryRepository.saveAndFlush(aclEntry);

        // Get the aclEntry
        restAclEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, aclEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aclEntry.getId().intValue()))
            .andExpect(jsonPath("$.objectType").value(DEFAULT_OBJECT_TYPE))
            .andExpect(jsonPath("$.objectId").value(DEFAULT_OBJECT_ID.intValue()))
            .andExpect(jsonPath("$.sidId").value(DEFAULT_SID_ID.intValue()))
            .andExpect(jsonPath("$.canRead").value(DEFAULT_CAN_READ.booleanValue()))
            .andExpect(jsonPath("$.canWrite").value(DEFAULT_CAN_WRITE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAclEntry() throws Exception {
        // Get the aclEntry
        restAclEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAclEntry() throws Exception {
        // Initialize the database
        aclEntryRepository.saveAndFlush(aclEntry);

        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();

        // Update the aclEntry
        AclEntry updatedAclEntry = aclEntryRepository.findById(aclEntry.getId()).get();
        // Disconnect from session so that the updates on updatedAclEntry are not directly saved in db
        em.detach(updatedAclEntry);
        updatedAclEntry
            .objectType(UPDATED_OBJECT_TYPE)
            .objectId(UPDATED_OBJECT_ID)
            .sidId(UPDATED_SID_ID)
            .canRead(UPDATED_CAN_READ)
            .canWrite(UPDATED_CAN_WRITE);

        restAclEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAclEntry.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAclEntry))
            )
            .andExpect(status().isOk());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
        AclEntry testAclEntry = aclEntryList.get(aclEntryList.size() - 1);
        assertThat(testAclEntry.getObjectType()).isEqualTo(UPDATED_OBJECT_TYPE);
        assertThat(testAclEntry.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
        assertThat(testAclEntry.getSidId()).isEqualTo(UPDATED_SID_ID);
        assertThat(testAclEntry.getCanRead()).isEqualTo(UPDATED_CAN_READ);
        assertThat(testAclEntry.getCanWrite()).isEqualTo(UPDATED_CAN_WRITE);
    }

    @Test
    @Transactional
    void putNonExistingAclEntry() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();
        aclEntry.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclEntry.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAclEntry() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();
        aclEntry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAclEntry() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();
        aclEntry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclEntryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntry))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAclEntryWithPatch() throws Exception {
        // Initialize the database
        aclEntryRepository.saveAndFlush(aclEntry);

        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();

        // Update the aclEntry using partial update
        AclEntry partialUpdatedAclEntry = new AclEntry();
        partialUpdatedAclEntry.setId(aclEntry.getId());

        partialUpdatedAclEntry.sidId(UPDATED_SID_ID).canRead(UPDATED_CAN_READ).canWrite(UPDATED_CAN_WRITE);

        restAclEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclEntry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclEntry))
            )
            .andExpect(status().isOk());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
        AclEntry testAclEntry = aclEntryList.get(aclEntryList.size() - 1);
        assertThat(testAclEntry.getObjectType()).isEqualTo(DEFAULT_OBJECT_TYPE);
        assertThat(testAclEntry.getObjectId()).isEqualTo(DEFAULT_OBJECT_ID);
        assertThat(testAclEntry.getSidId()).isEqualTo(UPDATED_SID_ID);
        assertThat(testAclEntry.getCanRead()).isEqualTo(UPDATED_CAN_READ);
        assertThat(testAclEntry.getCanWrite()).isEqualTo(UPDATED_CAN_WRITE);
    }

    @Test
    @Transactional
    void fullUpdateAclEntryWithPatch() throws Exception {
        // Initialize the database
        aclEntryRepository.saveAndFlush(aclEntry);

        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();

        // Update the aclEntry using partial update
        AclEntry partialUpdatedAclEntry = new AclEntry();
        partialUpdatedAclEntry.setId(aclEntry.getId());

        partialUpdatedAclEntry
            .objectType(UPDATED_OBJECT_TYPE)
            .objectId(UPDATED_OBJECT_ID)
            .sidId(UPDATED_SID_ID)
            .canRead(UPDATED_CAN_READ)
            .canWrite(UPDATED_CAN_WRITE);

        restAclEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclEntry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclEntry))
            )
            .andExpect(status().isOk());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
        AclEntry testAclEntry = aclEntryList.get(aclEntryList.size() - 1);
        assertThat(testAclEntry.getObjectType()).isEqualTo(UPDATED_OBJECT_TYPE);
        assertThat(testAclEntry.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
        assertThat(testAclEntry.getSidId()).isEqualTo(UPDATED_SID_ID);
        assertThat(testAclEntry.getCanRead()).isEqualTo(UPDATED_CAN_READ);
        assertThat(testAclEntry.getCanWrite()).isEqualTo(UPDATED_CAN_WRITE);
    }

    @Test
    @Transactional
    void patchNonExistingAclEntry() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();
        aclEntry.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aclEntry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAclEntry() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();
        aclEntry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAclEntry() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();
        aclEntry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclEntry))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAclEntry() throws Exception {
        // Initialize the database
        aclEntryRepository.saveAndFlush(aclEntry);

        int databaseSizeBeforeDelete = aclEntryRepository.findAll().size();

        // Delete the aclEntry
        restAclEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, aclEntry.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
