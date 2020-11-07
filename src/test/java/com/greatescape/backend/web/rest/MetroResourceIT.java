package com.greatescape.backend.web.rest;

import com.greatescape.backend.GreatEscapeBackendApp;
import com.greatescape.backend.domain.Location;
import com.greatescape.backend.domain.Metro;
import com.greatescape.backend.repository.MetroRepository;
import com.greatescape.backend.service.MetroQueryService;
import com.greatescape.backend.service.MetroService;
import com.greatescape.backend.service.dto.MetroDTO;
import com.greatescape.backend.service.mapper.MetroMapper;
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
 * Integration tests for the {@link MetroResource} REST controller.
 */
@SpringBootTest(classes = GreatEscapeBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MetroResourceIT {

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private MetroRepository metroRepository;

    @Autowired
    private MetroMapper metroMapper;

    @Autowired
    private MetroService metroService;

    @Autowired
    private MetroQueryService metroQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetroMockMvc;

    private Metro metro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metro createEntity(EntityManager em) {
        Metro metro = new Metro()
            .slug(DEFAULT_SLUG)
            .title(DEFAULT_TITLE);
        return metro;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metro createUpdatedEntity(EntityManager em) {
        Metro metro = new Metro()
            .slug(UPDATED_SLUG)
            .title(UPDATED_TITLE);
        return metro;
    }

    @BeforeEach
    public void initTest() {
        metro = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetro() throws Exception {
        int databaseSizeBeforeCreate = metroRepository.findAll().size();
        // Create the Metro
        MetroDTO metroDTO = metroMapper.toDto(metro);
        restMetroMockMvc.perform(post("/api/metros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metroDTO)))
            .andExpect(status().isCreated());

        // Validate the Metro in the database
        List<Metro> metroList = metroRepository.findAll();
        assertThat(metroList).hasSize(databaseSizeBeforeCreate + 1);
        Metro testMetro = metroList.get(metroList.size() - 1);
        assertThat(testMetro.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testMetro.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createMetroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metroRepository.findAll().size();

        // Create the Metro with an existing ID
        metro.setId(1L);
        MetroDTO metroDTO = metroMapper.toDto(metro);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetroMockMvc.perform(post("/api/metros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Metro in the database
        List<Metro> metroList = metroRepository.findAll();
        assertThat(metroList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = metroRepository.findAll().size();
        // set the field null
        metro.setSlug(null);

        // Create the Metro, which fails.
        MetroDTO metroDTO = metroMapper.toDto(metro);


        restMetroMockMvc.perform(post("/api/metros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metroDTO)))
            .andExpect(status().isBadRequest());

        List<Metro> metroList = metroRepository.findAll();
        assertThat(metroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = metroRepository.findAll().size();
        // set the field null
        metro.setTitle(null);

        // Create the Metro, which fails.
        MetroDTO metroDTO = metroMapper.toDto(metro);


        restMetroMockMvc.perform(post("/api/metros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metroDTO)))
            .andExpect(status().isBadRequest());

        List<Metro> metroList = metroRepository.findAll();
        assertThat(metroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMetros() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList
        restMetroMockMvc.perform(get("/api/metros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metro.getId().intValue())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    public void getMetro() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get the metro
        restMetroMockMvc.perform(get("/api/metros/{id}", metro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metro.getId().intValue()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }


    @Test
    @Transactional
    public void getMetrosByIdFiltering() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        Long id = metro.getId();

        defaultMetroShouldBeFound("id.equals=" + id);
        defaultMetroShouldNotBeFound("id.notEquals=" + id);

        defaultMetroShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMetroShouldNotBeFound("id.greaterThan=" + id);

        defaultMetroShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMetroShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMetrosBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList where slug equals to DEFAULT_SLUG
        defaultMetroShouldBeFound("slug.equals=" + DEFAULT_SLUG);

        // Get all the metroList where slug equals to UPDATED_SLUG
        defaultMetroShouldNotBeFound("slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllMetrosBySlugIsNotEqualToSomething() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList where slug not equals to DEFAULT_SLUG
        defaultMetroShouldNotBeFound("slug.notEquals=" + DEFAULT_SLUG);

        // Get all the metroList where slug not equals to UPDATED_SLUG
        defaultMetroShouldBeFound("slug.notEquals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllMetrosBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList where slug in DEFAULT_SLUG or UPDATED_SLUG
        defaultMetroShouldBeFound("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG);

        // Get all the metroList where slug equals to UPDATED_SLUG
        defaultMetroShouldNotBeFound("slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllMetrosBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList where slug is not null
        defaultMetroShouldBeFound("slug.specified=true");

        // Get all the metroList where slug is null
        defaultMetroShouldNotBeFound("slug.specified=false");
    }
                @Test
    @Transactional
    public void getAllMetrosBySlugContainsSomething() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList where slug contains DEFAULT_SLUG
        defaultMetroShouldBeFound("slug.contains=" + DEFAULT_SLUG);

        // Get all the metroList where slug contains UPDATED_SLUG
        defaultMetroShouldNotBeFound("slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllMetrosBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList where slug does not contain DEFAULT_SLUG
        defaultMetroShouldNotBeFound("slug.doesNotContain=" + DEFAULT_SLUG);

        // Get all the metroList where slug does not contain UPDATED_SLUG
        defaultMetroShouldBeFound("slug.doesNotContain=" + UPDATED_SLUG);
    }


    @Test
    @Transactional
    public void getAllMetrosByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList where title equals to DEFAULT_TITLE
        defaultMetroShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the metroList where title equals to UPDATED_TITLE
        defaultMetroShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMetrosByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList where title not equals to DEFAULT_TITLE
        defaultMetroShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the metroList where title not equals to UPDATED_TITLE
        defaultMetroShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMetrosByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultMetroShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the metroList where title equals to UPDATED_TITLE
        defaultMetroShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMetrosByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList where title is not null
        defaultMetroShouldBeFound("title.specified=true");

        // Get all the metroList where title is null
        defaultMetroShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllMetrosByTitleContainsSomething() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList where title contains DEFAULT_TITLE
        defaultMetroShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the metroList where title contains UPDATED_TITLE
        defaultMetroShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMetrosByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metroList where title does not contain DEFAULT_TITLE
        defaultMetroShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the metroList where title does not contain UPDATED_TITLE
        defaultMetroShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllMetrosByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        metro.addLocation(location);
        metroRepository.saveAndFlush(metro);
        Long locationId = location.getId();

        // Get all the metroList where location equals to locationId
        defaultMetroShouldBeFound("locationId.equals=" + locationId);

        // Get all the metroList where location equals to locationId + 1
        defaultMetroShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMetroShouldBeFound(String filter) throws Exception {
        restMetroMockMvc.perform(get("/api/metros?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metro.getId().intValue())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));

        // Check, that the count call also returns 1
        restMetroMockMvc.perform(get("/api/metros/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMetroShouldNotBeFound(String filter) throws Exception {
        restMetroMockMvc.perform(get("/api/metros?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMetroMockMvc.perform(get("/api/metros/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMetro() throws Exception {
        // Get the metro
        restMetroMockMvc.perform(get("/api/metros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetro() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        int databaseSizeBeforeUpdate = metroRepository.findAll().size();

        // Update the metro
        Metro updatedMetro = metroRepository.findById(metro.getId()).get();
        // Disconnect from session so that the updates on updatedMetro are not directly saved in db
        em.detach(updatedMetro);
        updatedMetro
            .slug(UPDATED_SLUG)
            .title(UPDATED_TITLE);
        MetroDTO metroDTO = metroMapper.toDto(updatedMetro);

        restMetroMockMvc.perform(put("/api/metros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metroDTO)))
            .andExpect(status().isOk());

        // Validate the Metro in the database
        List<Metro> metroList = metroRepository.findAll();
        assertThat(metroList).hasSize(databaseSizeBeforeUpdate);
        Metro testMetro = metroList.get(metroList.size() - 1);
        assertThat(testMetro.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testMetro.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingMetro() throws Exception {
        int databaseSizeBeforeUpdate = metroRepository.findAll().size();

        // Create the Metro
        MetroDTO metroDTO = metroMapper.toDto(metro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetroMockMvc.perform(put("/api/metros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Metro in the database
        List<Metro> metroList = metroRepository.findAll();
        assertThat(metroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMetro() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        int databaseSizeBeforeDelete = metroRepository.findAll().size();

        // Delete the metro
        restMetroMockMvc.perform(delete("/api/metros/{id}", metro.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Metro> metroList = metroRepository.findAll();
        assertThat(metroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
