package com.greatescape.api.monolith.web.rest;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.City;
import com.greatescape.api.monolith.repository.CityRepository;
import com.greatescape.api.monolith.service.CityQueryService;
import com.greatescape.api.monolith.service.CityService;
import com.greatescape.api.monolith.service.dto.CityDTO;
import com.greatescape.api.monolith.service.mapper.CityMapper;
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
 * Integration tests for the {@link CityResource} REST controller.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CityResourceIT {

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TIMEZONE = "AAAAAAAAAA";
    private static final String UPDATED_TIMEZONE = "BBBBBBBBBB";

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private CityService cityService;

    @Autowired
    private CityQueryService cityQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCityMockMvc;

    private City city;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createEntity(EntityManager em) {
        City city = new City()
            .setSlug(DEFAULT_SLUG)
            .setTitle(DEFAULT_TITLE)
            .setTimezone(DEFAULT_TIMEZONE);
        return city;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createUpdatedEntity(EntityManager em) {
        City city = new City()
            .setSlug(UPDATED_SLUG)
            .setTitle(UPDATED_TITLE)
            .setTimezone(UPDATED_TIMEZONE);
        return city;
    }

    @BeforeEach
    public void initTest() {
        city = createEntity(em);
    }

    @Test
    @Transactional
    public void createCity() throws Exception {
        int databaseSizeBeforeCreate = cityRepository.findAll().size();
        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);
        restCityMockMvc.perform(post("/api/cities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isCreated());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate + 1);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testCity.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCity.getTimezone()).isEqualTo(DEFAULT_TIMEZONE);
    }

    @Test
    @Transactional
    public void createCityWithExistingId() throws Exception {
        final City city = cityRepository.save(createEntity(em));
        int databaseSizeBeforeCreate = cityRepository.findAll().size();

        // Create the City with an existing ID
        this.city.setId(city.getId());
        CityDTO cityDTO = cityMapper.toDto(this.city);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCityMockMvc.perform(post("/api/cities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = cityRepository.findAll().size();
        // set the field null
        city.setSlug(null);

        // Create the City, which fails.
        CityDTO cityDTO = cityMapper.toDto(city);


        restCityMockMvc.perform(post("/api/cities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = cityRepository.findAll().size();
        // set the field null
        city.setTitle(null);

        // Create the City, which fails.
        CityDTO cityDTO = cityMapper.toDto(city);


        restCityMockMvc.perform(post("/api/cities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimezoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = cityRepository.findAll().size();
        // set the field null
        city.setTimezone(null);

        // Create the City, which fails.
        CityDTO cityDTO = cityMapper.toDto(city);


        restCityMockMvc.perform(post("/api/cities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCities() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList
        restCityMockMvc.perform(get("/api/cities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].timezone").value(hasItem(DEFAULT_TIMEZONE)));
    }

    @Test
    @Transactional
    public void getCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get the city
        restCityMockMvc.perform(get("/api/cities/{id}", city.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(city.getId().toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.timezone").value(DEFAULT_TIMEZONE));
    }


    @Test
    @Transactional
    public void getCitiesByIdFiltering() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        UUID id = city.getId();

        defaultCityShouldBeFound("id.equals=" + id);
        defaultCityShouldNotBeFound("id.notEquals=" + id);
    }


    @Test
    @Transactional
    public void getAllCitiesBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where slug equals to DEFAULT_SLUG
        defaultCityShouldBeFound("slug.equals=" + DEFAULT_SLUG);

        // Get all the cityList where slug equals to UPDATED_SLUG
        defaultCityShouldNotBeFound("slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllCitiesBySlugIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where slug not equals to DEFAULT_SLUG
        defaultCityShouldNotBeFound("slug.notEquals=" + DEFAULT_SLUG);

        // Get all the cityList where slug not equals to UPDATED_SLUG
        defaultCityShouldBeFound("slug.notEquals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllCitiesBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where slug in DEFAULT_SLUG or UPDATED_SLUG
        defaultCityShouldBeFound("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG);

        // Get all the cityList where slug equals to UPDATED_SLUG
        defaultCityShouldNotBeFound("slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllCitiesBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where slug is not null
        defaultCityShouldBeFound("slug.specified=true");

        // Get all the cityList where slug is null
        defaultCityShouldNotBeFound("slug.specified=false");
    }
                @Test
    @Transactional
    public void getAllCitiesBySlugContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where slug contains DEFAULT_SLUG
        defaultCityShouldBeFound("slug.contains=" + DEFAULT_SLUG);

        // Get all the cityList where slug contains UPDATED_SLUG
        defaultCityShouldNotBeFound("slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllCitiesBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where slug does not contain DEFAULT_SLUG
        defaultCityShouldNotBeFound("slug.doesNotContain=" + DEFAULT_SLUG);

        // Get all the cityList where slug does not contain UPDATED_SLUG
        defaultCityShouldBeFound("slug.doesNotContain=" + UPDATED_SLUG);
    }


    @Test
    @Transactional
    public void getAllCitiesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where title equals to DEFAULT_TITLE
        defaultCityShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the cityList where title equals to UPDATED_TITLE
        defaultCityShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCitiesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where title not equals to DEFAULT_TITLE
        defaultCityShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the cityList where title not equals to UPDATED_TITLE
        defaultCityShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCitiesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCityShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the cityList where title equals to UPDATED_TITLE
        defaultCityShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCitiesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where title is not null
        defaultCityShouldBeFound("title.specified=true");

        // Get all the cityList where title is null
        defaultCityShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllCitiesByTitleContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where title contains DEFAULT_TITLE
        defaultCityShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the cityList where title contains UPDATED_TITLE
        defaultCityShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCitiesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where title does not contain DEFAULT_TITLE
        defaultCityShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the cityList where title does not contain UPDATED_TITLE
        defaultCityShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllCitiesByTimezoneIsEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where timezone equals to DEFAULT_TIMEZONE
        defaultCityShouldBeFound("timezone.equals=" + DEFAULT_TIMEZONE);

        // Get all the cityList where timezone equals to UPDATED_TIMEZONE
        defaultCityShouldNotBeFound("timezone.equals=" + UPDATED_TIMEZONE);
    }

    @Test
    @Transactional
    public void getAllCitiesByTimezoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where timezone not equals to DEFAULT_TIMEZONE
        defaultCityShouldNotBeFound("timezone.notEquals=" + DEFAULT_TIMEZONE);

        // Get all the cityList where timezone not equals to UPDATED_TIMEZONE
        defaultCityShouldBeFound("timezone.notEquals=" + UPDATED_TIMEZONE);
    }

    @Test
    @Transactional
    public void getAllCitiesByTimezoneIsInShouldWork() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where timezone in DEFAULT_TIMEZONE or UPDATED_TIMEZONE
        defaultCityShouldBeFound("timezone.in=" + DEFAULT_TIMEZONE + "," + UPDATED_TIMEZONE);

        // Get all the cityList where timezone equals to UPDATED_TIMEZONE
        defaultCityShouldNotBeFound("timezone.in=" + UPDATED_TIMEZONE);
    }

    @Test
    @Transactional
    public void getAllCitiesByTimezoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where timezone is not null
        defaultCityShouldBeFound("timezone.specified=true");

        // Get all the cityList where timezone is null
        defaultCityShouldNotBeFound("timezone.specified=false");
    }
                @Test
    @Transactional
    public void getAllCitiesByTimezoneContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where timezone contains DEFAULT_TIMEZONE
        defaultCityShouldBeFound("timezone.contains=" + DEFAULT_TIMEZONE);

        // Get all the cityList where timezone contains UPDATED_TIMEZONE
        defaultCityShouldNotBeFound("timezone.contains=" + UPDATED_TIMEZONE);
    }

    @Test
    @Transactional
    public void getAllCitiesByTimezoneNotContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where timezone does not contain DEFAULT_TIMEZONE
        defaultCityShouldNotBeFound("timezone.doesNotContain=" + DEFAULT_TIMEZONE);

        // Get all the cityList where timezone does not contain UPDATED_TIMEZONE
        defaultCityShouldBeFound("timezone.doesNotContain=" + UPDATED_TIMEZONE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCityShouldBeFound(String filter) throws Exception {
        restCityMockMvc.perform(get("/api/cities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].timezone").value(hasItem(DEFAULT_TIMEZONE)));

        // Check, that the count call also returns 1
        restCityMockMvc.perform(get("/api/cities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCityShouldNotBeFound(String filter) throws Exception {
        restCityMockMvc.perform(get("/api/cities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCityMockMvc.perform(get("/api/cities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCity() throws Exception {
        // Get the city
        restCityMockMvc.perform(get("/api/cities/{id}", UUID.fromString("ae69808b-0bc6-47d7-b5fa-8c116c49e4d0")))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        int databaseSizeBeforeUpdate = cityRepository.findAll().size();

        // Update the city
        City updatedCity = cityRepository.findById(city.getId()).get();
        // Disconnect from session so that the updates on updatedCity are not directly saved in db
        em.detach(updatedCity);
        updatedCity
            .setSlug(UPDATED_SLUG)
            .setTitle(UPDATED_TITLE)
            .setTimezone(UPDATED_TIMEZONE);
        CityDTO cityDTO = cityMapper.toDto(updatedCity);

        restCityMockMvc.perform(put("/api/cities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isOk());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testCity.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCity.getTimezone()).isEqualTo(UPDATED_TIMEZONE);
    }

    @Test
    @Transactional
    public void updateNonExistingCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityMockMvc.perform(put("/api/cities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        int databaseSizeBeforeDelete = cityRepository.findAll().size();

        // Delete the city
        restCityMockMvc.perform(delete("/api/cities/{id}", city.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<City> cityList = cityRepository.findAll();

        // Due to commented delete service
        // assertThat(cityList).hasSize(databaseSizeBeforeDelete - 1);
        assertThat(cityList).hasSize(databaseSizeBeforeDelete);
    }
}
