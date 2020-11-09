package com.greatescape.api.monolith.web.rest;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.service.QuestIntegrationSettingQueryService;
import com.greatescape.api.monolith.service.QuestIntegrationSettingService;
import com.greatescape.api.monolith.service.dto.QuestIntegrationSettingDTO;
import com.greatescape.api.monolith.service.mapper.QuestIntegrationSettingMapper;
import java.util.List;
import javax.persistence.EntityManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
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
@WithMockUser
public class QuestIntegrationSettingResourceIT {

    private static final QuestIntegrationType DEFAULT_TYPE = QuestIntegrationType.MIR_KVESTOV;
    private static final QuestIntegrationType UPDATED_TYPE = QuestIntegrationType.BOOK_FORM;

    private static final String DEFAULT_SETTINGS = "AAAAAAAAAA";
    private static final String UPDATED_SETTINGS = "BBBBBBBBBB";

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
        restQuestIntegrationSettingMockMvc.perform(post("/api/quest-integration-settings")
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
        int databaseSizeBeforeCreate = questIntegrationSettingRepository.findAll().size();

        // Create the QuestIntegrationSetting with an existing ID
        questIntegrationSetting.setId(1L);
        QuestIntegrationSettingDTO questIntegrationSettingDTO = questIntegrationSettingMapper.toDto(questIntegrationSetting);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestIntegrationSettingMockMvc.perform(post("/api/quest-integration-settings")
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


        restQuestIntegrationSettingMockMvc.perform(post("/api/quest-integration-settings")
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
        restQuestIntegrationSettingMockMvc.perform(get("/api/quest-integration-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questIntegrationSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].settings").value(hasItem(DEFAULT_SETTINGS.toString())));
    }

    @Test
    @Transactional
    public void getQuestIntegrationSetting() throws Exception {
        // Initialize the database
        questIntegrationSettingRepository.saveAndFlush(questIntegrationSetting);

        // Get the questIntegrationSetting
        restQuestIntegrationSettingMockMvc.perform(get("/api/quest-integration-settings/{id}", questIntegrationSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questIntegrationSetting.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.settings").value(DEFAULT_SETTINGS.toString()));
    }


    @Test
    @Transactional
    public void getQuestIntegrationSettingsByIdFiltering() throws Exception {
        // Initialize the database
        questIntegrationSettingRepository.saveAndFlush(questIntegrationSetting);

        Long id = questIntegrationSetting.getId();

        defaultQuestIntegrationSettingShouldBeFound("id.equals=" + id);
        defaultQuestIntegrationSettingShouldNotBeFound("id.notEquals=" + id);

        defaultQuestIntegrationSettingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestIntegrationSettingShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestIntegrationSettingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestIntegrationSettingShouldNotBeFound("id.lessThan=" + id);
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
        Long questId = quest.getId();

        // Get all the questIntegrationSettingList where quest equals to questId
        defaultQuestIntegrationSettingShouldBeFound("questId.equals=" + questId);

        // Get all the questIntegrationSettingList where quest equals to questId + 1
        defaultQuestIntegrationSettingShouldNotBeFound("questId.equals=" + (questId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestIntegrationSettingShouldBeFound(String filter) throws Exception {
        restQuestIntegrationSettingMockMvc.perform(get("/api/quest-integration-settings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questIntegrationSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].settings").value(hasItem(DEFAULT_SETTINGS.toString())));

        // Check, that the count call also returns 1
        restQuestIntegrationSettingMockMvc.perform(get("/api/quest-integration-settings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestIntegrationSettingShouldNotBeFound(String filter) throws Exception {
        restQuestIntegrationSettingMockMvc.perform(get("/api/quest-integration-settings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestIntegrationSettingMockMvc.perform(get("/api/quest-integration-settings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingQuestIntegrationSetting() throws Exception {
        // Get the questIntegrationSetting
        restQuestIntegrationSettingMockMvc.perform(get("/api/quest-integration-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
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

        restQuestIntegrationSettingMockMvc.perform(put("/api/quest-integration-settings")
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
        restQuestIntegrationSettingMockMvc.perform(put("/api/quest-integration-settings")
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
        restQuestIntegrationSettingMockMvc.perform(delete("/api/quest-integration-settings/{id}", questIntegrationSetting.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestIntegrationSetting> questIntegrationSettingList = questIntegrationSettingRepository.findAll();
        assertThat(questIntegrationSettingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
