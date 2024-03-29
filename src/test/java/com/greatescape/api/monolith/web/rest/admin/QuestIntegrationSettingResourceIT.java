package com.greatescape.api.monolith.web.rest.admin;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting.AbstractSettings;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting.BookForm;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting.MirKvestov;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.security.AuthoritiesConstants;
import com.greatescape.api.monolith.service.QuestIntegrationSettingQueryService;
import com.greatescape.api.monolith.service.QuestIntegrationSettingService;
import com.greatescape.api.monolith.service.dto.QuestIntegrationSettingDTO;
import com.greatescape.api.monolith.service.mapper.QuestIntegrationSettingMapper;
import com.greatescape.api.monolith.web.rest.TestUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.EntityManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;
/**
 * Integration tests for the {@link QuestIntegrationSettingResource} REST controller.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
@AutoConfigureMockMvc
@WithMockUser(username="admin",authorities=AuthoritiesConstants.ADMIN)
public class QuestIntegrationSettingResourceIT {

    private static final QuestIntegrationType DEFAULT_TYPE = QuestIntegrationType.MIR_KVESTOV;
    private static final QuestIntegrationType UPDATED_TYPE = QuestIntegrationType.BOOK_FORM;

    private static final AbstractSettings DEFAULT_SETTINGS = new BookForm();
    private static final AbstractSettings UPDATED_SETTINGS = new MirKvestov();
    private static final Map<String, String> DEFAULT_SETTINGS_EXPECTED = new HashMap<>(){{
            put("integrationType", QuestIntegrationType.BOOK_FORM.toString());
            put("serviceId", null);
            put("widgetId", null);
        }};

    @Autowired
    private QuestIntegrationSettingRepository questIntegrationSettingRepository;

    @Autowired
    private QuestIntegrationSettingMapper questIntegrationSettingMapper;

    @Autowired
    private QuestIntegrationSettingService questIntegrationSettingService;

    @Autowired
    private QuestIntegrationSettingQueryService questIntegrationSettingQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestIntegrationSettingMockMvc;

    private QuestIntegrationSetting questIntegrationSetting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestIntegrationSetting createEntity(EntityManager em) {
        QuestIntegrationSetting questIntegrationSetting = new QuestIntegrationSetting()
            .setType(DEFAULT_TYPE)
            .setSettings(DEFAULT_SETTINGS);
        // Add required entity
        Quest quest;
        if (TestUtil.findAll(em, Quest.class).isEmpty()) {
            quest = QuestResourceIT.createEntity(em);
            em.persist(quest);
            em.flush();
        } else {
            quest = TestUtil.findAll(em, Quest.class).get(0);
        }
        questIntegrationSetting.setQuest(quest);
        return questIntegrationSetting;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestIntegrationSetting createUpdatedEntity(EntityManager em) {
        QuestIntegrationSetting questIntegrationSetting = new QuestIntegrationSetting()
            .setType(UPDATED_TYPE)
            .setSettings(UPDATED_SETTINGS);
        // Add required entity
        Quest quest;
        if (TestUtil.findAll(em, Quest.class).isEmpty()) {
            quest = QuestResourceIT.createUpdatedEntity(em);
            em.persist(quest);
            em.flush();
        } else {
            quest = TestUtil.findAll(em, Quest.class).get(0);
        }
        questIntegrationSetting.setQuest(quest);
        return questIntegrationSetting;
    }

    @BeforeEach
    public void initTest() {
        questIntegrationSetting = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestIntegrationSetting() throws Exception {
        int databaseSizeBeforeCreate = questIntegrationSettingRepository.findAll().size();
        // Create the QuestIntegrationSetting
        QuestIntegrationSettingDTO questIntegrationSettingDTO = questIntegrationSettingMapper.toDto(questIntegrationSetting);
        restQuestIntegrationSettingMockMvc.perform(post("/admin-api/quest-integration-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questIntegrationSettingDTO)))
            .andExpect(status().isCreated());

        // Validate the QuestIntegrationSetting in the database
        List<QuestIntegrationSetting> questIntegrationSettingList = questIntegrationSettingRepository.findAll();
        assertThat(questIntegrationSettingList).hasSize(databaseSizeBeforeCreate + 1);
        QuestIntegrationSetting testQuestIntegrationSetting = questIntegrationSettingList.get(questIntegrationSettingList.size() - 1);
        assertThat(testQuestIntegrationSetting.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testQuestIntegrationSetting.getSettings()).isEqualTo(DEFAULT_SETTINGS);
    }

    @Test
    @Transactional
    public void createQuestIntegrationSettingWithExistingId() throws Exception {
        final QuestIntegrationSetting questIntegrationSetting = questIntegrationSettingRepository.save(createEntity(em));
        int databaseSizeBeforeCreate = questIntegrationSettingRepository.findAll().size();

        // Create the QuestIntegrationSetting with an existing ID
        this.questIntegrationSetting.setId(questIntegrationSetting.getId());
        QuestIntegrationSettingDTO questIntegrationSettingDTO = questIntegrationSettingMapper.toDto(this.questIntegrationSetting);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestIntegrationSettingMockMvc.perform(post("/admin-api/quest-integration-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questIntegrationSettingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestIntegrationSetting in the database
        List<QuestIntegrationSetting> questIntegrationSettingList = questIntegrationSettingRepository.findAll();
        assertThat(questIntegrationSettingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = questIntegrationSettingRepository.findAll().size();
        // set the field null
        questIntegrationSetting.setType(null);

        // Create the QuestIntegrationSetting, which fails.
        QuestIntegrationSettingDTO questIntegrationSettingDTO = questIntegrationSettingMapper.toDto(questIntegrationSetting);


        restQuestIntegrationSettingMockMvc.perform(post("/admin-api/quest-integration-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questIntegrationSettingDTO)))
            .andExpect(status().isBadRequest());

        List<QuestIntegrationSetting> questIntegrationSettingList = questIntegrationSettingRepository.findAll();
        assertThat(questIntegrationSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuestIntegrationSettings() throws Exception {
        // Initialize the database
        questIntegrationSettingRepository.saveAndFlush(questIntegrationSetting);

        // Get all the questIntegrationSettingList
        restQuestIntegrationSettingMockMvc.perform(get("/admin-api/quest-integration-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questIntegrationSetting.getId().toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].settings").value(hasItem(DEFAULT_SETTINGS_EXPECTED)));
    }

    @Test
    @Transactional
    public void getQuestIntegrationSetting() throws Exception {
        // Initialize the database
        questIntegrationSettingRepository.saveAndFlush(questIntegrationSetting);

        // Get the questIntegrationSetting
        restQuestIntegrationSettingMockMvc.perform(get("/admin-api/quest-integration-settings/{id}", questIntegrationSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questIntegrationSetting.getId().toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.settings").value(is(DEFAULT_SETTINGS_EXPECTED)));
    }


    @Test
    @Transactional
    public void getQuestIntegrationSettingsByIdFiltering() throws Exception {
        // Initialize the database
        questIntegrationSettingRepository.saveAndFlush(questIntegrationSetting);

        UUID id = questIntegrationSetting.getId();

        defaultQuestIntegrationSettingShouldBeFound("id.equals=" + id);
        defaultQuestIntegrationSettingShouldNotBeFound("id.notEquals=" + id);
    }


    @Test
    @Transactional
    public void getAllQuestIntegrationSettingsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        questIntegrationSettingRepository.saveAndFlush(questIntegrationSetting);

        // Get all the questIntegrationSettingList where type equals to DEFAULT_TYPE
        defaultQuestIntegrationSettingShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the questIntegrationSettingList where type equals to UPDATED_TYPE
        defaultQuestIntegrationSettingShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuestIntegrationSettingsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questIntegrationSettingRepository.saveAndFlush(questIntegrationSetting);

        // Get all the questIntegrationSettingList where type not equals to DEFAULT_TYPE
        defaultQuestIntegrationSettingShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the questIntegrationSettingList where type not equals to UPDATED_TYPE
        defaultQuestIntegrationSettingShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuestIntegrationSettingsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        questIntegrationSettingRepository.saveAndFlush(questIntegrationSetting);

        // Get all the questIntegrationSettingList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultQuestIntegrationSettingShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the questIntegrationSettingList where type equals to UPDATED_TYPE
        defaultQuestIntegrationSettingShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuestIntegrationSettingsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        questIntegrationSettingRepository.saveAndFlush(questIntegrationSetting);

        // Get all the questIntegrationSettingList where type is not null
        defaultQuestIntegrationSettingShouldBeFound("type.specified=true");

        // Get all the questIntegrationSettingList where type is null
        defaultQuestIntegrationSettingShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestIntegrationSettingsByQuestIsEqualToSomething() throws Exception {
        // Get already existing entity
        Quest quest = questIntegrationSetting.getQuest();
        questIntegrationSettingRepository.saveAndFlush(questIntegrationSetting);
        UUID questId = quest.getId();

        // Get all the questIntegrationSettingList where quest equals to questId
        defaultQuestIntegrationSettingShouldBeFound("questId.equals=" + questId);

        // Get all the questIntegrationSettingList where quest equals to questId + 1
        defaultQuestIntegrationSettingShouldNotBeFound("questId.equals=" + UUID.fromString("750591bf-7d50-4275-a68f-903606a27d43"));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestIntegrationSettingShouldBeFound(String filter) throws Exception {
        restQuestIntegrationSettingMockMvc.perform(get("/admin-api/quest-integration-settings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questIntegrationSetting.getId().toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].settings").value(hasItem(DEFAULT_SETTINGS_EXPECTED)));

        // Check, that the count call also returns 1
        restQuestIntegrationSettingMockMvc.perform(get("/admin-api/quest-integration-settings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestIntegrationSettingShouldNotBeFound(String filter) throws Exception {
        restQuestIntegrationSettingMockMvc.perform(get("/admin-api/quest-integration-settings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestIntegrationSettingMockMvc.perform(get("/admin-api/quest-integration-settings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingQuestIntegrationSetting() throws Exception {
        // Get the questIntegrationSetting
        restQuestIntegrationSettingMockMvc.perform(
            get(
                "/admin-api/quest-integration-settings/{id}",
                UUID.fromString("cce859ac-5d36-4cba-8ee0-8c05fd8ae994")
            )
        ).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestIntegrationSetting() throws Exception {
        // Initialize the database
        questIntegrationSettingRepository.saveAndFlush(questIntegrationSetting);

        int databaseSizeBeforeUpdate = questIntegrationSettingRepository.findAll().size();

        // Update the questIntegrationSetting
        QuestIntegrationSetting updatedQuestIntegrationSetting = questIntegrationSettingRepository.findById(questIntegrationSetting.getId()).get();
        // Disconnect from session so that the updates on updatedQuestIntegrationSetting are not directly saved in db
        em.detach(updatedQuestIntegrationSetting);
        updatedQuestIntegrationSetting
            .setType(UPDATED_TYPE)
            .setSettings(UPDATED_SETTINGS);
        QuestIntegrationSettingDTO questIntegrationSettingDTO = questIntegrationSettingMapper.toDto(updatedQuestIntegrationSetting);

        restQuestIntegrationSettingMockMvc.perform(put("/admin-api/quest-integration-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questIntegrationSettingDTO)))
            .andExpect(status().isOk());

        // Validate the QuestIntegrationSetting in the database
        List<QuestIntegrationSetting> questIntegrationSettingList = questIntegrationSettingRepository.findAll();
        assertThat(questIntegrationSettingList).hasSize(databaseSizeBeforeUpdate);
        QuestIntegrationSetting testQuestIntegrationSetting = questIntegrationSettingList.get(questIntegrationSettingList.size() - 1);
        assertThat(testQuestIntegrationSetting.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testQuestIntegrationSetting.getSettings()).isEqualTo(UPDATED_SETTINGS);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestIntegrationSetting() throws Exception {
        int databaseSizeBeforeUpdate = questIntegrationSettingRepository.findAll().size();

        // Create the QuestIntegrationSetting
        QuestIntegrationSettingDTO questIntegrationSettingDTO = questIntegrationSettingMapper.toDto(questIntegrationSetting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestIntegrationSettingMockMvc.perform(put("/admin-api/quest-integration-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questIntegrationSettingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestIntegrationSetting in the database
        List<QuestIntegrationSetting> questIntegrationSettingList = questIntegrationSettingRepository.findAll();
        assertThat(questIntegrationSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuestIntegrationSetting() throws Exception {
        // Initialize the database
        questIntegrationSettingRepository.saveAndFlush(questIntegrationSetting);

        int databaseSizeBeforeDelete = questIntegrationSettingRepository.findAll().size();

        // Delete the questIntegrationSetting
        restQuestIntegrationSettingMockMvc.perform(delete("/admin-api/quest-integration-settings/{id}", questIntegrationSetting.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestIntegrationSetting> questIntegrationSettingList = questIntegrationSettingRepository.findAll();

        // Due to commented delete service
        // assertThat(questIntegrationSettingList).hasSize(databaseSizeBeforeDelete - 1);
        assertThat(questIntegrationSettingList).hasSize(databaseSizeBeforeDelete);
    }
}
