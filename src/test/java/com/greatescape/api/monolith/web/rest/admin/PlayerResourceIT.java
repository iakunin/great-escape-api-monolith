package com.greatescape.api.monolith.web.rest.admin;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.Company;
import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.User;
import com.greatescape.api.monolith.domain.enumeration.Gender;
import com.greatescape.api.monolith.repository.PlayerRepository;
import com.greatescape.api.monolith.security.AuthoritiesConstants;
import com.greatescape.api.monolith.service.dto.PlayerDTO;
import com.greatescape.api.monolith.service.mapper.PlayerMapper;
import com.greatescape.api.monolith.web.rest.TestUtil;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PlayerResource} REST controller.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
@AutoConfigureMockMvc
@WithMockUser(username="admin",authorities=AuthoritiesConstants.ADMIN)
public class PlayerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "01";
    private static final String UPDATED_PHONE = "51";

    private static final String DEFAULT_EMAIL = "I@U";
    private static final String UPDATED_EMAIL = "zZ1$y@2!r]";

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTHDAY = LocalDate.ofEpochDay(-1L);

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Boolean DEFAULT_SUBSCRIPTION_ALLOWED = false;
    private static final Boolean UPDATED_SUBSCRIPTION_ALLOWED = true;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayerMockMvc;

    private Player player;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Player createEntity(EntityManager em) {
        Player player = new Player()
            .setName(DEFAULT_NAME)
            .setPhone(DEFAULT_PHONE)
            .setEmail(DEFAULT_EMAIL)
            .setBirthday(DEFAULT_BIRTHDAY)
            .setGender(DEFAULT_GENDER)
            .setSubscriptionAllowed(DEFAULT_SUBSCRIPTION_ALLOWED);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        player.setInternalUser(user);
        return player;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Player createUpdatedEntity(EntityManager em) {
        Player player = new Player()
            .setName(UPDATED_NAME)
            .setPhone(UPDATED_PHONE)
            .setEmail(UPDATED_EMAIL)
            .setBirthday(UPDATED_BIRTHDAY)
            .setGender(UPDATED_GENDER)
            .setSubscriptionAllowed(UPDATED_SUBSCRIPTION_ALLOWED);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        player.setInternalUser(user);
        return player;
    }

    @BeforeEach
    public void initTest() {
        player = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayer() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();
        // Create the Player
        PlayerDTO playerDTO = playerMapper.toDto(player);
        restPlayerMockMvc.perform(post("/admin-api/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isCreated());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate + 1);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlayer.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPlayer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPlayer.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testPlayer.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPlayer.getSubscriptionAllowed()).isEqualTo(DEFAULT_SUBSCRIPTION_ALLOWED);
    }

    @Test
    @Transactional
    public void createPlayerWithExistingId() throws Exception {
        final Player player = playerRepository.save(createEntity(em));
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player with an existing ID
        this.player.setId(player.getId());
        PlayerDTO playerDTO = playerMapper.toDto(this.player);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerMockMvc.perform(post("/admin-api/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerRepository.findAll().size();
        // set the field null
        player.setName(null);

        // Create the Player, which fails.
        PlayerDTO playerDTO = playerMapper.toDto(player);


        restPlayerMockMvc.perform(post("/admin-api/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isBadRequest());

        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerRepository.findAll().size();
        // set the field null
        player.setPhone(null);

        // Create the Player, which fails.
        PlayerDTO playerDTO = playerMapper.toDto(player);


        restPlayerMockMvc.perform(post("/admin-api/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isBadRequest());

        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerRepository.findAll().size();
        // set the field null
        player.setEmail(null);

        // Create the Player, which fails.
        PlayerDTO playerDTO = playerMapper.toDto(player);


        restPlayerMockMvc.perform(post("/admin-api/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isBadRequest());

        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlayers() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList
        restPlayerMockMvc.perform(get("/admin-api/players?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].subscriptionAllowed").value(hasItem(DEFAULT_SUBSCRIPTION_ALLOWED)));
    }

    @Test
    @Transactional
    public void getPlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get the player
        restPlayerMockMvc.perform(get("/admin-api/players/{id}", player.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(player.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.subscriptionAllowed").value(DEFAULT_SUBSCRIPTION_ALLOWED));
    }


    @Test
    @Transactional
    public void getPlayersByIdFiltering() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        UUID id = player.getId();

        defaultPlayerShouldBeFound("id.equals=" + id);
        defaultPlayerShouldNotBeFound("id.notEquals=" + id);
    }


    @Test
    @Transactional
    public void getAllPlayersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where name equals to DEFAULT_NAME
        defaultPlayerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the playerList where name equals to UPDATED_NAME
        defaultPlayerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlayersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where name not equals to DEFAULT_NAME
        defaultPlayerShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the playerList where name not equals to UPDATED_NAME
        defaultPlayerShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlayersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPlayerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the playerList where name equals to UPDATED_NAME
        defaultPlayerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlayersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where name is not null
        defaultPlayerShouldBeFound("name.specified=true");

        // Get all the playerList where name is null
        defaultPlayerShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlayersByNameContainsSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where name contains DEFAULT_NAME
        defaultPlayerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the playerList where name contains UPDATED_NAME
        defaultPlayerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlayersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where name does not contain DEFAULT_NAME
        defaultPlayerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the playerList where name does not contain UPDATED_NAME
        defaultPlayerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPlayersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where phone equals to DEFAULT_PHONE
        defaultPlayerShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the playerList where phone equals to UPDATED_PHONE
        defaultPlayerShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPlayersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where phone not equals to DEFAULT_PHONE
        defaultPlayerShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the playerList where phone not equals to UPDATED_PHONE
        defaultPlayerShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPlayersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultPlayerShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the playerList where phone equals to UPDATED_PHONE
        defaultPlayerShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPlayersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where phone is not null
        defaultPlayerShouldBeFound("phone.specified=true");

        // Get all the playerList where phone is null
        defaultPlayerShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where phone contains DEFAULT_PHONE
        defaultPlayerShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the playerList where phone contains UPDATED_PHONE
        defaultPlayerShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPlayersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where phone does not contain DEFAULT_PHONE
        defaultPlayerShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the playerList where phone does not contain UPDATED_PHONE
        defaultPlayerShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllPlayersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where email equals to DEFAULT_EMAIL
        defaultPlayerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the playerList where email equals to UPDATED_EMAIL
        defaultPlayerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPlayersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where email not equals to DEFAULT_EMAIL
        defaultPlayerShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the playerList where email not equals to UPDATED_EMAIL
        defaultPlayerShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPlayersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPlayerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the playerList where email equals to UPDATED_EMAIL
        defaultPlayerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPlayersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where email is not null
        defaultPlayerShouldBeFound("email.specified=true");

        // Get all the playerList where email is null
        defaultPlayerShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayersByEmailContainsSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where email contains DEFAULT_EMAIL
        defaultPlayerShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the playerList where email contains UPDATED_EMAIL
        defaultPlayerShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPlayersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where email does not contain DEFAULT_EMAIL
        defaultPlayerShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the playerList where email does not contain UPDATED_EMAIL
        defaultPlayerShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllPlayersByBirthdayIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where birthday equals to DEFAULT_BIRTHDAY
        defaultPlayerShouldBeFound("birthday.equals=" + DEFAULT_BIRTHDAY);

        // Get all the playerList where birthday equals to UPDATED_BIRTHDAY
        defaultPlayerShouldNotBeFound("birthday.equals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPlayersByBirthdayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where birthday not equals to DEFAULT_BIRTHDAY
        defaultPlayerShouldNotBeFound("birthday.notEquals=" + DEFAULT_BIRTHDAY);

        // Get all the playerList where birthday not equals to UPDATED_BIRTHDAY
        defaultPlayerShouldBeFound("birthday.notEquals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPlayersByBirthdayIsInShouldWork() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where birthday in DEFAULT_BIRTHDAY or UPDATED_BIRTHDAY
        defaultPlayerShouldBeFound("birthday.in=" + DEFAULT_BIRTHDAY + "," + UPDATED_BIRTHDAY);

        // Get all the playerList where birthday equals to UPDATED_BIRTHDAY
        defaultPlayerShouldNotBeFound("birthday.in=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPlayersByBirthdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where birthday is not null
        defaultPlayerShouldBeFound("birthday.specified=true");

        // Get all the playerList where birthday is null
        defaultPlayerShouldNotBeFound("birthday.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayersByBirthdayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where birthday is greater than or equal to DEFAULT_BIRTHDAY
        defaultPlayerShouldBeFound("birthday.greaterThanOrEqual=" + DEFAULT_BIRTHDAY);

        // Get all the playerList where birthday is greater than or equal to UPDATED_BIRTHDAY
        defaultPlayerShouldNotBeFound("birthday.greaterThanOrEqual=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPlayersByBirthdayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where birthday is less than or equal to DEFAULT_BIRTHDAY
        defaultPlayerShouldBeFound("birthday.lessThanOrEqual=" + DEFAULT_BIRTHDAY);

        // Get all the playerList where birthday is less than or equal to SMALLER_BIRTHDAY
        defaultPlayerShouldNotBeFound("birthday.lessThanOrEqual=" + SMALLER_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPlayersByBirthdayIsLessThanSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where birthday is less than DEFAULT_BIRTHDAY
        defaultPlayerShouldNotBeFound("birthday.lessThan=" + DEFAULT_BIRTHDAY);

        // Get all the playerList where birthday is less than UPDATED_BIRTHDAY
        defaultPlayerShouldBeFound("birthday.lessThan=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPlayersByBirthdayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where birthday is greater than DEFAULT_BIRTHDAY
        defaultPlayerShouldNotBeFound("birthday.greaterThan=" + DEFAULT_BIRTHDAY);

        // Get all the playerList where birthday is greater than SMALLER_BIRTHDAY
        defaultPlayerShouldBeFound("birthday.greaterThan=" + SMALLER_BIRTHDAY);
    }


    @Test
    @Transactional
    public void getAllPlayersByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where gender equals to DEFAULT_GENDER
        defaultPlayerShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the playerList where gender equals to UPDATED_GENDER
        defaultPlayerShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllPlayersByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where gender not equals to DEFAULT_GENDER
        defaultPlayerShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the playerList where gender not equals to UPDATED_GENDER
        defaultPlayerShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllPlayersByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultPlayerShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the playerList where gender equals to UPDATED_GENDER
        defaultPlayerShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllPlayersByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where gender is not null
        defaultPlayerShouldBeFound("gender.specified=true");

        // Get all the playerList where gender is null
        defaultPlayerShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayersBySubscriptionAllowedIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where subscriptionAllowed equals to DEFAULT_SUBSCRIPTION_ALLOWED
        defaultPlayerShouldBeFound("subscriptionAllowed.equals=" + DEFAULT_SUBSCRIPTION_ALLOWED);

        // Get all the playerList where subscriptionAllowed equals to UPDATED_SUBSCRIPTION_ALLOWED
        defaultPlayerShouldNotBeFound("subscriptionAllowed.equals=" + UPDATED_SUBSCRIPTION_ALLOWED);
    }

    @Test
    @Transactional
    public void getAllPlayersBySubscriptionAllowedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where subscriptionAllowed not equals to DEFAULT_SUBSCRIPTION_ALLOWED
        defaultPlayerShouldNotBeFound("subscriptionAllowed.notEquals=" + DEFAULT_SUBSCRIPTION_ALLOWED);

        // Get all the playerList where subscriptionAllowed not equals to UPDATED_SUBSCRIPTION_ALLOWED
        defaultPlayerShouldBeFound("subscriptionAllowed.notEquals=" + UPDATED_SUBSCRIPTION_ALLOWED);
    }

    @Test
    @Transactional
    public void getAllPlayersBySubscriptionAllowedIsInShouldWork() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where subscriptionAllowed in DEFAULT_SUBSCRIPTION_ALLOWED or UPDATED_SUBSCRIPTION_ALLOWED
        defaultPlayerShouldBeFound("subscriptionAllowed.in=" + DEFAULT_SUBSCRIPTION_ALLOWED + "," + UPDATED_SUBSCRIPTION_ALLOWED);

        // Get all the playerList where subscriptionAllowed equals to UPDATED_SUBSCRIPTION_ALLOWED
        defaultPlayerShouldNotBeFound("subscriptionAllowed.in=" + UPDATED_SUBSCRIPTION_ALLOWED);
    }

    @Test
    @Transactional
    public void getAllPlayersBySubscriptionAllowedIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where subscriptionAllowed is not null
        defaultPlayerShouldBeFound("subscriptionAllowed.specified=true");

        // Get all the playerList where subscriptionAllowed is null
        defaultPlayerShouldNotBeFound("subscriptionAllowed.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayersByInternalUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User internalUser = player.getInternalUser();
        playerRepository.saveAndFlush(player);
        UUID internalUserId = internalUser.getId();

        // Get all the playerList where internalUser equals to internalUserId
        defaultPlayerShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the playerList where internalUser equals to internalUserId + 1
        defaultPlayerShouldNotBeFound("internalUserId.equals=" + UUID.fromString("bd1c8121-3cfc-4a1f-85d8-258f83e2955c"));
    }


    @Test
    @Transactional
    public void getAllPlayersByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        Company company = CompanyResourceIT.createEntity(em);
        em.persist(company);
        em.flush();
        player.setCompany(company);
        playerRepository.saveAndFlush(player);
        UUID companyId = company.getId();

        // Get all the playerList where company equals to companyId
        defaultPlayerShouldBeFound("companyId.equals=" + companyId);

        // Get all the playerList where company equals to companyId + 1
        defaultPlayerShouldNotBeFound("companyId.equals=" + UUID.fromString("6dfecba8-39bb-43ef-9611-76c69228a7f8"));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlayerShouldBeFound(String filter) throws Exception {
        restPlayerMockMvc.perform(get("/admin-api/players?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].subscriptionAllowed").value(hasItem(DEFAULT_SUBSCRIPTION_ALLOWED)));

        // Check, that the count call also returns 1
        restPlayerMockMvc.perform(get("/admin-api/players/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlayerShouldNotBeFound(String filter) throws Exception {
        restPlayerMockMvc.perform(get("/admin-api/players?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlayerMockMvc.perform(get("/admin-api/players/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPlayer() throws Exception {
        // Get the player
        restPlayerMockMvc.perform(get("/admin-api/players/{id}", UUID.fromString("6c7c5653-09cd-4178-8609-0b4cdd5692f5")))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Update the player
        Player updatedPlayer = playerRepository.findById(player.getId()).get();
        // Disconnect from session so that the updates on updatedPlayer are not directly saved in db
        em.detach(updatedPlayer);
        updatedPlayer
            .setName(UPDATED_NAME)
            .setPhone(UPDATED_PHONE)
            .setEmail(UPDATED_EMAIL)
            .setBirthday(UPDATED_BIRTHDAY)
            .setGender(UPDATED_GENDER)
            .setSubscriptionAllowed(UPDATED_SUBSCRIPTION_ALLOWED);
        PlayerDTO playerDTO = playerMapper.toDto(updatedPlayer);

        restPlayerMockMvc.perform(put("/admin-api/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isOk());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlayer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPlayer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPlayer.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testPlayer.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPlayer.getSubscriptionAllowed()).isEqualTo(UPDATED_SUBSCRIPTION_ALLOWED);
    }

    @Test
    @Transactional
    public void updateNonExistingPlayer() throws Exception {
        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Create the Player
        PlayerDTO playerDTO = playerMapper.toDto(player);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerMockMvc.perform(put("/admin-api/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        int databaseSizeBeforeDelete = playerRepository.findAll().size();

        // Delete the player
        restPlayerMockMvc.perform(delete("/admin-api/players/{id}", player.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Player> playerList = playerRepository.findAll();

        // Due to commented delete service
        // assertThat(playerList).hasSize(databaseSizeBeforeDelete - 1);
        assertThat(playerList).hasSize(databaseSizeBeforeDelete);
    }
}
