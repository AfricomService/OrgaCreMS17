package com.orgacare.app.web.rest;

import static com.orgacare.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.Salaire;
import com.orgacare.app.repository.SalaireRepository;
import com.orgacare.app.service.dto.SalaireDTO;
import com.orgacare.app.service.mapper.SalaireMapper;
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
 * Integration tests for the {@link SalaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalaireResourceIT {

    private static final Double DEFAULT_VALEUR = 1D;
    private static final Double UPDATED_VALEUR = 2D;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_PERSONNE_ID = 1L;
    private static final Long UPDATED_PERSONNE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/salaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalaireRepository salaireRepository;

    @Autowired
    private SalaireMapper salaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalaireMockMvc;

    private Salaire salaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salaire createEntity(EntityManager em) {
        Salaire salaire = new Salaire().valeur(DEFAULT_VALEUR).date(DEFAULT_DATE).personneId(DEFAULT_PERSONNE_ID);
        return salaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salaire createUpdatedEntity(EntityManager em) {
        Salaire salaire = new Salaire().valeur(UPDATED_VALEUR).date(UPDATED_DATE).personneId(UPDATED_PERSONNE_ID);
        return salaire;
    }

    @BeforeEach
    public void initTest() {
        salaire = createEntity(em);
    }

    @Test
    @Transactional
    void createSalaire() throws Exception {
        int databaseSizeBeforeCreate = salaireRepository.findAll().size();
        // Create the Salaire
        SalaireDTO salaireDTO = salaireMapper.toDto(salaire);
        restSalaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Salaire in the database
        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeCreate + 1);
        Salaire testSalaire = salaireList.get(salaireList.size() - 1);
        assertThat(testSalaire.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testSalaire.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSalaire.getPersonneId()).isEqualTo(DEFAULT_PERSONNE_ID);
    }

    @Test
    @Transactional
    void createSalaireWithExistingId() throws Exception {
        // Create the Salaire with an existing ID
        salaire.setId(1L);
        SalaireDTO salaireDTO = salaireMapper.toDto(salaire);

        int databaseSizeBeforeCreate = salaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salaire in the database
        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaireRepository.findAll().size();
        // set the field null
        salaire.setValeur(null);

        // Create the Salaire, which fails.
        SalaireDTO salaireDTO = salaireMapper.toDto(salaire);

        restSalaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaireDTO))
            )
            .andExpect(status().isBadRequest());

        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSalaires() throws Exception {
        // Initialize the database
        salaireRepository.saveAndFlush(salaire);

        // Get all the salaireList
        restSalaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].personneId").value(hasItem(DEFAULT_PERSONNE_ID.intValue())));
    }

    @Test
    @Transactional
    void getSalaire() throws Exception {
        // Initialize the database
        salaireRepository.saveAndFlush(salaire);

        // Get the salaire
        restSalaireMockMvc
            .perform(get(ENTITY_API_URL_ID, salaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salaire.getId().intValue()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR.doubleValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.personneId").value(DEFAULT_PERSONNE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSalaire() throws Exception {
        // Get the salaire
        restSalaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSalaire() throws Exception {
        // Initialize the database
        salaireRepository.saveAndFlush(salaire);

        int databaseSizeBeforeUpdate = salaireRepository.findAll().size();

        // Update the salaire
        Salaire updatedSalaire = salaireRepository.findById(salaire.getId()).get();
        // Disconnect from session so that the updates on updatedSalaire are not directly saved in db
        em.detach(updatedSalaire);
        updatedSalaire.valeur(UPDATED_VALEUR).date(UPDATED_DATE).personneId(UPDATED_PERSONNE_ID);
        SalaireDTO salaireDTO = salaireMapper.toDto(updatedSalaire);

        restSalaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Salaire in the database
        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeUpdate);
        Salaire testSalaire = salaireList.get(salaireList.size() - 1);
        assertThat(testSalaire.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testSalaire.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSalaire.getPersonneId()).isEqualTo(UPDATED_PERSONNE_ID);
    }

    @Test
    @Transactional
    void putNonExistingSalaire() throws Exception {
        int databaseSizeBeforeUpdate = salaireRepository.findAll().size();
        salaire.setId(count.incrementAndGet());

        // Create the Salaire
        SalaireDTO salaireDTO = salaireMapper.toDto(salaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salaire in the database
        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalaire() throws Exception {
        int databaseSizeBeforeUpdate = salaireRepository.findAll().size();
        salaire.setId(count.incrementAndGet());

        // Create the Salaire
        SalaireDTO salaireDTO = salaireMapper.toDto(salaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salaire in the database
        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalaire() throws Exception {
        int databaseSizeBeforeUpdate = salaireRepository.findAll().size();
        salaire.setId(count.incrementAndGet());

        // Create the Salaire
        SalaireDTO salaireDTO = salaireMapper.toDto(salaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaireMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salaire in the database
        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalaireWithPatch() throws Exception {
        // Initialize the database
        salaireRepository.saveAndFlush(salaire);

        int databaseSizeBeforeUpdate = salaireRepository.findAll().size();

        // Update the salaire using partial update
        Salaire partialUpdatedSalaire = new Salaire();
        partialUpdatedSalaire.setId(salaire.getId());

        partialUpdatedSalaire.valeur(UPDATED_VALEUR).date(UPDATED_DATE).personneId(UPDATED_PERSONNE_ID);

        restSalaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalaire))
            )
            .andExpect(status().isOk());

        // Validate the Salaire in the database
        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeUpdate);
        Salaire testSalaire = salaireList.get(salaireList.size() - 1);
        assertThat(testSalaire.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testSalaire.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSalaire.getPersonneId()).isEqualTo(UPDATED_PERSONNE_ID);
    }

    @Test
    @Transactional
    void fullUpdateSalaireWithPatch() throws Exception {
        // Initialize the database
        salaireRepository.saveAndFlush(salaire);

        int databaseSizeBeforeUpdate = salaireRepository.findAll().size();

        // Update the salaire using partial update
        Salaire partialUpdatedSalaire = new Salaire();
        partialUpdatedSalaire.setId(salaire.getId());

        partialUpdatedSalaire.valeur(UPDATED_VALEUR).date(UPDATED_DATE).personneId(UPDATED_PERSONNE_ID);

        restSalaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalaire))
            )
            .andExpect(status().isOk());

        // Validate the Salaire in the database
        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeUpdate);
        Salaire testSalaire = salaireList.get(salaireList.size() - 1);
        assertThat(testSalaire.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testSalaire.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSalaire.getPersonneId()).isEqualTo(UPDATED_PERSONNE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSalaire() throws Exception {
        int databaseSizeBeforeUpdate = salaireRepository.findAll().size();
        salaire.setId(count.incrementAndGet());

        // Create the Salaire
        SalaireDTO salaireDTO = salaireMapper.toDto(salaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salaireDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salaire in the database
        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalaire() throws Exception {
        int databaseSizeBeforeUpdate = salaireRepository.findAll().size();
        salaire.setId(count.incrementAndGet());

        // Create the Salaire
        SalaireDTO salaireDTO = salaireMapper.toDto(salaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salaire in the database
        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalaire() throws Exception {
        int databaseSizeBeforeUpdate = salaireRepository.findAll().size();
        salaire.setId(count.incrementAndGet());

        // Create the Salaire
        SalaireDTO salaireDTO = salaireMapper.toDto(salaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salaire in the database
        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalaire() throws Exception {
        // Initialize the database
        salaireRepository.saveAndFlush(salaire);

        int databaseSizeBeforeDelete = salaireRepository.findAll().size();

        // Delete the salaire
        restSalaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, salaire.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Salaire> salaireList = salaireRepository.findAll();
        assertThat(salaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
