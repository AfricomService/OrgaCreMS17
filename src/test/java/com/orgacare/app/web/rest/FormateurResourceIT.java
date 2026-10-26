package com.orgacare.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.Formateur;
import com.orgacare.app.repository.FormateurRepository;
import com.orgacare.app.service.dto.FormateurDTO;
import com.orgacare.app.service.mapper.FormateurMapper;
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
 * Integration tests for the {@link FormateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormateurResourceIT {

    private static final String DEFAULT_NOM_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_CIN = "AAAAAAAAAA";
    private static final String UPDATED_CIN = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final Long DEFAULT_THEME_ID = 1L;
    private static final Long UPDATED_THEME_ID = 2L;

    private static final String ENTITY_API_URL = "/api/formateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormateurRepository formateurRepository;

    @Autowired
    private FormateurMapper formateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormateurMockMvc;

    private Formateur formateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formateur createEntity(EntityManager em) {
        Formateur formateur = new Formateur()
            .nomPrenom(DEFAULT_NOM_PRENOM)
            .cin(DEFAULT_CIN)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .status(DEFAULT_STATUS)
            .comments(DEFAULT_COMMENTS)
            .themeId(DEFAULT_THEME_ID);
        return formateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formateur createUpdatedEntity(EntityManager em) {
        Formateur formateur = new Formateur()
            .nomPrenom(UPDATED_NOM_PRENOM)
            .cin(UPDATED_CIN)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .status(UPDATED_STATUS)
            .comments(UPDATED_COMMENTS)
            .themeId(UPDATED_THEME_ID);
        return formateur;
    }

    @BeforeEach
    public void initTest() {
        formateur = createEntity(em);
    }

    @Test
    @Transactional
    void createFormateur() throws Exception {
        int databaseSizeBeforeCreate = formateurRepository.findAll().size();
        // Create the Formateur
        FormateurDTO formateurDTO = formateurMapper.toDto(formateur);
        restFormateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeCreate + 1);
        Formateur testFormateur = formateurList.get(formateurList.size() - 1);
        assertThat(testFormateur.getNomPrenom()).isEqualTo(DEFAULT_NOM_PRENOM);
        assertThat(testFormateur.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testFormateur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFormateur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testFormateur.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFormateur.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testFormateur.getThemeId()).isEqualTo(DEFAULT_THEME_ID);
    }

    @Test
    @Transactional
    void createFormateurWithExistingId() throws Exception {
        // Create the Formateur with an existing ID
        formateur.setId(1L);
        FormateurDTO formateurDTO = formateurMapper.toDto(formateur);

        int databaseSizeBeforeCreate = formateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = formateurRepository.findAll().size();
        // set the field null
        formateur.setNomPrenom(null);

        // Create the Formateur, which fails.
        FormateurDTO formateurDTO = formateurMapper.toDto(formateur);

        restFormateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurDTO))
            )
            .andExpect(status().isBadRequest());

        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFormateurs() throws Exception {
        // Initialize the database
        formateurRepository.saveAndFlush(formateur);

        // Get all the formateurList
        restFormateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPrenom").value(hasItem(DEFAULT_NOM_PRENOM)))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].themeId").value(hasItem(DEFAULT_THEME_ID.intValue())));
    }

    @Test
    @Transactional
    void getFormateur() throws Exception {
        // Initialize the database
        formateurRepository.saveAndFlush(formateur);

        // Get the formateur
        restFormateurMockMvc
            .perform(get(ENTITY_API_URL_ID, formateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formateur.getId().intValue()))
            .andExpect(jsonPath("$.nomPrenom").value(DEFAULT_NOM_PRENOM))
            .andExpect(jsonPath("$.cin").value(DEFAULT_CIN))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.themeId").value(DEFAULT_THEME_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFormateur() throws Exception {
        // Get the formateur
        restFormateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormateur() throws Exception {
        // Initialize the database
        formateurRepository.saveAndFlush(formateur);

        int databaseSizeBeforeUpdate = formateurRepository.findAll().size();

        // Update the formateur
        Formateur updatedFormateur = formateurRepository.findById(formateur.getId()).get();
        // Disconnect from session so that the updates on updatedFormateur are not directly saved in db
        em.detach(updatedFormateur);
        updatedFormateur
            .nomPrenom(UPDATED_NOM_PRENOM)
            .cin(UPDATED_CIN)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .status(UPDATED_STATUS)
            .comments(UPDATED_COMMENTS)
            .themeId(UPDATED_THEME_ID);
        FormateurDTO formateurDTO = formateurMapper.toDto(updatedFormateur);

        restFormateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formateurDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeUpdate);
        Formateur testFormateur = formateurList.get(formateurList.size() - 1);
        assertThat(testFormateur.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
        assertThat(testFormateur.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testFormateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFormateur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testFormateur.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFormateur.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testFormateur.getThemeId()).isEqualTo(UPDATED_THEME_ID);
    }

    @Test
    @Transactional
    void putNonExistingFormateur() throws Exception {
        int databaseSizeBeforeUpdate = formateurRepository.findAll().size();
        formateur.setId(count.incrementAndGet());

        // Create the Formateur
        FormateurDTO formateurDTO = formateurMapper.toDto(formateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formateurDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormateur() throws Exception {
        int databaseSizeBeforeUpdate = formateurRepository.findAll().size();
        formateur.setId(count.incrementAndGet());

        // Create the Formateur
        FormateurDTO formateurDTO = formateurMapper.toDto(formateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormateur() throws Exception {
        int databaseSizeBeforeUpdate = formateurRepository.findAll().size();
        formateur.setId(count.incrementAndGet());

        // Create the Formateur
        FormateurDTO formateurDTO = formateurMapper.toDto(formateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormateurMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormateurWithPatch() throws Exception {
        // Initialize the database
        formateurRepository.saveAndFlush(formateur);

        int databaseSizeBeforeUpdate = formateurRepository.findAll().size();

        // Update the formateur using partial update
        Formateur partialUpdatedFormateur = new Formateur();
        partialUpdatedFormateur.setId(formateur.getId());

        partialUpdatedFormateur.cin(UPDATED_CIN).status(UPDATED_STATUS).comments(UPDATED_COMMENTS);

        restFormateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormateur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormateur))
            )
            .andExpect(status().isOk());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeUpdate);
        Formateur testFormateur = formateurList.get(formateurList.size() - 1);
        assertThat(testFormateur.getNomPrenom()).isEqualTo(DEFAULT_NOM_PRENOM);
        assertThat(testFormateur.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testFormateur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFormateur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testFormateur.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFormateur.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testFormateur.getThemeId()).isEqualTo(DEFAULT_THEME_ID);
    }

    @Test
    @Transactional
    void fullUpdateFormateurWithPatch() throws Exception {
        // Initialize the database
        formateurRepository.saveAndFlush(formateur);

        int databaseSizeBeforeUpdate = formateurRepository.findAll().size();

        // Update the formateur using partial update
        Formateur partialUpdatedFormateur = new Formateur();
        partialUpdatedFormateur.setId(formateur.getId());

        partialUpdatedFormateur
            .nomPrenom(UPDATED_NOM_PRENOM)
            .cin(UPDATED_CIN)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .status(UPDATED_STATUS)
            .comments(UPDATED_COMMENTS)
            .themeId(UPDATED_THEME_ID);

        restFormateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormateur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormateur))
            )
            .andExpect(status().isOk());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeUpdate);
        Formateur testFormateur = formateurList.get(formateurList.size() - 1);
        assertThat(testFormateur.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
        assertThat(testFormateur.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testFormateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFormateur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testFormateur.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFormateur.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testFormateur.getThemeId()).isEqualTo(UPDATED_THEME_ID);
    }

    @Test
    @Transactional
    void patchNonExistingFormateur() throws Exception {
        int databaseSizeBeforeUpdate = formateurRepository.findAll().size();
        formateur.setId(count.incrementAndGet());

        // Create the Formateur
        FormateurDTO formateurDTO = formateurMapper.toDto(formateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formateurDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormateur() throws Exception {
        int databaseSizeBeforeUpdate = formateurRepository.findAll().size();
        formateur.setId(count.incrementAndGet());

        // Create the Formateur
        FormateurDTO formateurDTO = formateurMapper.toDto(formateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormateur() throws Exception {
        int databaseSizeBeforeUpdate = formateurRepository.findAll().size();
        formateur.setId(count.incrementAndGet());

        // Create the Formateur
        FormateurDTO formateurDTO = formateurMapper.toDto(formateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormateurMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormateur() throws Exception {
        // Initialize the database
        formateurRepository.saveAndFlush(formateur);

        int databaseSizeBeforeDelete = formateurRepository.findAll().size();

        // Delete the formateur
        restFormateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, formateur.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
