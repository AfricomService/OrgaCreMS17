package com.orgacare.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.AclSid;
import com.orgacare.app.repository.AclSidRepository;
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
 * Integration tests for the {@link AclSidResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AclSidResourceIT {

    private static final String DEFAULT_SID_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SID_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SID_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_SID_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_SID_KEY = "AAAAAAAAAA";
    private static final String UPDATED_SID_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_DESCRIPTIF = "AAAAAAAAAA";
    private static final String UPDATED_NOM_DESCRIPTIF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/acl-sids";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AclSidRepository aclSidRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAclSidMockMvc;

    private AclSid aclSid;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclSid createEntity(EntityManager em) {
        AclSid aclSid = new AclSid()
            .sidType(DEFAULT_SID_TYPE)
            .sidValue(DEFAULT_SID_VALUE)
            .sidKey(DEFAULT_SID_KEY)
            .nomDescriptif(DEFAULT_NOM_DESCRIPTIF);
        return aclSid;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclSid createUpdatedEntity(EntityManager em) {
        AclSid aclSid = new AclSid()
            .sidType(UPDATED_SID_TYPE)
            .sidValue(UPDATED_SID_VALUE)
            .sidKey(UPDATED_SID_KEY)
            .nomDescriptif(UPDATED_NOM_DESCRIPTIF);
        return aclSid;
    }

    @BeforeEach
    public void initTest() {
        aclSid = createEntity(em);
    }

    @Test
    @Transactional
    void createAclSid() throws Exception {
        int databaseSizeBeforeCreate = aclSidRepository.findAll().size();
        // Create the AclSid
        restAclSidMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclSid))
            )
            .andExpect(status().isCreated());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeCreate + 1);
        AclSid testAclSid = aclSidList.get(aclSidList.size() - 1);
        assertThat(testAclSid.getSidType()).isEqualTo(DEFAULT_SID_TYPE);
        assertThat(testAclSid.getSidValue()).isEqualTo(DEFAULT_SID_VALUE);
        assertThat(testAclSid.getSidKey()).isEqualTo(DEFAULT_SID_KEY);
        assertThat(testAclSid.getNomDescriptif()).isEqualTo(DEFAULT_NOM_DESCRIPTIF);
    }

    @Test
    @Transactional
    void createAclSidWithExistingId() throws Exception {
        // Create the AclSid with an existing ID
        aclSid.setId(1L);

        int databaseSizeBeforeCreate = aclSidRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAclSidMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclSid))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAclSids() throws Exception {
        // Initialize the database
        aclSidRepository.saveAndFlush(aclSid);

        // Get all the aclSidList
        restAclSidMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aclSid.getId().intValue())))
            .andExpect(jsonPath("$.[*].sidType").value(hasItem(DEFAULT_SID_TYPE)))
            .andExpect(jsonPath("$.[*].sidValue").value(hasItem(DEFAULT_SID_VALUE)))
            .andExpect(jsonPath("$.[*].sidKey").value(hasItem(DEFAULT_SID_KEY)))
            .andExpect(jsonPath("$.[*].nomDescriptif").value(hasItem(DEFAULT_NOM_DESCRIPTIF)));
    }

    @Test
    @Transactional
    void getAclSid() throws Exception {
        // Initialize the database
        aclSidRepository.saveAndFlush(aclSid);

        // Get the aclSid
        restAclSidMockMvc
            .perform(get(ENTITY_API_URL_ID, aclSid.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aclSid.getId().intValue()))
            .andExpect(jsonPath("$.sidType").value(DEFAULT_SID_TYPE))
            .andExpect(jsonPath("$.sidValue").value(DEFAULT_SID_VALUE))
            .andExpect(jsonPath("$.sidKey").value(DEFAULT_SID_KEY))
            .andExpect(jsonPath("$.nomDescriptif").value(DEFAULT_NOM_DESCRIPTIF));
    }

    @Test
    @Transactional
    void getNonExistingAclSid() throws Exception {
        // Get the aclSid
        restAclSidMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAclSid() throws Exception {
        // Initialize the database
        aclSidRepository.saveAndFlush(aclSid);

        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();

        // Update the aclSid
        AclSid updatedAclSid = aclSidRepository.findById(aclSid.getId()).get();
        // Disconnect from session so that the updates on updatedAclSid are not directly saved in db
        em.detach(updatedAclSid);
        updatedAclSid.sidType(UPDATED_SID_TYPE).sidValue(UPDATED_SID_VALUE).sidKey(UPDATED_SID_KEY).nomDescriptif(UPDATED_NOM_DESCRIPTIF);

        restAclSidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAclSid.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAclSid))
            )
            .andExpect(status().isOk());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
        AclSid testAclSid = aclSidList.get(aclSidList.size() - 1);
        assertThat(testAclSid.getSidType()).isEqualTo(UPDATED_SID_TYPE);
        assertThat(testAclSid.getSidValue()).isEqualTo(UPDATED_SID_VALUE);
        assertThat(testAclSid.getSidKey()).isEqualTo(UPDATED_SID_KEY);
        assertThat(testAclSid.getNomDescriptif()).isEqualTo(UPDATED_NOM_DESCRIPTIF);
    }

    @Test
    @Transactional
    void putNonExistingAclSid() throws Exception {
        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();
        aclSid.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclSidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclSid.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclSid))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAclSid() throws Exception {
        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();
        aclSid.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclSidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclSid))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAclSid() throws Exception {
        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();
        aclSid.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclSidMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclSid))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAclSidWithPatch() throws Exception {
        // Initialize the database
        aclSidRepository.saveAndFlush(aclSid);

        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();

        // Update the aclSid using partial update
        AclSid partialUpdatedAclSid = new AclSid();
        partialUpdatedAclSid.setId(aclSid.getId());

        partialUpdatedAclSid
            .sidType(UPDATED_SID_TYPE)
            .sidValue(UPDATED_SID_VALUE)
            .sidKey(UPDATED_SID_KEY)
            .nomDescriptif(UPDATED_NOM_DESCRIPTIF);

        restAclSidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclSid.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclSid))
            )
            .andExpect(status().isOk());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
        AclSid testAclSid = aclSidList.get(aclSidList.size() - 1);
        assertThat(testAclSid.getSidType()).isEqualTo(UPDATED_SID_TYPE);
        assertThat(testAclSid.getSidValue()).isEqualTo(UPDATED_SID_VALUE);
        assertThat(testAclSid.getSidKey()).isEqualTo(UPDATED_SID_KEY);
        assertThat(testAclSid.getNomDescriptif()).isEqualTo(UPDATED_NOM_DESCRIPTIF);
    }

    @Test
    @Transactional
    void fullUpdateAclSidWithPatch() throws Exception {
        // Initialize the database
        aclSidRepository.saveAndFlush(aclSid);

        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();

        // Update the aclSid using partial update
        AclSid partialUpdatedAclSid = new AclSid();
        partialUpdatedAclSid.setId(aclSid.getId());

        partialUpdatedAclSid
            .sidType(UPDATED_SID_TYPE)
            .sidValue(UPDATED_SID_VALUE)
            .sidKey(UPDATED_SID_KEY)
            .nomDescriptif(UPDATED_NOM_DESCRIPTIF);

        restAclSidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclSid.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclSid))
            )
            .andExpect(status().isOk());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
        AclSid testAclSid = aclSidList.get(aclSidList.size() - 1);
        assertThat(testAclSid.getSidType()).isEqualTo(UPDATED_SID_TYPE);
        assertThat(testAclSid.getSidValue()).isEqualTo(UPDATED_SID_VALUE);
        assertThat(testAclSid.getSidKey()).isEqualTo(UPDATED_SID_KEY);
        assertThat(testAclSid.getNomDescriptif()).isEqualTo(UPDATED_NOM_DESCRIPTIF);
    }

    @Test
    @Transactional
    void patchNonExistingAclSid() throws Exception {
        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();
        aclSid.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclSidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aclSid.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclSid))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAclSid() throws Exception {
        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();
        aclSid.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclSidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclSid))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAclSid() throws Exception {
        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();
        aclSid.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclSidMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclSid))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAclSid() throws Exception {
        // Initialize the database
        aclSidRepository.saveAndFlush(aclSid);

        int databaseSizeBeforeDelete = aclSidRepository.findAll().size();

        // Delete the aclSid
        restAclSidMockMvc
            .perform(delete(ENTITY_API_URL_ID, aclSid.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
