package com.greatescape.api.monolith.web.rest.admin;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.Subscriber;
import com.greatescape.api.monolith.repository.SubscriberRepository;
import com.greatescape.api.monolith.security.AuthoritiesConstants;
import com.greatescape.api.monolith.service.SubscriberQueryService;
import com.greatescape.api.monolith.service.SubscriberService;
import com.greatescape.api.monolith.service.dto.SubscriberDTO;
import com.greatescape.api.monolith.service.mapper.SubscriberMapper;
import com.greatescape.api.monolith.web.rest.TestUtil;
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
 * Integration tests for the {@link SubscriberResource} REST controller.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
@AutoConfigureMockMvc
@WithMockUser(username="admin",authorities=AuthoritiesConstants.ADMIN)
public class SubscriberResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "default@email.com";
    private static final String UPDATED_EMAIL = "updated@email.com";

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private SubscriberMapper subscriberMapper;

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private SubscriberQueryService subscriberQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriberMockMvc;

    private Subscriber subscriber;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subscriber createEntity(EntityManager em) {
        return new Subscriber()
            .setName(DEFAULT_NAME)
            .setEmail(DEFAULT_EMAIL);
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subscriber createUpdatedEntity(EntityManager em) {
        return new Subscriber()
            .setName(UPDATED_NAME)
            .setEmail(UPDATED_EMAIL);
    }

    @BeforeEach
    public void initTest() {
        subscriber = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubscriber() throws Exception {
        int databaseSizeBeforeCreate = subscriberRepository.findAll().size();
        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);
        restSubscriberMockMvc.perform(post("/admin-api/subscribers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isCreated());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeCreate + 1);
        Subscriber testSubscriber = subscriberList.get(subscriberList.size() - 1);
        assertThat(testSubscriber.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubscriber.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createSubscriberWithExistingId() throws Exception {
        final Subscriber subscriber = subscriberRepository.save(createEntity(em));
        int databaseSizeBeforeCreate = subscriberRepository.findAll().size();

        // Create the Subscriber with an existing ID
        this.subscriber.setId(subscriber.getId());
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(this.subscriber);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriberMockMvc.perform(post("/admin-api/subscribers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setName(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);


        restSubscriberMockMvc.perform(post("/admin-api/subscribers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setEmail(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);


        restSubscriberMockMvc.perform(post("/admin-api/subscribers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubscribers() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList
        restSubscriberMockMvc.perform(get("/admin-api/subscribers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriber.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    public void getSubscriber() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get the subscriber
        restSubscriberMockMvc.perform(get("/admin-api/subscribers/{id}", subscriber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriber.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }


    @Test
    @Transactional
    public void getSubscribersByIdFiltering() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        UUID id = subscriber.getId();

        defaultSubscriberShouldBeFound("id.equals=" + id);
        defaultSubscriberShouldNotBeFound("id.notEquals=" + id);
    }


    @Test
    @Transactional
    public void getAllSubscribersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where name equals to DEFAULT_NAME
        defaultSubscriberShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the subscriberList where name equals to UPDATED_NAME
        defaultSubscriberShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSubscribersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where name not equals to DEFAULT_NAME
        defaultSubscriberShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the subscriberList where name not equals to UPDATED_NAME
        defaultSubscriberShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSubscribersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSubscriberShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the subscriberList where name equals to UPDATED_NAME
        defaultSubscriberShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSubscribersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where name is not null
        defaultSubscriberShouldBeFound("name.specified=true");

        // Get all the subscriberList where name is null
        defaultSubscriberShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllSubscribersByNameContainsSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where name contains DEFAULT_NAME
        defaultSubscriberShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the subscriberList where name contains UPDATED_NAME
        defaultSubscriberShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSubscribersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where name does not contain DEFAULT_NAME
        defaultSubscriberShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the subscriberList where name does not contain UPDATED_NAME
        defaultSubscriberShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllSubscribersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where email equals to DEFAULT_EMAIL
        defaultSubscriberShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the subscriberList where email equals to UPDATED_EMAIL
        defaultSubscriberShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSubscribersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where email not equals to DEFAULT_EMAIL
        defaultSubscriberShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the subscriberList where email not equals to UPDATED_EMAIL
        defaultSubscriberShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSubscribersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultSubscriberShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the subscriberList where email equals to UPDATED_EMAIL
        defaultSubscriberShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSubscribersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where email is not null
        defaultSubscriberShouldBeFound("email.specified=true");

        // Get all the subscriberList where email is null
        defaultSubscriberShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllSubscribersByEmailContainsSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where email contains DEFAULT_EMAIL
        defaultSubscriberShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the subscriberList where email contains UPDATED_EMAIL
        defaultSubscriberShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSubscribersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where email does not contain DEFAULT_EMAIL
        defaultSubscriberShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the subscriberList where email does not contain UPDATED_EMAIL
        defaultSubscriberShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubscriberShouldBeFound(String filter) throws Exception {
        restSubscriberMockMvc.perform(get("/admin-api/subscribers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriber.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restSubscriberMockMvc.perform(get("/admin-api/subscribers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubscriberShouldNotBeFound(String filter) throws Exception {
        restSubscriberMockMvc.perform(get("/admin-api/subscribers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubscriberMockMvc.perform(get("/admin-api/subscribers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSubscriber() throws Exception {
        // Get the subscriber
        restSubscriberMockMvc.perform(
            get("/admin-api/subscribers/{id}", UUID.fromString("03cbfdac-c326-42b2-b633-07e3afad67be"))
        ).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubscriber() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();

        // Update the subscriber
        Subscriber updatedSubscriber = subscriberRepository.findById(subscriber.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriber are not directly saved in db
        em.detach(updatedSubscriber);
        updatedSubscriber
            .setName(UPDATED_NAME)
            .setEmail(UPDATED_EMAIL);
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(updatedSubscriber);

        restSubscriberMockMvc.perform(put("/admin-api/subscribers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isOk());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
        Subscriber testSubscriber = subscriberList.get(subscriberList.size() - 1);
        assertThat(testSubscriber.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubscriber.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriberMockMvc.perform(put("/admin-api/subscribers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubscriber() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        int databaseSizeBeforeDelete = subscriberRepository.findAll().size();

        // Delete the subscriber
        restSubscriberMockMvc.perform(delete("/admin-api/subscribers/{id}", subscriber.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Subscriber> subscriberList = subscriberRepository.findAll();

        // Due to commented delete service
        // assertThat(subscriberList).hasSize(databaseSizeBeforeDelete - 1);
        assertThat(subscriberList).hasSize(databaseSizeBeforeDelete);
    }
}
