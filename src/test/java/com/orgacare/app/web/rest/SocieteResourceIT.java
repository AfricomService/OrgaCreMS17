package com.orgacare.app.web.rest;

import static com.orgacare.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.orgacare.app.IntegrationTest;
import com.orgacare.app.domain.Societe;
import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.repository.SocieteRepository;
import com.orgacare.app.service.dto.SocieteDTO;
import com.orgacare.app.service.mapper.SocieteMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link SocieteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SocieteResourceIT {

    private static final String DEFAULT_RAISON_SOCIALE = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIALE = "BBBBBBBBBB";

    private static final String DEFAULT_ABREVIATION = "AAAAAAAAAA";
    private static final String UPDATED_ABREVIATION = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVITE = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITE = "BBBBBBBBBB";

    private static final String DEFAULT_FORME_JURIDIQUE = "AAAAAAAAAA";
    private static final String UPDATED_FORME_JURIDIQUE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_POSTALE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTALE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_FAX = "AAAAAAAAAA";
    private static final String UPDATED_FAX = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_SITE_INTERNET = "AAAAAAAAAA";
    private static final String UPDATED_SITE_INTERNET = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE_FISCALE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_FISCALE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGES_SITE_PRINCIPALE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGES_SITE_PRINCIPALE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGES_SITE_PRINCIPALE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGES_SITE_PRINCIPALE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_HOLDING = "AAAAAAAAAA";
    private static final String UPDATED_HOLDING = "BBBBBBBBBB";

    private static final Etat DEFAULT_ETAT = Etat.DRAFT;
    private static final Etat UPDATED_ETAT = Etat.ACTIF;

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_ACTIVATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ACTIVATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_CLOTURE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CLOTURE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_IMPORT_TEMPLATE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMPORT_TEMPLATE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMPORT_TEMPLATE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMPORT_TEMPLATE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CODE_SOCIETE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_SOCIETE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_ORGANIGRAMME = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ORGANIGRAMME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/societes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SocieteRepository societeRepository;

    @Autowired
    private SocieteMapper societeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocieteMockMvc;

    private Societe societe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Societe createEntity(EntityManager em) {
        Societe societe = new Societe()
            .raisonSociale(DEFAULT_RAISON_SOCIALE)
            .abreviation(DEFAULT_ABREVIATION)
            .activite(DEFAULT_ACTIVITE)
            .formeJuridique(DEFAULT_FORME_JURIDIQUE)
            .adresse(DEFAULT_ADRESSE)
            .codePostale(DEFAULT_CODE_POSTALE)
            .ville(DEFAULT_VILLE)
            .pays(DEFAULT_PAYS)
            .region(DEFAULT_REGION)
            .tel(DEFAULT_TEL)
            .fax(DEFAULT_FAX)
            .mail(DEFAULT_MAIL)
            .siteInternet(DEFAULT_SITE_INTERNET)
            .matriculeFiscale(DEFAULT_MATRICULE_FISCALE)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .imagesSitePrincipale(DEFAULT_IMAGES_SITE_PRINCIPALE)
            .imagesSitePrincipaleContentType(DEFAULT_IMAGES_SITE_PRINCIPALE_CONTENT_TYPE)
            .holding(DEFAULT_HOLDING)
            .etat(DEFAULT_ETAT)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateActivation(DEFAULT_DATE_ACTIVATION)
            .dateCloture(DEFAULT_DATE_CLOTURE)
            .importTemplate(DEFAULT_IMPORT_TEMPLATE)
            .importTemplateContentType(DEFAULT_IMPORT_TEMPLATE_CONTENT_TYPE)
            .codeSociete(DEFAULT_CODE_SOCIETE)
            .codeOrganigramme(DEFAULT_CODE_ORGANIGRAMME);
        return societe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Societe createUpdatedEntity(EntityManager em) {
        Societe societe = new Societe()
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .abreviation(UPDATED_ABREVIATION)
            .activite(UPDATED_ACTIVITE)
            .formeJuridique(UPDATED_FORME_JURIDIQUE)
            .adresse(UPDATED_ADRESSE)
            .codePostale(UPDATED_CODE_POSTALE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .region(UPDATED_REGION)
            .tel(UPDATED_TEL)
            .fax(UPDATED_FAX)
            .mail(UPDATED_MAIL)
            .siteInternet(UPDATED_SITE_INTERNET)
            .matriculeFiscale(UPDATED_MATRICULE_FISCALE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .imagesSitePrincipale(UPDATED_IMAGES_SITE_PRINCIPALE)
            .imagesSitePrincipaleContentType(UPDATED_IMAGES_SITE_PRINCIPALE_CONTENT_TYPE)
            .holding(UPDATED_HOLDING)
            .etat(UPDATED_ETAT)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateActivation(UPDATED_DATE_ACTIVATION)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .importTemplate(UPDATED_IMPORT_TEMPLATE)
            .importTemplateContentType(UPDATED_IMPORT_TEMPLATE_CONTENT_TYPE)
            .codeSociete(UPDATED_CODE_SOCIETE)
            .codeOrganigramme(UPDATED_CODE_ORGANIGRAMME);
        return societe;
    }

    @BeforeEach
    public void initTest() {
        societe = createEntity(em);
    }

    @Test
    @Transactional
    void createSociete() throws Exception {
        int databaseSizeBeforeCreate = societeRepository.findAll().size();
        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);
        restSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeCreate + 1);
        Societe testSociete = societeList.get(societeList.size() - 1);
        assertThat(testSociete.getRaisonSociale()).isEqualTo(DEFAULT_RAISON_SOCIALE);
        assertThat(testSociete.getAbreviation()).isEqualTo(DEFAULT_ABREVIATION);
        assertThat(testSociete.getActivite()).isEqualTo(DEFAULT_ACTIVITE);
        assertThat(testSociete.getFormeJuridique()).isEqualTo(DEFAULT_FORME_JURIDIQUE);
        assertThat(testSociete.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testSociete.getCodePostale()).isEqualTo(DEFAULT_CODE_POSTALE);
        assertThat(testSociete.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testSociete.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testSociete.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testSociete.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testSociete.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testSociete.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testSociete.getSiteInternet()).isEqualTo(DEFAULT_SITE_INTERNET);
        assertThat(testSociete.getMatriculeFiscale()).isEqualTo(DEFAULT_MATRICULE_FISCALE);
        assertThat(testSociete.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testSociete.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testSociete.getImagesSitePrincipale()).isEqualTo(DEFAULT_IMAGES_SITE_PRINCIPALE);
        assertThat(testSociete.getImagesSitePrincipaleContentType()).isEqualTo(DEFAULT_IMAGES_SITE_PRINCIPALE_CONTENT_TYPE);
        assertThat(testSociete.getHolding()).isEqualTo(DEFAULT_HOLDING);
        assertThat(testSociete.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testSociete.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testSociete.getDateActivation()).isEqualTo(DEFAULT_DATE_ACTIVATION);
        assertThat(testSociete.getDateCloture()).isEqualTo(DEFAULT_DATE_CLOTURE);
        assertThat(testSociete.getImportTemplate()).isEqualTo(DEFAULT_IMPORT_TEMPLATE);
        assertThat(testSociete.getImportTemplateContentType()).isEqualTo(DEFAULT_IMPORT_TEMPLATE_CONTENT_TYPE);
        assertThat(testSociete.getCodeSociete()).isEqualTo(DEFAULT_CODE_SOCIETE);
        assertThat(testSociete.getCodeOrganigramme()).isEqualTo(DEFAULT_CODE_ORGANIGRAMME);
    }

    @Test
    @Transactional
    void createSocieteWithExistingId() throws Exception {
        // Create the Societe with an existing ID
        societe.setId(1L);
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        int databaseSizeBeforeCreate = societeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRaisonSocialeIsRequired() throws Exception {
        int databaseSizeBeforeTest = societeRepository.findAll().size();
        // set the field null
        societe.setRaisonSociale(null);

        // Create the Societe, which fails.
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        restSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtatIsRequired() throws Exception {
        int databaseSizeBeforeTest = societeRepository.findAll().size();
        // set the field null
        societe.setEtat(null);

        // Create the Societe, which fails.
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        restSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeOrganigrammeIsRequired() throws Exception {
        int databaseSizeBeforeTest = societeRepository.findAll().size();
        // set the field null
        societe.setCodeOrganigramme(null);

        // Create the Societe, which fails.
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        restSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSocietes() throws Exception {
        // Initialize the database
        societeRepository.saveAndFlush(societe);

        // Get all the societeList
        restSocieteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(societe.getId().intValue())))
            .andExpect(jsonPath("$.[*].raisonSociale").value(hasItem(DEFAULT_RAISON_SOCIALE)))
            .andExpect(jsonPath("$.[*].abreviation").value(hasItem(DEFAULT_ABREVIATION)))
            .andExpect(jsonPath("$.[*].activite").value(hasItem(DEFAULT_ACTIVITE)))
            .andExpect(jsonPath("$.[*].formeJuridique").value(hasItem(DEFAULT_FORME_JURIDIQUE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].codePostale").value(hasItem(DEFAULT_CODE_POSTALE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].siteInternet").value(hasItem(DEFAULT_SITE_INTERNET)))
            .andExpect(jsonPath("$.[*].matriculeFiscale").value(hasItem(DEFAULT_MATRICULE_FISCALE)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].imagesSitePrincipaleContentType").value(hasItem(DEFAULT_IMAGES_SITE_PRINCIPALE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagesSitePrincipale").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGES_SITE_PRINCIPALE))))
            .andExpect(jsonPath("$.[*].holding").value(hasItem(DEFAULT_HOLDING)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))))
            .andExpect(jsonPath("$.[*].dateActivation").value(hasItem(sameInstant(DEFAULT_DATE_ACTIVATION))))
            .andExpect(jsonPath("$.[*].dateCloture").value(hasItem(sameInstant(DEFAULT_DATE_CLOTURE))))
            .andExpect(jsonPath("$.[*].importTemplateContentType").value(hasItem(DEFAULT_IMPORT_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].importTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMPORT_TEMPLATE))))
            .andExpect(jsonPath("$.[*].codeSociete").value(hasItem(DEFAULT_CODE_SOCIETE)))
            .andExpect(jsonPath("$.[*].codeOrganigramme").value(hasItem(DEFAULT_CODE_ORGANIGRAMME)));
    }

    @Test
    @Transactional
    void getSociete() throws Exception {
        // Initialize the database
        societeRepository.saveAndFlush(societe);

        // Get the societe
        restSocieteMockMvc
            .perform(get(ENTITY_API_URL_ID, societe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(societe.getId().intValue()))
            .andExpect(jsonPath("$.raisonSociale").value(DEFAULT_RAISON_SOCIALE))
            .andExpect(jsonPath("$.abreviation").value(DEFAULT_ABREVIATION))
            .andExpect(jsonPath("$.activite").value(DEFAULT_ACTIVITE))
            .andExpect(jsonPath("$.formeJuridique").value(DEFAULT_FORME_JURIDIQUE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.codePostale").value(DEFAULT_CODE_POSTALE))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.fax").value(DEFAULT_FAX))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL))
            .andExpect(jsonPath("$.siteInternet").value(DEFAULT_SITE_INTERNET))
            .andExpect(jsonPath("$.matriculeFiscale").value(DEFAULT_MATRICULE_FISCALE))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.imagesSitePrincipaleContentType").value(DEFAULT_IMAGES_SITE_PRINCIPALE_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagesSitePrincipale").value(Base64Utils.encodeToString(DEFAULT_IMAGES_SITE_PRINCIPALE)))
            .andExpect(jsonPath("$.holding").value(DEFAULT_HOLDING))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)))
            .andExpect(jsonPath("$.dateActivation").value(sameInstant(DEFAULT_DATE_ACTIVATION)))
            .andExpect(jsonPath("$.dateCloture").value(sameInstant(DEFAULT_DATE_CLOTURE)))
            .andExpect(jsonPath("$.importTemplateContentType").value(DEFAULT_IMPORT_TEMPLATE_CONTENT_TYPE))
            .andExpect(jsonPath("$.importTemplate").value(Base64Utils.encodeToString(DEFAULT_IMPORT_TEMPLATE)))
            .andExpect(jsonPath("$.codeSociete").value(DEFAULT_CODE_SOCIETE))
            .andExpect(jsonPath("$.codeOrganigramme").value(DEFAULT_CODE_ORGANIGRAMME));
    }

    @Test
    @Transactional
    void getNonExistingSociete() throws Exception {
        // Get the societe
        restSocieteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSociete() throws Exception {
        // Initialize the database
        societeRepository.saveAndFlush(societe);

        int databaseSizeBeforeUpdate = societeRepository.findAll().size();

        // Update the societe
        Societe updatedSociete = societeRepository.findById(societe.getId()).get();
        // Disconnect from session so that the updates on updatedSociete are not directly saved in db
        em.detach(updatedSociete);
        updatedSociete
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .abreviation(UPDATED_ABREVIATION)
            .activite(UPDATED_ACTIVITE)
            .formeJuridique(UPDATED_FORME_JURIDIQUE)
            .adresse(UPDATED_ADRESSE)
            .codePostale(UPDATED_CODE_POSTALE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .region(UPDATED_REGION)
            .tel(UPDATED_TEL)
            .fax(UPDATED_FAX)
            .mail(UPDATED_MAIL)
            .siteInternet(UPDATED_SITE_INTERNET)
            .matriculeFiscale(UPDATED_MATRICULE_FISCALE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .imagesSitePrincipale(UPDATED_IMAGES_SITE_PRINCIPALE)
            .imagesSitePrincipaleContentType(UPDATED_IMAGES_SITE_PRINCIPALE_CONTENT_TYPE)
            .holding(UPDATED_HOLDING)
            .etat(UPDATED_ETAT)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateActivation(UPDATED_DATE_ACTIVATION)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .importTemplate(UPDATED_IMPORT_TEMPLATE)
            .importTemplateContentType(UPDATED_IMPORT_TEMPLATE_CONTENT_TYPE)
            .codeSociete(UPDATED_CODE_SOCIETE)
            .codeOrganigramme(UPDATED_CODE_ORGANIGRAMME);
        SocieteDTO societeDTO = societeMapper.toDto(updatedSociete);

        restSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, societeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
        Societe testSociete = societeList.get(societeList.size() - 1);
        assertThat(testSociete.getRaisonSociale()).isEqualTo(UPDATED_RAISON_SOCIALE);
        assertThat(testSociete.getAbreviation()).isEqualTo(UPDATED_ABREVIATION);
        assertThat(testSociete.getActivite()).isEqualTo(UPDATED_ACTIVITE);
        assertThat(testSociete.getFormeJuridique()).isEqualTo(UPDATED_FORME_JURIDIQUE);
        assertThat(testSociete.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testSociete.getCodePostale()).isEqualTo(UPDATED_CODE_POSTALE);
        assertThat(testSociete.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testSociete.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testSociete.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testSociete.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testSociete.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testSociete.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testSociete.getSiteInternet()).isEqualTo(UPDATED_SITE_INTERNET);
        assertThat(testSociete.getMatriculeFiscale()).isEqualTo(UPDATED_MATRICULE_FISCALE);
        assertThat(testSociete.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testSociete.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testSociete.getImagesSitePrincipale()).isEqualTo(UPDATED_IMAGES_SITE_PRINCIPALE);
        assertThat(testSociete.getImagesSitePrincipaleContentType()).isEqualTo(UPDATED_IMAGES_SITE_PRINCIPALE_CONTENT_TYPE);
        assertThat(testSociete.getHolding()).isEqualTo(UPDATED_HOLDING);
        assertThat(testSociete.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testSociete.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testSociete.getDateActivation()).isEqualTo(UPDATED_DATE_ACTIVATION);
        assertThat(testSociete.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testSociete.getImportTemplate()).isEqualTo(UPDATED_IMPORT_TEMPLATE);
        assertThat(testSociete.getImportTemplateContentType()).isEqualTo(UPDATED_IMPORT_TEMPLATE_CONTENT_TYPE);
        assertThat(testSociete.getCodeSociete()).isEqualTo(UPDATED_CODE_SOCIETE);
        assertThat(testSociete.getCodeOrganigramme()).isEqualTo(UPDATED_CODE_ORGANIGRAMME);
    }

    @Test
    @Transactional
    void putNonExistingSociete() throws Exception {
        int databaseSizeBeforeUpdate = societeRepository.findAll().size();
        societe.setId(count.incrementAndGet());

        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, societeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSociete() throws Exception {
        int databaseSizeBeforeUpdate = societeRepository.findAll().size();
        societe.setId(count.incrementAndGet());

        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSociete() throws Exception {
        int databaseSizeBeforeUpdate = societeRepository.findAll().size();
        societe.setId(count.incrementAndGet());

        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocieteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSocieteWithPatch() throws Exception {
        // Initialize the database
        societeRepository.saveAndFlush(societe);

        int databaseSizeBeforeUpdate = societeRepository.findAll().size();

        // Update the societe using partial update
        Societe partialUpdatedSociete = new Societe();
        partialUpdatedSociete.setId(societe.getId());

        partialUpdatedSociete
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .abreviation(UPDATED_ABREVIATION)
            .activite(UPDATED_ACTIVITE)
            .adresse(UPDATED_ADRESSE)
            .codePostale(UPDATED_CODE_POSTALE)
            .fax(UPDATED_FAX)
            .siteInternet(UPDATED_SITE_INTERNET)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .importTemplate(UPDATED_IMPORT_TEMPLATE)
            .importTemplateContentType(UPDATED_IMPORT_TEMPLATE_CONTENT_TYPE)
            .codeSociete(UPDATED_CODE_SOCIETE);

        restSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSociete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSociete))
            )
            .andExpect(status().isOk());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
        Societe testSociete = societeList.get(societeList.size() - 1);
        assertThat(testSociete.getRaisonSociale()).isEqualTo(UPDATED_RAISON_SOCIALE);
        assertThat(testSociete.getAbreviation()).isEqualTo(UPDATED_ABREVIATION);
        assertThat(testSociete.getActivite()).isEqualTo(UPDATED_ACTIVITE);
        assertThat(testSociete.getFormeJuridique()).isEqualTo(DEFAULT_FORME_JURIDIQUE);
        assertThat(testSociete.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testSociete.getCodePostale()).isEqualTo(UPDATED_CODE_POSTALE);
        assertThat(testSociete.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testSociete.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testSociete.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testSociete.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testSociete.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testSociete.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testSociete.getSiteInternet()).isEqualTo(UPDATED_SITE_INTERNET);
        assertThat(testSociete.getMatriculeFiscale()).isEqualTo(DEFAULT_MATRICULE_FISCALE);
        assertThat(testSociete.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testSociete.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testSociete.getImagesSitePrincipale()).isEqualTo(DEFAULT_IMAGES_SITE_PRINCIPALE);
        assertThat(testSociete.getImagesSitePrincipaleContentType()).isEqualTo(DEFAULT_IMAGES_SITE_PRINCIPALE_CONTENT_TYPE);
        assertThat(testSociete.getHolding()).isEqualTo(DEFAULT_HOLDING);
        assertThat(testSociete.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testSociete.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testSociete.getDateActivation()).isEqualTo(DEFAULT_DATE_ACTIVATION);
        assertThat(testSociete.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testSociete.getImportTemplate()).isEqualTo(UPDATED_IMPORT_TEMPLATE);
        assertThat(testSociete.getImportTemplateContentType()).isEqualTo(UPDATED_IMPORT_TEMPLATE_CONTENT_TYPE);
        assertThat(testSociete.getCodeSociete()).isEqualTo(UPDATED_CODE_SOCIETE);
        assertThat(testSociete.getCodeOrganigramme()).isEqualTo(DEFAULT_CODE_ORGANIGRAMME);
    }

    @Test
    @Transactional
    void fullUpdateSocieteWithPatch() throws Exception {
        // Initialize the database
        societeRepository.saveAndFlush(societe);

        int databaseSizeBeforeUpdate = societeRepository.findAll().size();

        // Update the societe using partial update
        Societe partialUpdatedSociete = new Societe();
        partialUpdatedSociete.setId(societe.getId());

        partialUpdatedSociete
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .abreviation(UPDATED_ABREVIATION)
            .activite(UPDATED_ACTIVITE)
            .formeJuridique(UPDATED_FORME_JURIDIQUE)
            .adresse(UPDATED_ADRESSE)
            .codePostale(UPDATED_CODE_POSTALE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .region(UPDATED_REGION)
            .tel(UPDATED_TEL)
            .fax(UPDATED_FAX)
            .mail(UPDATED_MAIL)
            .siteInternet(UPDATED_SITE_INTERNET)
            .matriculeFiscale(UPDATED_MATRICULE_FISCALE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .imagesSitePrincipale(UPDATED_IMAGES_SITE_PRINCIPALE)
            .imagesSitePrincipaleContentType(UPDATED_IMAGES_SITE_PRINCIPALE_CONTENT_TYPE)
            .holding(UPDATED_HOLDING)
            .etat(UPDATED_ETAT)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateActivation(UPDATED_DATE_ACTIVATION)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .importTemplate(UPDATED_IMPORT_TEMPLATE)
            .importTemplateContentType(UPDATED_IMPORT_TEMPLATE_CONTENT_TYPE)
            .codeSociete(UPDATED_CODE_SOCIETE)
            .codeOrganigramme(UPDATED_CODE_ORGANIGRAMME);

        restSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSociete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSociete))
            )
            .andExpect(status().isOk());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
        Societe testSociete = societeList.get(societeList.size() - 1);
        assertThat(testSociete.getRaisonSociale()).isEqualTo(UPDATED_RAISON_SOCIALE);
        assertThat(testSociete.getAbreviation()).isEqualTo(UPDATED_ABREVIATION);
        assertThat(testSociete.getActivite()).isEqualTo(UPDATED_ACTIVITE);
        assertThat(testSociete.getFormeJuridique()).isEqualTo(UPDATED_FORME_JURIDIQUE);
        assertThat(testSociete.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testSociete.getCodePostale()).isEqualTo(UPDATED_CODE_POSTALE);
        assertThat(testSociete.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testSociete.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testSociete.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testSociete.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testSociete.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testSociete.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testSociete.getSiteInternet()).isEqualTo(UPDATED_SITE_INTERNET);
        assertThat(testSociete.getMatriculeFiscale()).isEqualTo(UPDATED_MATRICULE_FISCALE);
        assertThat(testSociete.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testSociete.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testSociete.getImagesSitePrincipale()).isEqualTo(UPDATED_IMAGES_SITE_PRINCIPALE);
        assertThat(testSociete.getImagesSitePrincipaleContentType()).isEqualTo(UPDATED_IMAGES_SITE_PRINCIPALE_CONTENT_TYPE);
        assertThat(testSociete.getHolding()).isEqualTo(UPDATED_HOLDING);
        assertThat(testSociete.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testSociete.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testSociete.getDateActivation()).isEqualTo(UPDATED_DATE_ACTIVATION);
        assertThat(testSociete.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testSociete.getImportTemplate()).isEqualTo(UPDATED_IMPORT_TEMPLATE);
        assertThat(testSociete.getImportTemplateContentType()).isEqualTo(UPDATED_IMPORT_TEMPLATE_CONTENT_TYPE);
        assertThat(testSociete.getCodeSociete()).isEqualTo(UPDATED_CODE_SOCIETE);
        assertThat(testSociete.getCodeOrganigramme()).isEqualTo(UPDATED_CODE_ORGANIGRAMME);
    }

    @Test
    @Transactional
    void patchNonExistingSociete() throws Exception {
        int databaseSizeBeforeUpdate = societeRepository.findAll().size();
        societe.setId(count.incrementAndGet());

        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, societeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSociete() throws Exception {
        int databaseSizeBeforeUpdate = societeRepository.findAll().size();
        societe.setId(count.incrementAndGet());

        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSociete() throws Exception {
        int databaseSizeBeforeUpdate = societeRepository.findAll().size();
        societe.setId(count.incrementAndGet());

        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSociete() throws Exception {
        // Initialize the database
        societeRepository.saveAndFlush(societe);

        int databaseSizeBeforeDelete = societeRepository.findAll().size();

        // Delete the societe
        restSocieteMockMvc
            .perform(delete(ENTITY_API_URL_ID, societe.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
