package com.orgacare.app.web.rest;

import static com.orgacare.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.Formation;
import com.orgacare.app.repository.FormationRepository;
import com.orgacare.app.service.dto.FormationDTO;
import com.orgacare.app.service.mapper.FormationMapper;
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
 * Integration tests for the {@link FormationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormationResourceIT {

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final Double DEFAULT_NOTE = 1D;
    private static final Double UPDATED_NOTE = 2D;

    private static final ZonedDateTime DEFAULT_DATE_OBTENTION_CERTIFICAT = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_DATE_OBTENTION_CERTIFICAT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_EXPIRATION_CERTIFICAT = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_DATE_EXPIRATION_CERTIFICAT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_NOTIFICATION_ENVOYEE = false;
    private static final Boolean UPDATED_NOTIFICATION_ENVOYEE = true;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Long DEFAULT_SESSION_ID = 1L;
    private static final Long UPDATED_SESSION_ID = 2L;

    private static final Long DEFAULT_PERSONNE_ID = 1L;
    private static final Long UPDATED_PERSONNE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/formations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationMapper formationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationMockMvc;

    private Formation formation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createEntity(EntityManager em) {
        Formation formation = new Formation()
            .statut(DEFAULT_STATUT)
            .etat(DEFAULT_ETAT)
            .note(DEFAULT_NOTE)
            .dateObtentionCertificat(DEFAULT_DATE_OBTENTION_CERTIFICAT)
            .dateExpirationCertificat(DEFAULT_DATE_EXPIRATION_CERTIFICAT)
            .notificationEnvoyee(DEFAULT_NOTIFICATION_ENVOYEE)
            .commentaire(DEFAULT_COMMENTAIRE)
            .sessionId(DEFAULT_SESSION_ID)
            .personneId(DEFAULT_PERSONNE_ID);
        return formation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createUpdatedEntity(EntityManager em) {
        Formation formation = new Formation()
            .statut(UPDATED_STATUT)
            .etat(UPDATED_ETAT)
            .note(UPDATED_NOTE)
            .dateObtentionCertificat(UPDATED_DATE_OBTENTION_CERTIFICAT)
            .dateExpirationCertificat(UPDATED_DATE_EXPIRATION_CERTIFICAT)
            .notificationEnvoyee(UPDATED_NOTIFICATION_ENVOYEE)
            .commentaire(UPDATED_COMMENTAIRE)
            .sessionId(UPDATED_SESSION_ID)
            .personneId(UPDATED_PERSONNE_ID);
        return formation;
    }

    @BeforeEach
    public void initTest() {
        formation = createEntity(em);
    }

    @Test
    @Transactional
    void createFormation() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();
        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);
        restFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate + 1);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testFormation.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testFormation.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testFormation.getDateObtentionCertificat()).isEqualTo(DEFAULT_DATE_OBTENTION_CERTIFICAT);
        assertThat(testFormation.getDateExpirationCertificat()).isEqualTo(DEFAULT_DATE_EXPIRATION_CERTIFICAT);
        assertThat(testFormation.getNotificationEnvoyee()).isEqualTo(DEFAULT_NOTIFICATION_ENVOYEE);
        assertThat(testFormation.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testFormation.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
        assertThat(testFormation.getPersonneId()).isEqualTo(DEFAULT_PERSONNE_ID);
    }

    @Test
    @Transactional
    void createFormationWithExistingId() throws Exception {
        // Create the Formation with an existing ID
        formation.setId(1L);
        FormationDTO formationDTO = formationMapper.toDto(formation);

        int databaseSizeBeforeCreate = formationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormations() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList
        restFormationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateObtentionCertificat").value(hasItem(sameInstant(DEFAULT_DATE_OBTENTION_CERTIFICAT))))
            .andExpect(jsonPath("$.[*].dateExpirationCertificat").value(hasItem(sameInstant(DEFAULT_DATE_EXPIRATION_CERTIFICAT))))
            .andExpect(jsonPath("$.[*].notificationEnvoyee").value(hasItem(DEFAULT_NOTIFICATION_ENVOYEE.booleanValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].sessionId").value(hasItem(DEFAULT_SESSION_ID.intValue())))
            .andExpect(jsonPath("$.[*].personneId").value(hasItem(DEFAULT_PERSONNE_ID.intValue())));
    }

    @Test
    @Transactional
    void getFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get the formation
        restFormationMockMvc
            .perform(get(ENTITY_API_URL_ID, formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formation.getId().intValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.doubleValue()))
            .andExpect(jsonPath("$.dateObtentionCertificat").value(sameInstant(DEFAULT_DATE_OBTENTION_CERTIFICAT)))
            .andExpect(jsonPath("$.dateExpirationCertificat").value(sameInstant(DEFAULT_DATE_EXPIRATION_CERTIFICAT)))
            .andExpect(jsonPath("$.notificationEnvoyee").value(DEFAULT_NOTIFICATION_ENVOYEE.booleanValue()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.sessionId").value(DEFAULT_SESSION_ID.intValue()))
            .andExpect(jsonPath("$.personneId").value(DEFAULT_PERSONNE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFormation() throws Exception {
        // Get the formation
        restFormationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation
        Formation updatedFormation = formationRepository.findById(formation.getId()).get();
        // Disconnect from session so that the updates on updatedFormation are not directly saved in db
        em.detach(updatedFormation);
        updatedFormation
            .statut(UPDATED_STATUT)
            .etat(UPDATED_ETAT)
            .note(UPDATED_NOTE)
            .dateObtentionCertificat(UPDATED_DATE_OBTENTION_CERTIFICAT)
            .dateExpirationCertificat(UPDATED_DATE_EXPIRATION_CERTIFICAT)
            .notificationEnvoyee(UPDATED_NOTIFICATION_ENVOYEE)
            .commentaire(UPDATED_COMMENTAIRE)
            .sessionId(UPDATED_SESSION_ID)
            .personneId(UPDATED_PERSONNE_ID);
        FormationDTO formationDTO = formationMapper.toDto(updatedFormation);

        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testFormation.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testFormation.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testFormation.getDateObtentionCertificat()).isEqualTo(UPDATED_DATE_OBTENTION_CERTIFICAT);
        assertThat(testFormation.getDateExpirationCertificat()).isEqualTo(UPDATED_DATE_EXPIRATION_CERTIFICAT);
        assertThat(testFormation.getNotificationEnvoyee()).isEqualTo(UPDATED_NOTIFICATION_ENVOYEE);
        assertThat(testFormation.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testFormation.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testFormation.getPersonneId()).isEqualTo(UPDATED_PERSONNE_ID);
    }

    @Test
    @Transactional
    void putNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormationWithPatch() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation using partial update
        Formation partialUpdatedFormation = new Formation();
        partialUpdatedFormation.setId(formation.getId());

        partialUpdatedFormation.dateExpirationCertificat(UPDATED_DATE_EXPIRATION_CERTIFICAT).personneId(UPDATED_PERSONNE_ID);

        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testFormation.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testFormation.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testFormation.getDateObtentionCertificat()).isEqualTo(DEFAULT_DATE_OBTENTION_CERTIFICAT);
        assertThat(testFormation.getDateExpirationCertificat()).isEqualTo(UPDATED_DATE_EXPIRATION_CERTIFICAT);
        assertThat(testFormation.getNotificationEnvoyee()).isEqualTo(DEFAULT_NOTIFICATION_ENVOYEE);
        assertThat(testFormation.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testFormation.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
        assertThat(testFormation.getPersonneId()).isEqualTo(UPDATED_PERSONNE_ID);
    }

    @Test
    @Transactional
    void fullUpdateFormationWithPatch() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation using partial update
        Formation partialUpdatedFormation = new Formation();
        partialUpdatedFormation.setId(formation.getId());

        partialUpdatedFormation
            .statut(UPDATED_STATUT)
            .etat(UPDATED_ETAT)
            .note(UPDATED_NOTE)
            .dateObtentionCertificat(UPDATED_DATE_OBTENTION_CERTIFICAT)
            .dateExpirationCertificat(UPDATED_DATE_EXPIRATION_CERTIFICAT)
            .notificationEnvoyee(UPDATED_NOTIFICATION_ENVOYEE)
            .commentaire(UPDATED_COMMENTAIRE)
            .sessionId(UPDATED_SESSION_ID)
            .personneId(UPDATED_PERSONNE_ID);

        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testFormation.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testFormation.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testFormation.getDateObtentionCertificat()).isEqualTo(UPDATED_DATE_OBTENTION_CERTIFICAT);
        assertThat(testFormation.getDateExpirationCertificat()).isEqualTo(UPDATED_DATE_EXPIRATION_CERTIFICAT);
        assertThat(testFormation.getNotificationEnvoyee()).isEqualTo(UPDATED_NOTIFICATION_ENVOYEE);
        assertThat(testFormation.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testFormation.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testFormation.getPersonneId()).isEqualTo(UPDATED_PERSONNE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeDelete = formationRepository.findAll().size();

        // Delete the formation
        restFormationMockMvc
            .perform(delete(ENTITY_API_URL_ID, formation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
