package com.greatescape.api.monolith.web.rest;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.Thematic;
import com.greatescape.api.monolith.repository.ThematicRepository;
import com.greatescape.api.monolith.service.ThematicQueryService;
import com.greatescape.api.monolith.service.ThematicService;
import com.greatescape.api.monolith.service.dto.ThematicDTO;
import com.greatescape.api.monolith.service.mapper.ThematicMapper;
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
 * Integration tests for the {@link ThematicResource} REST controller.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ThematicResourceIT {

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private ThematicRepository thematicRepository;

    @Autowired
    private ThematicMapper thematicMapper;

    @Autowired
    private ThematicService thematicService;

    @Autowired
    private ThematicQueryService thematicQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restThematicMockMvc;

    private Thematic thematic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Thematic createEntity(EntityManager em) {
        Thematic thematic = new Thematic()
            .setSlug(DEFAULT_SLUG)
            .setTitle(DEFAULT_TITLE);
        return thematic;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Thematic createUpdatedEntity(EntityManager em) {
        Thematic thematic = new Thematic()
            .setSlug(UPDATED_SLUG)
            .setTitle(UPDATED_TITLE);
        return thematic;
    }

    @BeforeEach
    public void initTest() {
        thematic = createEntity(em);
    }

    @Test
    @Transactional
    public void createThematic() throws Exception {
        int databaseSizeBeforeCreate = thematicRepository.findAll().size();
        // Create the Thematic
        ThematicDTO thematicDTO = thematicMapper.toDto(thematic);
        restThematicMockMvc.perform(post("/api/thematics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thematicDTO)))
            .andExpect(status().isCreated());

        // Validate the Thematic in the database
        List<Thematic> thematicList = thematicRepository.findAll();
        assertThat(thematicList).hasSize(databaseSizeBeforeCreate + 1);
        Thematic testThematic = thematicList.get(thematicList.size() - 1);
        assertThat(testThematic.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testThematic.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createThematicWithExistingId() throws Exception {
        final Thematic thematic = thematicRepository.save(createEntity(em));
        int databaseSizeBeforeCreate = thematicRepository.findAll().size();

        // Create the Thematic with an existing ID
        this.thematic.setId(thematic.getId());
        ThematicDTO thematicDTO = thematicMapper.toDto(this.thematic);

        // An entity with an existing ID cannot be created, so this API call must fail
        restThematicMockMvc.perform(post("/api/thematics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thematicDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Thematic in the database
        List<Thematic> thematicList = thematicRepository.findAll();
        assertThat(thematicList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = thematicRepository.findAll().size();
        // set the field null
        thematic.setSlug(null);

        // Create the Thematic, which fails.
        ThematicDTO thematicDTO = thematicMapper.toDto(thematic);


        restThematicMockMvc.perform(post("/api/thematics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thematicDTO)))
            .andExpect(status().isBadRequest());

        List<Thematic> thematicList = thematicRepository.findAll();
        assertThat(thematicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = thematicRepository.findAll().size();
        // set the field null
        thematic.setTitle(null);

        // Create the Thematic, which fails.
        ThematicDTO thematicDTO = thematicMapper.toDto(thematic);


        restThematicMockMvc.perform(post("/api/thematics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thematicDTO)))
            .andExpect(status().isBadRequest());

        List<Thematic> thematicList = thematicRepository.findAll();
        assertThat(thematicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllThematics() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList
        restThematicMockMvc.perform(get("/api/thematics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thematic.getId().toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    public void getThematic() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get the thematic
        restThematicMockMvc.perform(get("/api/thematics/{id}", thematic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(thematic.getId().toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }


    @Test
    @Transactional
    public void getThematicsByIdFiltering() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        UUID id = thematic.getId();

        defaultThematicShouldBeFound("id.equals=" + id);
        defaultThematicShouldNotBeFound("id.notEquals=" + id);
    }


    @Test
    @Transactional
    public void getAllThematicsBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList where slug equals to DEFAULT_SLUG
        defaultThematicShouldBeFound("slug.equals=" + DEFAULT_SLUG);

        // Get all the thematicList where slug equals to UPDATED_SLUG
        defaultThematicShouldNotBeFound("slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllThematicsBySlugIsNotEqualToSomething() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList where slug not equals to DEFAULT_SLUG
        defaultThematicShouldNotBeFound("slug.notEquals=" + DEFAULT_SLUG);

        // Get all the thematicList where slug not equals to UPDATED_SLUG
        defaultThematicShouldBeFound("slug.notEquals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllThematicsBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList where slug in DEFAULT_SLUG or UPDATED_SLUG
        defaultThematicShouldBeFound("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG);

        // Get all the thematicList where slug equals to UPDATED_SLUG
        defaultThematicShouldNotBeFound("slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllThematicsBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList where slug is not null
        defaultThematicShouldBeFound("slug.specified=true");

        // Get all the thematicList where slug is null
        defaultThematicShouldNotBeFound("slug.specified=false");
    }
                @Test
    @Transactional
    public void getAllThematicsBySlugContainsSomething() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList where slug contains DEFAULT_SLUG
        defaultThematicShouldBeFound("slug.contains=" + DEFAULT_SLUG);

        // Get all the thematicList where slug contains UPDATED_SLUG
        defaultThematicShouldNotBeFound("slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllThematicsBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList where slug does not contain DEFAULT_SLUG
        defaultThematicShouldNotBeFound("slug.doesNotContain=" + DEFAULT_SLUG);

        // Get all the thematicList where slug does not contain UPDATED_SLUG
        defaultThematicShouldBeFound("slug.doesNotContain=" + UPDATED_SLUG);
    }


    @Test
    @Transactional
    public void getAllThematicsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList where title equals to DEFAULT_TITLE
        defaultThematicShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the thematicList where title equals to UPDATED_TITLE
        defaultThematicShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllThematicsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList where title not equals to DEFAULT_TITLE
        defaultThematicShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the thematicList where title not equals to UPDATED_TITLE
        defaultThematicShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllThematicsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultThematicShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the thematicList where title equals to UPDATED_TITLE
        defaultThematicShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllThematicsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList where title is not null
        defaultThematicShouldBeFound("title.specified=true");

        // Get all the thematicList where title is null
        defaultThematicShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllThematicsByTitleContainsSomething() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList where title contains DEFAULT_TITLE
        defaultThematicShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the thematicList where title contains UPDATED_TITLE
        defaultThematicShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllThematicsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        // Get all the thematicList where title does not contain DEFAULT_TITLE
        defaultThematicShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the thematicList where title does not contain UPDATED_TITLE
        defaultThematicShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllThematicsByQuestIsEqualToSomething() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);
        Quest quest = QuestResourceIT.createEntity(em);
        em.persist(quest);
        em.flush();
        thematic.addQuest(quest);
        thematicRepository.saveAndFlush(thematic);
        UUID questId = quest.getId();

        // Get all the thematicList where quest equals to questId
        defaultThematicShouldBeFound("questId.equals=" + questId);

        // Get all the thematicList where quest equals to questId + 1
        defaultThematicShouldNotBeFound("questId.equals=" + UUID.fromString("ac0b2139-d2ca-4f5f-8cee-d72c10a8a662"));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultThematicShouldBeFound(String filter) throws Exception {
        restThematicMockMvc.perform(get("/api/thematics?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thematic.getId().toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));

        // Check, that the count call also returns 1
        restThematicMockMvc.perform(get("/api/thematics/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultThematicShouldNotBeFound(String filter) throws Exception {
        restThematicMockMvc.perform(get("/api/thematics?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restThematicMockMvc.perform(get("/api/thematics/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingThematic() throws Exception {
        // Get the thematic
        restThematicMockMvc.perform(
            get("/api/thematics/{id}", UUID.fromString("8aaab667-faeb-41e7-8467-48d825ddf863"))
        ).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateThematic() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        int databaseSizeBeforeUpdate = thematicRepository.findAll().size();

        // Update the thematic
        Thematic updatedThematic = thematicRepository.findById(thematic.getId()).get();
        // Disconnect from session so that the updates on updatedThematic are not directly saved in db
        em.detach(updatedThematic);
        updatedThematic
            .setSlug(UPDATED_SLUG)
            .setTitle(UPDATED_TITLE);
        ThematicDTO thematicDTO = thematicMapper.toDto(updatedThematic);

        restThematicMockMvc.perform(put("/api/thematics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thematicDTO)))
            .andExpect(status().isOk());

        // Validate the Thematic in the database
        List<Thematic> thematicList = thematicRepository.findAll();
        assertThat(thematicList).hasSize(databaseSizeBeforeUpdate);
        Thematic testThematic = thematicList.get(thematicList.size() - 1);
        assertThat(testThematic.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testThematic.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingThematic() throws Exception {
        int databaseSizeBeforeUpdate = thematicRepository.findAll().size();

        // Create the Thematic
        ThematicDTO thematicDTO = thematicMapper.toDto(thematic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThematicMockMvc.perform(put("/api/thematics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thematicDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Thematic in the database
        List<Thematic> thematicList = thematicRepository.findAll();
        assertThat(thematicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteThematic() throws Exception {
        // Initialize the database
        thematicRepository.saveAndFlush(thematic);

        int databaseSizeBeforeDelete = thematicRepository.findAll().size();

        // Delete the thematic
        restThematicMockMvc.perform(delete("/api/thematics/{id}", thematic.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Thematic> thematicList = thematicRepository.findAll();
        assertThat(thematicList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
