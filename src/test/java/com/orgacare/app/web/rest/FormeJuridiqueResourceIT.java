package com.orgacare.app.web.rest;

import static com.orgacare.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.FormeJuridique;
import com.orgacare.app.repository.FormeJuridiqueRepository;
import com.orgacare.app.service.dto.FormeJuridiqueDTO;
import com.orgacare.app.service.mapper.FormeJuridiqueMapper;
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
 * Integration tests for the {@link FormeJuridiqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormeJuridiqueResourceIT {

    private static final String DEFAULT_ABREVIATION = "AAAAAAAAAA";
    private static final String UPDATED_ABREVIATION = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/forme-juridiques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormeJuridiqueRepository formeJuridiqueRepository;

    @Autowired
    private FormeJuridiqueMapper formeJuridiqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormeJuridiqueMockMvc;

    private FormeJuridique formeJuridique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormeJuridique createEntity(EntityManager em) {
        FormeJuridique formeJuridique = new FormeJuridique()
            .abreviation(DEFAULT_ABREVIATION)
            .nom(DEFAULT_NOM)
            .dateCreation(DEFAULT_DATE_CREATION)
            .etat(DEFAULT_ETAT);
        return formeJuridique;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormeJuridique createUpdatedEntity(EntityManager em) {
        FormeJuridique formeJuridique = new FormeJuridique()
            .abreviation(UPDATED_ABREVIATION)
            .nom(UPDATED_NOM)
            .dateCreation(UPDATED_DATE_CREATION)
            .etat(UPDATED_ETAT);
        return formeJuridique;
    }

    @BeforeEach
    public void initTest() {
        formeJuridique = createEntity(em);
    }

    @Test
    @Transactional
    void createFormeJuridique() throws Exception {
        int databaseSizeBeforeCreate = formeJuridiqueRepository.findAll().size();
        // Create the FormeJuridique
        FormeJuridiqueDTO formeJuridiqueDTO = formeJuridiqueMapper.toDto(formeJuridique);
        restFormeJuridiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formeJuridiqueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FormeJuridique in the database
        List<FormeJuridique> formeJuridiqueList = formeJuridiqueRepository.findAll();
        assertThat(formeJuridiqueList).hasSize(databaseSizeBeforeCreate + 1);
        FormeJuridique testFormeJuridique = formeJuridiqueList.get(formeJuridiqueList.size() - 1);
        assertThat(testFormeJuridique.getAbreviation()).isEqualTo(DEFAULT_ABREVIATION);
        assertThat(testFormeJuridique.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testFormeJuridique.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testFormeJuridique.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void createFormeJuridiqueWithExistingId() throws Exception {
        // Create the FormeJuridique with an existing ID
        formeJuridique.setId(1L);
        FormeJuridiqueDTO formeJuridiqueDTO = formeJuridiqueMapper.toDto(formeJuridique);

        int databaseSizeBeforeCreate = formeJuridiqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormeJuridiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formeJuridiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormeJuridique in the database
        List<FormeJuridique> formeJuridiqueList = formeJuridiqueRepository.findAll();
        assertThat(formeJuridiqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormeJuridiques() throws Exception {
        // Initialize the database
        formeJuridiqueRepository.saveAndFlush(formeJuridique);

        // Get all the formeJuridiqueList
        restFormeJuridiqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formeJuridique.getId().intValue())))
            .andExpect(jsonPath("$.[*].abreviation").value(hasItem(DEFAULT_ABREVIATION)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)));
    }

    @Test
    @Transactional
    void getFormeJuridique() throws Exception {
        // Initialize the database
        formeJuridiqueRepository.saveAndFlush(formeJuridique);

        // Get the formeJuridique
        restFormeJuridiqueMockMvc
            .perform(get(ENTITY_API_URL_ID, formeJuridique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formeJuridique.getId().intValue()))
            .andExpect(jsonPath("$.abreviation").value(DEFAULT_ABREVIATION))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT));
    }

    @Test
    @Transactional
    void getNonExistingFormeJuridique() throws Exception {
        // Get the formeJuridique
        restFormeJuridiqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormeJuridique() throws Exception {
        // Initialize the database
        formeJuridiqueRepository.saveAndFlush(formeJuridique);

        int databaseSizeBeforeUpdate = formeJuridiqueRepository.findAll().size();

        // Update the formeJuridique
        FormeJuridique updatedFormeJuridique = formeJuridiqueRepository.findById(formeJuridique.getId()).get();
        // Disconnect from session so that the updates on updatedFormeJuridique are not directly saved in db
        em.detach(updatedFormeJuridique);
        updatedFormeJuridique.abreviation(UPDATED_ABREVIATION).nom(UPDATED_NOM).dateCreation(UPDATED_DATE_CREATION).etat(UPDATED_ETAT);
        FormeJuridiqueDTO formeJuridiqueDTO = formeJuridiqueMapper.toDto(updatedFormeJuridique);

        restFormeJuridiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formeJuridiqueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formeJuridiqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormeJuridique in the database
        List<FormeJuridique> formeJuridiqueList = formeJuridiqueRepository.findAll();
        assertThat(formeJuridiqueList).hasSize(databaseSizeBeforeUpdate);
        FormeJuridique testFormeJuridique = formeJuridiqueList.get(formeJuridiqueList.size() - 1);
        assertThat(testFormeJuridique.getAbreviation()).isEqualTo(UPDATED_ABREVIATION);
        assertThat(testFormeJuridique.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFormeJuridique.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testFormeJuridique.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void putNonExistingFormeJuridique() throws Exception {
        int databaseSizeBeforeUpdate = formeJuridiqueRepository.findAll().size();
        formeJuridique.setId(count.incrementAndGet());

        // Create the FormeJuridique
        FormeJuridiqueDTO formeJuridiqueDTO = formeJuridiqueMapper.toDto(formeJuridique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormeJuridiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formeJuridiqueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formeJuridiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormeJuridique in the database
        List<FormeJuridique> formeJuridiqueList = formeJuridiqueRepository.findAll();
        assertThat(formeJuridiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormeJuridique() throws Exception {
        int databaseSizeBeforeUpdate = formeJuridiqueRepository.findAll().size();
        formeJuridique.setId(count.incrementAndGet());

        // Create the FormeJuridique
        FormeJuridiqueDTO formeJuridiqueDTO = formeJuridiqueMapper.toDto(formeJuridique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormeJuridiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formeJuridiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormeJuridique in the database
        List<FormeJuridique> formeJuridiqueList = formeJuridiqueRepository.findAll();
        assertThat(formeJuridiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormeJuridique() throws Exception {
        int databaseSizeBeforeUpdate = formeJuridiqueRepository.findAll().size();
        formeJuridique.setId(count.incrementAndGet());

        // Create the FormeJuridique
        FormeJuridiqueDTO formeJuridiqueDTO = formeJuridiqueMapper.toDto(formeJuridique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormeJuridiqueMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formeJuridiqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormeJuridique in the database
        List<FormeJuridique> formeJuridiqueList = formeJuridiqueRepository.findAll();
        assertThat(formeJuridiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormeJuridiqueWithPatch() throws Exception {
        // Initialize the database
        formeJuridiqueRepository.saveAndFlush(formeJuridique);

        int databaseSizeBeforeUpdate = formeJuridiqueRepository.findAll().size();

        // Update the formeJuridique using partial update
        FormeJuridique partialUpdatedFormeJuridique = new FormeJuridique();
        partialUpdatedFormeJuridique.setId(formeJuridique.getId());

        partialUpdatedFormeJuridique.nom(UPDATED_NOM).dateCreation(UPDATED_DATE_CREATION);

        restFormeJuridiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormeJuridique.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormeJuridique))
            )
            .andExpect(status().isOk());

        // Validate the FormeJuridique in the database
        List<FormeJuridique> formeJuridiqueList = formeJuridiqueRepository.findAll();
        assertThat(formeJuridiqueList).hasSize(databaseSizeBeforeUpdate);
        FormeJuridique testFormeJuridique = formeJuridiqueList.get(formeJuridiqueList.size() - 1);
        assertThat(testFormeJuridique.getAbreviation()).isEqualTo(DEFAULT_ABREVIATION);
        assertThat(testFormeJuridique.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFormeJuridique.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testFormeJuridique.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void fullUpdateFormeJuridiqueWithPatch() throws Exception {
        // Initialize the database
        formeJuridiqueRepository.saveAndFlush(formeJuridique);

        int databaseSizeBeforeUpdate = formeJuridiqueRepository.findAll().size();

        // Update the formeJuridique using partial update
        FormeJuridique partialUpdatedFormeJuridique = new FormeJuridique();
        partialUpdatedFormeJuridique.setId(formeJuridique.getId());

        partialUpdatedFormeJuridique
            .abreviation(UPDATED_ABREVIATION)
            .nom(UPDATED_NOM)
            .dateCreation(UPDATED_DATE_CREATION)
            .etat(UPDATED_ETAT);

        restFormeJuridiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormeJuridique.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormeJuridique))
            )
            .andExpect(status().isOk());

        // Validate the FormeJuridique in the database
        List<FormeJuridique> formeJuridiqueList = formeJuridiqueRepository.findAll();
        assertThat(formeJuridiqueList).hasSize(databaseSizeBeforeUpdate);
        FormeJuridique testFormeJuridique = formeJuridiqueList.get(formeJuridiqueList.size() - 1);
        assertThat(testFormeJuridique.getAbreviation()).isEqualTo(UPDATED_ABREVIATION);
        assertThat(testFormeJuridique.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFormeJuridique.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testFormeJuridique.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void patchNonExistingFormeJuridique() throws Exception {
        int databaseSizeBeforeUpdate = formeJuridiqueRepository.findAll().size();
        formeJuridique.setId(count.incrementAndGet());

        // Create the FormeJuridique
        FormeJuridiqueDTO formeJuridiqueDTO = formeJuridiqueMapper.toDto(formeJuridique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormeJuridiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formeJuridiqueDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formeJuridiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormeJuridique in the database
        List<FormeJuridique> formeJuridiqueList = formeJuridiqueRepository.findAll();
        assertThat(formeJuridiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormeJuridique() throws Exception {
        int databaseSizeBeforeUpdate = formeJuridiqueRepository.findAll().size();
        formeJuridique.setId(count.incrementAndGet());

        // Create the FormeJuridique
        FormeJuridiqueDTO formeJuridiqueDTO = formeJuridiqueMapper.toDto(formeJuridique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormeJuridiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formeJuridiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormeJuridique in the database
        List<FormeJuridique> formeJuridiqueList = formeJuridiqueRepository.findAll();
        assertThat(formeJuridiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormeJuridique() throws Exception {
        int databaseSizeBeforeUpdate = formeJuridiqueRepository.findAll().size();
        formeJuridique.setId(count.incrementAndGet());

        // Create the FormeJuridique
        FormeJuridiqueDTO formeJuridiqueDTO = formeJuridiqueMapper.toDto(formeJuridique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormeJuridiqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formeJuridiqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormeJuridique in the database
        List<FormeJuridique> formeJuridiqueList = formeJuridiqueRepository.findAll();
        assertThat(formeJuridiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormeJuridique() throws Exception {
        // Initialize the database
        formeJuridiqueRepository.saveAndFlush(formeJuridique);

        int databaseSizeBeforeDelete = formeJuridiqueRepository.findAll().size();

        // Delete the formeJuridique
        restFormeJuridiqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, formeJuridique.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormeJuridique> formeJuridiqueList = formeJuridiqueRepository.findAll();
        assertThat(formeJuridiqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
