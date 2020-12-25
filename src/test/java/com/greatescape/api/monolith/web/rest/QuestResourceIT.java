package com.greatescape.api.monolith.web.rest;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.Company;
import com.greatescape.api.monolith.domain.Location;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.Thematic;
import com.greatescape.api.monolith.domain.enumeration.FearLevel;
import com.greatescape.api.monolith.domain.enumeration.QuestComplexity;
import com.greatescape.api.monolith.domain.enumeration.QuestType;
import com.greatescape.api.monolith.repository.QuestRepository;
import com.greatescape.api.monolith.service.QuestQueryService;
import com.greatescape.api.monolith.service.QuestService;
import com.greatescape.api.monolith.service.dto.QuestDTO;
import com.greatescape.api.monolith.service.mapper.QuestMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
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
 * Integration tests for the {@link QuestResource} REST controller.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class QuestResourceIT {

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PLAYERS_MIN_COUNT = 1;
    private static final Integer UPDATED_PLAYERS_MIN_COUNT = 2;
    private static final Integer SMALLER_PLAYERS_MIN_COUNT = 1 - 1;

    private static final Integer DEFAULT_PLAYERS_MAX_COUNT = 1;
    private static final Integer UPDATED_PLAYERS_MAX_COUNT = 2;
    private static final Integer SMALLER_PLAYERS_MAX_COUNT = 1 - 1;

    private static final Integer DEFAULT_DURATION_IN_MINUTES = 1;
    private static final Integer UPDATED_DURATION_IN_MINUTES = 2;
    private static final Integer SMALLER_DURATION_IN_MINUTES = 1 - 1;

    private static final QuestComplexity DEFAULT_COMPLEXITY = QuestComplexity.EASY;
    private static final QuestComplexity UPDATED_COMPLEXITY = QuestComplexity.MIDDLE;

    private static final FearLevel DEFAULT_FEAR_LEVEL = FearLevel.ABSENT;
    private static final FearLevel UPDATED_FEAR_LEVEL = FearLevel.MINIMAL;

    private static final QuestType DEFAULT_TYPE = QuestType.ESCAPE;
    private static final QuestType UPDATED_TYPE = QuestType.PERFORMANCE;

    private static final String DEFAULT_COVER_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_COVER_PHOTO = "BBBBBBBBBB";

    @Autowired
    private QuestRepository questRepository;

    @Mock
    private QuestRepository questRepositoryMock;

    @Autowired
    private QuestMapper questMapper;

    @Mock
    private QuestService questServiceMock;

    @Autowired
    private QuestService questService;

    @Autowired
    private QuestQueryService questQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestMockMvc;

    private Quest quest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quest createEntity(EntityManager em) {
        Quest quest = new Quest()
            .setSlug(DEFAULT_SLUG)
            .setTitle(DEFAULT_TITLE)
            .setDescription(DEFAULT_DESCRIPTION)
            .setPlayersMinCount(DEFAULT_PLAYERS_MIN_COUNT)
            .setPlayersMaxCount(DEFAULT_PLAYERS_MAX_COUNT)
            .setDurationInMinutes(DEFAULT_DURATION_IN_MINUTES)
            .setComplexity(DEFAULT_COMPLEXITY)
            .setFearLevel(DEFAULT_FEAR_LEVEL)
            .setType(DEFAULT_TYPE)
            .setCoverPhoto(DEFAULT_COVER_PHOTO);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        quest.setLocation(location);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        quest.setCompany(company);
        return quest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quest createUpdatedEntity(EntityManager em) {
        Quest quest = new Quest()
            .setSlug(UPDATED_SLUG)
            .setTitle(UPDATED_TITLE)
            .setDescription(UPDATED_DESCRIPTION)
            .setPlayersMinCount(UPDATED_PLAYERS_MIN_COUNT)
            .setPlayersMaxCount(UPDATED_PLAYERS_MAX_COUNT)
            .setDurationInMinutes(UPDATED_DURATION_IN_MINUTES)
            .setComplexity(UPDATED_COMPLEXITY)
            .setFearLevel(UPDATED_FEAR_LEVEL)
            .setType(UPDATED_TYPE)
            .setCoverPhoto(UPDATED_COVER_PHOTO);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        quest.setLocation(location);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        quest.setCompany(company);
        return quest;
    }

    @BeforeEach
    public void initTest() {
        quest = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuest() throws Exception {
        int databaseSizeBeforeCreate = questRepository.findAll().size();
        // Create the Quest
        QuestDTO questDTO = questMapper.toDto(quest);
        restQuestMockMvc.perform(post("/api/quests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questDTO)))
            .andExpect(status().isCreated());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeCreate + 1);
        Quest testQuest = questList.get(questList.size() - 1);
        assertThat(testQuest.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testQuest.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuest.getPlayersMinCount()).isEqualTo(DEFAULT_PLAYERS_MIN_COUNT);
        assertThat(testQuest.getPlayersMaxCount()).isEqualTo(DEFAULT_PLAYERS_MAX_COUNT);
        assertThat(testQuest.getDurationInMinutes()).isEqualTo(DEFAULT_DURATION_IN_MINUTES);
        assertThat(testQuest.getComplexity()).isEqualTo(DEFAULT_COMPLEXITY);
        assertThat(testQuest.getFearLevel()).isEqualTo(DEFAULT_FEAR_LEVEL);
        assertThat(testQuest.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testQuest.getCoverPhoto()).isEqualTo(DEFAULT_COVER_PHOTO);
    }

    @Test
    @Transactional
    public void createQuestWithExistingId() throws Exception {
        final Quest quest = questRepository.save(createEntity(em));
        int databaseSizeBeforeCreate = questRepository.findAll().size();

        // Create the Quest with an existing ID
        this.quest.setId(quest.getId());
        QuestDTO questDTO = questMapper.toDto(this.quest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestMockMvc.perform(post("/api/quests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setSlug(null);

        // Create the Quest, which fails.
        QuestDTO questDTO = questMapper.toDto(quest);


        restQuestMockMvc.perform(post("/api/quests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questDTO)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setTitle(null);

        // Create the Quest, which fails.
        QuestDTO questDTO = questMapper.toDto(quest);


        restQuestMockMvc.perform(post("/api/quests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questDTO)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPlayersMinCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setPlayersMinCount(null);

        // Create the Quest, which fails.
        QuestDTO questDTO = questMapper.toDto(quest);


        restQuestMockMvc.perform(post("/api/quests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questDTO)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPlayersMaxCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setPlayersMaxCount(null);

        // Create the Quest, which fails.
        QuestDTO questDTO = questMapper.toDto(quest);


        restQuestMockMvc.perform(post("/api/quests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questDTO)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationInMinutesIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setDurationInMinutes(null);

        // Create the Quest, which fails.
        QuestDTO questDTO = questMapper.toDto(quest);


        restQuestMockMvc.perform(post("/api/quests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questDTO)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkComplexityIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setComplexity(null);

        // Create the Quest, which fails.
        QuestDTO questDTO = questMapper.toDto(quest);


        restQuestMockMvc.perform(post("/api/quests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questDTO)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFearLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setFearLevel(null);

        // Create the Quest, which fails.
        QuestDTO questDTO = questMapper.toDto(quest);


        restQuestMockMvc.perform(post("/api/quests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questDTO)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = questRepository.findAll().size();
        // set the field null
        quest.setType(null);

        // Create the Quest, which fails.
        QuestDTO questDTO = questMapper.toDto(quest);


        restQuestMockMvc.perform(post("/api/quests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questDTO)))
            .andExpect(status().isBadRequest());

        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuests() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList
        restQuestMockMvc.perform(get("/api/quests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quest.getId().toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].playersMinCount").value(hasItem(DEFAULT_PLAYERS_MIN_COUNT)))
            .andExpect(jsonPath("$.[*].playersMaxCount").value(hasItem(DEFAULT_PLAYERS_MAX_COUNT)))
            .andExpect(jsonPath("$.[*].durationInMinutes").value(hasItem(DEFAULT_DURATION_IN_MINUTES)))
            .andExpect(jsonPath("$.[*].complexity").value(hasItem(DEFAULT_COMPLEXITY.toString())))
            .andExpect(jsonPath("$.[*].fearLevel").value(hasItem(DEFAULT_FEAR_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].coverPhoto").value(hasItem(DEFAULT_COVER_PHOTO)))
        ;
    }

    @SuppressWarnings({"unchecked"})
    public void getAllQuestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(questServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestMockMvc.perform(get("/api/quests?eagerload=true"))
            .andExpect(status().isOk());

        verify(questServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllQuestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(questServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestMockMvc.perform(get("/api/quests?eagerload=true"))
            .andExpect(status().isOk());

        verify(questServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getQuest() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get the quest
        restQuestMockMvc.perform(get("/api/quests/{id}", quest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quest.getId().toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.playersMinCount").value(DEFAULT_PLAYERS_MIN_COUNT))
            .andExpect(jsonPath("$.playersMaxCount").value(DEFAULT_PLAYERS_MAX_COUNT))
            .andExpect(jsonPath("$.durationInMinutes").value(DEFAULT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$.complexity").value(DEFAULT_COMPLEXITY.toString()))
            .andExpect(jsonPath("$.fearLevel").value(DEFAULT_FEAR_LEVEL.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.coverPhoto").value(DEFAULT_COVER_PHOTO));
    }


    @Test
    @Transactional
    public void getQuestsByIdFiltering() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        UUID id = quest.getId();

        defaultQuestShouldBeFound("id.equals=" + id);
        defaultQuestShouldNotBeFound("id.notEquals=" + id);
    }


    @Test
    @Transactional
    public void getAllQuestsBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where slug equals to DEFAULT_SLUG
        defaultQuestShouldBeFound("slug.equals=" + DEFAULT_SLUG);

        // Get all the questList where slug equals to UPDATED_SLUG
        defaultQuestShouldNotBeFound("slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllQuestsBySlugIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where slug not equals to DEFAULT_SLUG
        defaultQuestShouldNotBeFound("slug.notEquals=" + DEFAULT_SLUG);

        // Get all the questList where slug not equals to UPDATED_SLUG
        defaultQuestShouldBeFound("slug.notEquals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllQuestsBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where slug in DEFAULT_SLUG or UPDATED_SLUG
        defaultQuestShouldBeFound("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG);

        // Get all the questList where slug equals to UPDATED_SLUG
        defaultQuestShouldNotBeFound("slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllQuestsBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where slug is not null
        defaultQuestShouldBeFound("slug.specified=true");

        // Get all the questList where slug is null
        defaultQuestShouldNotBeFound("slug.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuestsBySlugContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where slug contains DEFAULT_SLUG
        defaultQuestShouldBeFound("slug.contains=" + DEFAULT_SLUG);

        // Get all the questList where slug contains UPDATED_SLUG
        defaultQuestShouldNotBeFound("slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllQuestsBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where slug does not contain DEFAULT_SLUG
        defaultQuestShouldNotBeFound("slug.doesNotContain=" + DEFAULT_SLUG);

        // Get all the questList where slug does not contain UPDATED_SLUG
        defaultQuestShouldBeFound("slug.doesNotContain=" + UPDATED_SLUG);
    }


    @Test
    @Transactional
    public void getAllQuestsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where title equals to DEFAULT_TITLE
        defaultQuestShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the questList where title equals to UPDATED_TITLE
        defaultQuestShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllQuestsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where title not equals to DEFAULT_TITLE
        defaultQuestShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the questList where title not equals to UPDATED_TITLE
        defaultQuestShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllQuestsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultQuestShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the questList where title equals to UPDATED_TITLE
        defaultQuestShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllQuestsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where title is not null
        defaultQuestShouldBeFound("title.specified=true");

        // Get all the questList where title is null
        defaultQuestShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuestsByTitleContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where title contains DEFAULT_TITLE
        defaultQuestShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the questList where title contains UPDATED_TITLE
        defaultQuestShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllQuestsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where title does not contain DEFAULT_TITLE
        defaultQuestShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the questList where title does not contain UPDATED_TITLE
        defaultQuestShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllQuestsByPlayersMinCountIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMinCount equals to DEFAULT_PLAYERS_MIN_COUNT
        defaultQuestShouldBeFound("playersMinCount.equals=" + DEFAULT_PLAYERS_MIN_COUNT);

        // Get all the questList where playersMinCount equals to UPDATED_PLAYERS_MIN_COUNT
        defaultQuestShouldNotBeFound("playersMinCount.equals=" + UPDATED_PLAYERS_MIN_COUNT);
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMinCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMinCount not equals to DEFAULT_PLAYERS_MIN_COUNT
        defaultQuestShouldNotBeFound("playersMinCount.notEquals=" + DEFAULT_PLAYERS_MIN_COUNT);

        // Get all the questList where playersMinCount not equals to UPDATED_PLAYERS_MIN_COUNT
        defaultQuestShouldBeFound("playersMinCount.notEquals=" + UPDATED_PLAYERS_MIN_COUNT);
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMinCountIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMinCount in DEFAULT_PLAYERS_MIN_COUNT or UPDATED_PLAYERS_MIN_COUNT
        defaultQuestShouldBeFound("playersMinCount.in=" + DEFAULT_PLAYERS_MIN_COUNT + "," + UPDATED_PLAYERS_MIN_COUNT);

        // Get all the questList where playersMinCount equals to UPDATED_PLAYERS_MIN_COUNT
        defaultQuestShouldNotBeFound("playersMinCount.in=" + UPDATED_PLAYERS_MIN_COUNT);
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMinCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMinCount is not null
        defaultQuestShouldBeFound("playersMinCount.specified=true");

        // Get all the questList where playersMinCount is null
        defaultQuestShouldNotBeFound("playersMinCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMinCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMinCount is greater than or equal to DEFAULT_PLAYERS_MIN_COUNT
        defaultQuestShouldBeFound("playersMinCount.greaterThanOrEqual=" + DEFAULT_PLAYERS_MIN_COUNT);

        // Get all the questList where playersMinCount is greater than or equal to UPDATED_PLAYERS_MIN_COUNT
        defaultQuestShouldNotBeFound("playersMinCount.greaterThanOrEqual=" + UPDATED_PLAYERS_MIN_COUNT);
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMinCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMinCount is less than or equal to DEFAULT_PLAYERS_MIN_COUNT
        defaultQuestShouldBeFound("playersMinCount.lessThanOrEqual=" + DEFAULT_PLAYERS_MIN_COUNT);

        // Get all the questList where playersMinCount is less than or equal to SMALLER_PLAYERS_MIN_COUNT
        defaultQuestShouldNotBeFound("playersMinCount.lessThanOrEqual=" + SMALLER_PLAYERS_MIN_COUNT);
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMinCountIsLessThanSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMinCount is less than DEFAULT_PLAYERS_MIN_COUNT
        defaultQuestShouldNotBeFound("playersMinCount.lessThan=" + DEFAULT_PLAYERS_MIN_COUNT);

        // Get all the questList where playersMinCount is less than UPDATED_PLAYERS_MIN_COUNT
        defaultQuestShouldBeFound("playersMinCount.lessThan=" + UPDATED_PLAYERS_MIN_COUNT);
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMinCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMinCount is greater than DEFAULT_PLAYERS_MIN_COUNT
        defaultQuestShouldNotBeFound("playersMinCount.greaterThan=" + DEFAULT_PLAYERS_MIN_COUNT);

        // Get all the questList where playersMinCount is greater than SMALLER_PLAYERS_MIN_COUNT
        defaultQuestShouldBeFound("playersMinCount.greaterThan=" + SMALLER_PLAYERS_MIN_COUNT);
    }


    @Test
    @Transactional
    public void getAllQuestsByPlayersMaxCountIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMaxCount equals to DEFAULT_PLAYERS_MAX_COUNT
        defaultQuestShouldBeFound("playersMaxCount.equals=" + DEFAULT_PLAYERS_MAX_COUNT);

        // Get all the questList where playersMaxCount equals to UPDATED_PLAYERS_MAX_COUNT
        defaultQuestShouldNotBeFound("playersMaxCount.equals=" + UPDATED_PLAYERS_MAX_COUNT);
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMaxCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMaxCount not equals to DEFAULT_PLAYERS_MAX_COUNT
        defaultQuestShouldNotBeFound("playersMaxCount.notEquals=" + DEFAULT_PLAYERS_MAX_COUNT);

        // Get all the questList where playersMaxCount not equals to UPDATED_PLAYERS_MAX_COUNT
        defaultQuestShouldBeFound("playersMaxCount.notEquals=" + UPDATED_PLAYERS_MAX_COUNT);
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMaxCountIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMaxCount in DEFAULT_PLAYERS_MAX_COUNT or UPDATED_PLAYERS_MAX_COUNT
        defaultQuestShouldBeFound("playersMaxCount.in=" + DEFAULT_PLAYERS_MAX_COUNT + "," + UPDATED_PLAYERS_MAX_COUNT);

        // Get all the questList where playersMaxCount equals to UPDATED_PLAYERS_MAX_COUNT
        defaultQuestShouldNotBeFound("playersMaxCount.in=" + UPDATED_PLAYERS_MAX_COUNT);
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMaxCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMaxCount is not null
        defaultQuestShouldBeFound("playersMaxCount.specified=true");

        // Get all the questList where playersMaxCount is null
        defaultQuestShouldNotBeFound("playersMaxCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMaxCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMaxCount is greater than or equal to DEFAULT_PLAYERS_MAX_COUNT
        defaultQuestShouldBeFound("playersMaxCount.greaterThanOrEqual=" + DEFAULT_PLAYERS_MAX_COUNT);

        // Get all the questList where playersMaxCount is greater than or equal to UPDATED_PLAYERS_MAX_COUNT
        defaultQuestShouldNotBeFound("playersMaxCount.greaterThanOrEqual=" + UPDATED_PLAYERS_MAX_COUNT);
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMaxCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMaxCount is less than or equal to DEFAULT_PLAYERS_MAX_COUNT
        defaultQuestShouldBeFound("playersMaxCount.lessThanOrEqual=" + DEFAULT_PLAYERS_MAX_COUNT);

        // Get all the questList where playersMaxCount is less than or equal to SMALLER_PLAYERS_MAX_COUNT
        defaultQuestShouldNotBeFound("playersMaxCount.lessThanOrEqual=" + SMALLER_PLAYERS_MAX_COUNT);
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMaxCountIsLessThanSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMaxCount is less than DEFAULT_PLAYERS_MAX_COUNT
        defaultQuestShouldNotBeFound("playersMaxCount.lessThan=" + DEFAULT_PLAYERS_MAX_COUNT);

        // Get all the questList where playersMaxCount is less than UPDATED_PLAYERS_MAX_COUNT
        defaultQuestShouldBeFound("playersMaxCount.lessThan=" + UPDATED_PLAYERS_MAX_COUNT);
    }

    @Test
    @Transactional
    public void getAllQuestsByPlayersMaxCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where playersMaxCount is greater than DEFAULT_PLAYERS_MAX_COUNT
        defaultQuestShouldNotBeFound("playersMaxCount.greaterThan=" + DEFAULT_PLAYERS_MAX_COUNT);

        // Get all the questList where playersMaxCount is greater than SMALLER_PLAYERS_MAX_COUNT
        defaultQuestShouldBeFound("playersMaxCount.greaterThan=" + SMALLER_PLAYERS_MAX_COUNT);
    }


    @Test
    @Transactional
    public void getAllQuestsByDurationInMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where durationInMinutes equals to DEFAULT_DURATION_IN_MINUTES
        defaultQuestShouldBeFound("durationInMinutes.equals=" + DEFAULT_DURATION_IN_MINUTES);

        // Get all the questList where durationInMinutes equals to UPDATED_DURATION_IN_MINUTES
        defaultQuestShouldNotBeFound("durationInMinutes.equals=" + UPDATED_DURATION_IN_MINUTES);
    }

    @Test
    @Transactional
    public void getAllQuestsByDurationInMinutesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where durationInMinutes not equals to DEFAULT_DURATION_IN_MINUTES
        defaultQuestShouldNotBeFound("durationInMinutes.notEquals=" + DEFAULT_DURATION_IN_MINUTES);

        // Get all the questList where durationInMinutes not equals to UPDATED_DURATION_IN_MINUTES
        defaultQuestShouldBeFound("durationInMinutes.notEquals=" + UPDATED_DURATION_IN_MINUTES);
    }

    @Test
    @Transactional
    public void getAllQuestsByDurationInMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where durationInMinutes in DEFAULT_DURATION_IN_MINUTES or UPDATED_DURATION_IN_MINUTES
        defaultQuestShouldBeFound("durationInMinutes.in=" + DEFAULT_DURATION_IN_MINUTES + "," + UPDATED_DURATION_IN_MINUTES);

        // Get all the questList where durationInMinutes equals to UPDATED_DURATION_IN_MINUTES
        defaultQuestShouldNotBeFound("durationInMinutes.in=" + UPDATED_DURATION_IN_MINUTES);
    }

    @Test
    @Transactional
    public void getAllQuestsByDurationInMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where durationInMinutes is not null
        defaultQuestShouldBeFound("durationInMinutes.specified=true");

        // Get all the questList where durationInMinutes is null
        defaultQuestShouldNotBeFound("durationInMinutes.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestsByDurationInMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where durationInMinutes is greater than or equal to DEFAULT_DURATION_IN_MINUTES
        defaultQuestShouldBeFound("durationInMinutes.greaterThanOrEqual=" + DEFAULT_DURATION_IN_MINUTES);

        // Get all the questList where durationInMinutes is greater than or equal to UPDATED_DURATION_IN_MINUTES
        defaultQuestShouldNotBeFound("durationInMinutes.greaterThanOrEqual=" + UPDATED_DURATION_IN_MINUTES);
    }

    @Test
    @Transactional
    public void getAllQuestsByDurationInMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where durationInMinutes is less than or equal to DEFAULT_DURATION_IN_MINUTES
        defaultQuestShouldBeFound("durationInMinutes.lessThanOrEqual=" + DEFAULT_DURATION_IN_MINUTES);

        // Get all the questList where durationInMinutes is less than or equal to SMALLER_DURATION_IN_MINUTES
        defaultQuestShouldNotBeFound("durationInMinutes.lessThanOrEqual=" + SMALLER_DURATION_IN_MINUTES);
    }

    @Test
    @Transactional
    public void getAllQuestsByDurationInMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where durationInMinutes is less than DEFAULT_DURATION_IN_MINUTES
        defaultQuestShouldNotBeFound("durationInMinutes.lessThan=" + DEFAULT_DURATION_IN_MINUTES);

        // Get all the questList where durationInMinutes is less than UPDATED_DURATION_IN_MINUTES
        defaultQuestShouldBeFound("durationInMinutes.lessThan=" + UPDATED_DURATION_IN_MINUTES);
    }

    @Test
    @Transactional
    public void getAllQuestsByDurationInMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where durationInMinutes is greater than DEFAULT_DURATION_IN_MINUTES
        defaultQuestShouldNotBeFound("durationInMinutes.greaterThan=" + DEFAULT_DURATION_IN_MINUTES);

        // Get all the questList where durationInMinutes is greater than SMALLER_DURATION_IN_MINUTES
        defaultQuestShouldBeFound("durationInMinutes.greaterThan=" + SMALLER_DURATION_IN_MINUTES);
    }


    @Test
    @Transactional
    public void getAllQuestsByComplexityIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where complexity equals to DEFAULT_COMPLEXITY
        defaultQuestShouldBeFound("complexity.equals=" + DEFAULT_COMPLEXITY);

        // Get all the questList where complexity equals to UPDATED_COMPLEXITY
        defaultQuestShouldNotBeFound("complexity.equals=" + UPDATED_COMPLEXITY);
    }

    @Test
    @Transactional
    public void getAllQuestsByComplexityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where complexity not equals to DEFAULT_COMPLEXITY
        defaultQuestShouldNotBeFound("complexity.notEquals=" + DEFAULT_COMPLEXITY);

        // Get all the questList where complexity not equals to UPDATED_COMPLEXITY
        defaultQuestShouldBeFound("complexity.notEquals=" + UPDATED_COMPLEXITY);
    }

    @Test
    @Transactional
    public void getAllQuestsByComplexityIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where complexity in DEFAULT_COMPLEXITY or UPDATED_COMPLEXITY
        defaultQuestShouldBeFound("complexity.in=" + DEFAULT_COMPLEXITY + "," + UPDATED_COMPLEXITY);

        // Get all the questList where complexity equals to UPDATED_COMPLEXITY
        defaultQuestShouldNotBeFound("complexity.in=" + UPDATED_COMPLEXITY);
    }

    @Test
    @Transactional
    public void getAllQuestsByComplexityIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where complexity is not null
        defaultQuestShouldBeFound("complexity.specified=true");

        // Get all the questList where complexity is null
        defaultQuestShouldNotBeFound("complexity.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestsByFearLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where fearLevel equals to DEFAULT_FEAR_LEVEL
        defaultQuestShouldBeFound("fearLevel.equals=" + DEFAULT_FEAR_LEVEL);

        // Get all the questList where fearLevel equals to UPDATED_FEAR_LEVEL
        defaultQuestShouldNotBeFound("fearLevel.equals=" + UPDATED_FEAR_LEVEL);
    }

    @Test
    @Transactional
    public void getAllQuestsByFearLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where fearLevel not equals to DEFAULT_FEAR_LEVEL
        defaultQuestShouldNotBeFound("fearLevel.notEquals=" + DEFAULT_FEAR_LEVEL);

        // Get all the questList where fearLevel not equals to UPDATED_FEAR_LEVEL
        defaultQuestShouldBeFound("fearLevel.notEquals=" + UPDATED_FEAR_LEVEL);
    }

    @Test
    @Transactional
    public void getAllQuestsByFearLevelIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where fearLevel in DEFAULT_FEAR_LEVEL or UPDATED_FEAR_LEVEL
        defaultQuestShouldBeFound("fearLevel.in=" + DEFAULT_FEAR_LEVEL + "," + UPDATED_FEAR_LEVEL);

        // Get all the questList where fearLevel equals to UPDATED_FEAR_LEVEL
        defaultQuestShouldNotBeFound("fearLevel.in=" + UPDATED_FEAR_LEVEL);
    }

    @Test
    @Transactional
    public void getAllQuestsByFearLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where fearLevel is not null
        defaultQuestShouldBeFound("fearLevel.specified=true");

        // Get all the questList where fearLevel is null
        defaultQuestShouldNotBeFound("fearLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where type equals to DEFAULT_TYPE
        defaultQuestShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the questList where type equals to UPDATED_TYPE
        defaultQuestShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuestsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where type not equals to DEFAULT_TYPE
        defaultQuestShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the questList where type not equals to UPDATED_TYPE
        defaultQuestShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuestsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultQuestShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the questList where type equals to UPDATED_TYPE
        defaultQuestShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuestsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        // Get all the questList where type is not null
        defaultQuestShouldBeFound("type.specified=true");

        // Get all the questList where type is null
        defaultQuestShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestsByLocationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Location location = quest.getLocation();
        questRepository.saveAndFlush(quest);
        UUID locationId = location.getId();

        // Get all the questList where location equals to locationId
        defaultQuestShouldBeFound("locationId.equals=" + locationId);

        // Get all the questList where location equals to locationId + 1
        defaultQuestShouldNotBeFound("locationId.equals=" + UUID.fromString("c20a14b8-be49-48d7-a975-2e0ac79df1af"));
    }


    @Test
    @Transactional
    public void getAllQuestsByCompanyIsEqualToSomething() throws Exception {
        // Get already existing entity
        Company company = quest.getCompany();
        questRepository.saveAndFlush(quest);
        UUID companyId = company.getId();

        // Get all the questList where company equals to companyId
        defaultQuestShouldBeFound("companyId.equals=" + companyId);

        // Get all the questList where company equals to companyId + 1
        defaultQuestShouldNotBeFound("companyId.equals=" + UUID.fromString("e0c1b4e7-d8aa-46be-88cb-925464876755"));
    }


    @Test
    @Transactional
    public void getAllQuestsByThematicIsEqualToSomething() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);
        Thematic thematic = ThematicResourceIT.createEntity(em);
        em.persist(thematic);
        em.flush();
        quest.addThematic(thematic);
        questRepository.saveAndFlush(quest);
        UUID thematicId = thematic.getId();

        // Get all the questList where thematic equals to thematicId
        defaultQuestShouldBeFound("thematicId.equals=" + thematicId);

        // Get all the questList where thematic equals to thematicId + 1
        defaultQuestShouldNotBeFound("thematicId.equals=" + UUID.fromString("5813cb0e-34f1-4ba1-9a35-fb756722d154"));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestShouldBeFound(String filter) throws Exception {
        restQuestMockMvc.perform(get("/api/quests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quest.getId().toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].playersMinCount").value(hasItem(DEFAULT_PLAYERS_MIN_COUNT)))
            .andExpect(jsonPath("$.[*].playersMaxCount").value(hasItem(DEFAULT_PLAYERS_MAX_COUNT)))
            .andExpect(jsonPath("$.[*].durationInMinutes").value(hasItem(DEFAULT_DURATION_IN_MINUTES)))
            .andExpect(jsonPath("$.[*].complexity").value(hasItem(DEFAULT_COMPLEXITY.toString())))
            .andExpect(jsonPath("$.[*].fearLevel").value(hasItem(DEFAULT_FEAR_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].coverPhoto").value(hasItem(DEFAULT_COVER_PHOTO)))
        ;

        // Check, that the count call also returns 1
        restQuestMockMvc.perform(get("/api/quests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestShouldNotBeFound(String filter) throws Exception {
        restQuestMockMvc.perform(get("/api/quests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestMockMvc.perform(get("/api/quests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingQuest() throws Exception {
        // Get the quest
        restQuestMockMvc.perform(get("/api/quests/{id}", UUID.fromString("224a5728-4212-4c9b-bdda-c473203b2bfc")))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuest() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        int databaseSizeBeforeUpdate = questRepository.findAll().size();

        // Update the quest
        Quest updatedQuest = questRepository.findById(quest.getId()).get();
        // Disconnect from session so that the updates on updatedQuest are not directly saved in db
        em.detach(updatedQuest);
        updatedQuest
            .setSlug(UPDATED_SLUG)
            .setTitle(UPDATED_TITLE)
            .setDescription(UPDATED_DESCRIPTION)
            .setPlayersMinCount(UPDATED_PLAYERS_MIN_COUNT)
            .setPlayersMaxCount(UPDATED_PLAYERS_MAX_COUNT)
            .setDurationInMinutes(UPDATED_DURATION_IN_MINUTES)
            .setComplexity(UPDATED_COMPLEXITY)
            .setFearLevel(UPDATED_FEAR_LEVEL)
            .setType(UPDATED_TYPE);
        QuestDTO questDTO = questMapper.toDto(updatedQuest);

        restQuestMockMvc.perform(put("/api/quests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questDTO)))
            .andExpect(status().isOk());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeUpdate);
        Quest testQuest = questList.get(questList.size() - 1);
        assertThat(testQuest.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testQuest.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuest.getPlayersMinCount()).isEqualTo(UPDATED_PLAYERS_MIN_COUNT);
        assertThat(testQuest.getPlayersMaxCount()).isEqualTo(UPDATED_PLAYERS_MAX_COUNT);
        assertThat(testQuest.getDurationInMinutes()).isEqualTo(UPDATED_DURATION_IN_MINUTES);
        assertThat(testQuest.getComplexity()).isEqualTo(UPDATED_COMPLEXITY);
        assertThat(testQuest.getFearLevel()).isEqualTo(UPDATED_FEAR_LEVEL);
        Assertions.assertThat(testQuest.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingQuest() throws Exception {
        int databaseSizeBeforeUpdate = questRepository.findAll().size();

        // Create the Quest
        QuestDTO questDTO = questMapper.toDto(quest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestMockMvc.perform(put("/api/quests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quest in the database
        List<Quest> questList = questRepository.findAll();
        assertThat(questList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuest() throws Exception {
        // Initialize the database
        questRepository.saveAndFlush(quest);

        int databaseSizeBeforeDelete = questRepository.findAll().size();

        // Delete the quest
        restQuestMockMvc.perform(delete("/api/quests/{id}", quest.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Quest> questList = questRepository.findAll();

        // Due to commented delete service
        // assertThat(questList).hasSize(databaseSizeBeforeDelete - 1);
        assertThat(questList).hasSize(databaseSizeBeforeDelete);
    }
}
