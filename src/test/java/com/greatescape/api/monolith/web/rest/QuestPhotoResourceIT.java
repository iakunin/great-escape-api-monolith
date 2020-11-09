package com.greatescape.api.monolith.web.rest;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestPhoto;
import com.greatescape.api.monolith.repository.QuestPhotoRepository;
import com.greatescape.api.monolith.service.QuestPhotoQueryService;
import com.greatescape.api.monolith.service.QuestPhotoService;
import com.greatescape.api.monolith.service.dto.QuestPhotoDTO;
import com.greatescape.api.monolith.service.mapper.QuestPhotoMapper;
import java.util.List;
import java.util.UUID;
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
 * Integration tests for the {@link QuestPhotoResource} REST controller.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class QuestPhotoResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private QuestPhotoRepository questPhotoRepository;

    @Autowired
    private QuestPhotoMapper questPhotoMapper;

    @Autowired
    private QuestPhotoService questPhotoService;

    @Autowired
    private QuestPhotoQueryService questPhotoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestPhotoMockMvc;

    private QuestPhoto questPhoto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestPhoto createEntity(EntityManager em) {
        QuestPhoto questPhoto = new QuestPhoto()
            .setUrl(DEFAULT_URL);
        // Add required entity
        Quest quest;
        if (TestUtil.findAll(em, Quest.class).isEmpty()) {
            quest = QuestResourceIT.createEntity(em);
            em.persist(quest);
            em.flush();
        } else {
            quest = TestUtil.findAll(em, Quest.class).get(0);
        }
        questPhoto.setQuest(quest);
        return questPhoto;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestPhoto createUpdatedEntity(EntityManager em) {
        QuestPhoto questPhoto = new QuestPhoto()
            .setUrl(UPDATED_URL);
        // Add required entity
        Quest quest;
        if (TestUtil.findAll(em, Quest.class).isEmpty()) {
            quest = QuestResourceIT.createUpdatedEntity(em);
            em.persist(quest);
            em.flush();
        } else {
            quest = TestUtil.findAll(em, Quest.class).get(0);
        }
        questPhoto.setQuest(quest);
        return questPhoto;
    }

    @BeforeEach
    public void initTest() {
        questPhoto = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestPhoto() throws Exception {
        int databaseSizeBeforeCreate = questPhotoRepository.findAll().size();
        // Create the QuestPhoto
        QuestPhotoDTO questPhotoDTO = questPhotoMapper.toDto(questPhoto);
        restQuestPhotoMockMvc.perform(post("/api/quest-photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questPhotoDTO)))
            .andExpect(status().isCreated());

        // Validate the QuestPhoto in the database
        List<QuestPhoto> questPhotoList = questPhotoRepository.findAll();
        assertThat(questPhotoList).hasSize(databaseSizeBeforeCreate + 1);
        QuestPhoto testQuestPhoto = questPhotoList.get(questPhotoList.size() - 1);
        assertThat(testQuestPhoto.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createQuestPhotoWithExistingId() throws Exception {
        final QuestPhoto questPhoto = questPhotoRepository.save(createEntity(em));
        int databaseSizeBeforeCreate = questPhotoRepository.findAll().size();

        // Create the QuestPhoto with an existing ID
        this.questPhoto.setId(questPhoto.getId());
        QuestPhotoDTO questPhotoDTO = questPhotoMapper.toDto(this.questPhoto);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestPhotoMockMvc.perform(post("/api/quest-photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questPhotoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestPhoto in the database
        List<QuestPhoto> questPhotoList = questPhotoRepository.findAll();
        assertThat(questPhotoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = questPhotoRepository.findAll().size();
        // set the field null
        questPhoto.setUrl(null);

        // Create the QuestPhoto, which fails.
        QuestPhotoDTO questPhotoDTO = questPhotoMapper.toDto(questPhoto);


        restQuestPhotoMockMvc.perform(post("/api/quest-photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questPhotoDTO)))
            .andExpect(status().isBadRequest());

        List<QuestPhoto> questPhotoList = questPhotoRepository.findAll();
        assertThat(questPhotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuestPhotos() throws Exception {
        // Initialize the database
        questPhotoRepository.saveAndFlush(questPhoto);

        // Get all the questPhotoList
        restQuestPhotoMockMvc.perform(get("/api/quest-photos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questPhoto.getId().toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    public void getQuestPhoto() throws Exception {
        // Initialize the database
        questPhotoRepository.saveAndFlush(questPhoto);

        // Get the questPhoto
        restQuestPhotoMockMvc.perform(get("/api/quest-photos/{id}", questPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questPhoto.getId().toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }


    @Test
    @Transactional
    public void getQuestPhotosByIdFiltering() throws Exception {
        // Initialize the database
        questPhotoRepository.saveAndFlush(questPhoto);

        UUID id = questPhoto.getId();

        defaultQuestPhotoShouldBeFound("id.equals=" + id);
        defaultQuestPhotoShouldNotBeFound("id.notEquals=" + id);
    }


    @Test
    @Transactional
    public void getAllQuestPhotosByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        questPhotoRepository.saveAndFlush(questPhoto);

        // Get all the questPhotoList where url equals to DEFAULT_URL
        defaultQuestPhotoShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the questPhotoList where url equals to UPDATED_URL
        defaultQuestPhotoShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllQuestPhotosByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questPhotoRepository.saveAndFlush(questPhoto);

        // Get all the questPhotoList where url not equals to DEFAULT_URL
        defaultQuestPhotoShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the questPhotoList where url not equals to UPDATED_URL
        defaultQuestPhotoShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllQuestPhotosByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        questPhotoRepository.saveAndFlush(questPhoto);

        // Get all the questPhotoList where url in DEFAULT_URL or UPDATED_URL
        defaultQuestPhotoShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the questPhotoList where url equals to UPDATED_URL
        defaultQuestPhotoShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllQuestPhotosByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        questPhotoRepository.saveAndFlush(questPhoto);

        // Get all the questPhotoList where url is not null
        defaultQuestPhotoShouldBeFound("url.specified=true");

        // Get all the questPhotoList where url is null
        defaultQuestPhotoShouldNotBeFound("url.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuestPhotosByUrlContainsSomething() throws Exception {
        // Initialize the database
        questPhotoRepository.saveAndFlush(questPhoto);

        // Get all the questPhotoList where url contains DEFAULT_URL
        defaultQuestPhotoShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the questPhotoList where url contains UPDATED_URL
        defaultQuestPhotoShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllQuestPhotosByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        questPhotoRepository.saveAndFlush(questPhoto);

        // Get all the questPhotoList where url does not contain DEFAULT_URL
        defaultQuestPhotoShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the questPhotoList where url does not contain UPDATED_URL
        defaultQuestPhotoShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }


    @Test
    @Transactional
    public void getAllQuestPhotosByQuestIsEqualToSomething() throws Exception {
        // Get already existing entity
        Quest quest = questPhoto.getQuest();
        questPhotoRepository.saveAndFlush(questPhoto);
        UUID questId = quest.getId();

        // Get all the questPhotoList where quest equals to questId
        defaultQuestPhotoShouldBeFound("questId.equals=" + questId);

        // Get all the questPhotoList where quest equals to questId + 1
        defaultQuestPhotoShouldNotBeFound("questId.equals=" + UUID.fromString("2b203a0c-3305-4909-868f-e2c9afd0847f"));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestPhotoShouldBeFound(String filter) throws Exception {
        restQuestPhotoMockMvc.perform(get("/api/quest-photos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questPhoto.getId().toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restQuestPhotoMockMvc.perform(get("/api/quest-photos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestPhotoShouldNotBeFound(String filter) throws Exception {
        restQuestPhotoMockMvc.perform(get("/api/quest-photos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestPhotoMockMvc.perform(get("/api/quest-photos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingQuestPhoto() throws Exception {
        // Get the questPhoto
        restQuestPhotoMockMvc.perform(get("/api/quest-photos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestPhoto() throws Exception {
        // Initialize the database
        questPhotoRepository.saveAndFlush(questPhoto);

        int databaseSizeBeforeUpdate = questPhotoRepository.findAll().size();

        // Update the questPhoto
        QuestPhoto updatedQuestPhoto = questPhotoRepository.findById(questPhoto.getId()).get();
        // Disconnect from session so that the updates on updatedQuestPhoto are not directly saved in db
        em.detach(updatedQuestPhoto);
        updatedQuestPhoto
            .setUrl(UPDATED_URL);
        QuestPhotoDTO questPhotoDTO = questPhotoMapper.toDto(updatedQuestPhoto);

        restQuestPhotoMockMvc.perform(put("/api/quest-photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questPhotoDTO)))
            .andExpect(status().isOk());

        // Validate the QuestPhoto in the database
        List<QuestPhoto> questPhotoList = questPhotoRepository.findAll();
        assertThat(questPhotoList).hasSize(databaseSizeBeforeUpdate);
        QuestPhoto testQuestPhoto = questPhotoList.get(questPhotoList.size() - 1);
        assertThat(testQuestPhoto.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestPhoto() throws Exception {
        int databaseSizeBeforeUpdate = questPhotoRepository.findAll().size();

        // Create the QuestPhoto
        QuestPhotoDTO questPhotoDTO = questPhotoMapper.toDto(questPhoto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestPhotoMockMvc.perform(put("/api/quest-photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questPhotoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestPhoto in the database
        List<QuestPhoto> questPhotoList = questPhotoRepository.findAll();
        assertThat(questPhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuestPhoto() throws Exception {
        // Initialize the database
        questPhotoRepository.saveAndFlush(questPhoto);

        int databaseSizeBeforeDelete = questPhotoRepository.findAll().size();

        // Delete the questPhoto
        restQuestPhotoMockMvc.perform(delete("/api/quest-photos/{id}", questPhoto.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestPhoto> questPhotoList = questPhotoRepository.findAll();
        assertThat(questPhotoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
