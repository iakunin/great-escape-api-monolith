package com.greatescape.api.monolith.web.rest;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.repository.SlotRepository;
import com.greatescape.api.monolith.service.SlotQueryService;
import com.greatescape.api.monolith.service.SlotService;
import com.greatescape.api.monolith.service.dto.SlotDTO;
import com.greatescape.api.monolith.service.mapper.SlotMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.persistence.EntityManager;
import static org.assertj.core.api.Assertions.assertThat;
import org.hamcrest.Matchers;
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
 * Integration tests for the {@link SlotResource} REST controller.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SlotResourceIT {

    private static final Instant DEFAULT_DATE_TIME_LOCAL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TIME_LOCAL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ZonedDateTime DEFAULT_DATE_TIME_WITH_TIME_ZONE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME_WITH_TIME_ZONE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE_TIME_WITH_TIME_ZONE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_IS_AVAILABLE = false;
    private static final Boolean UPDATED_IS_AVAILABLE = true;

    private static final Integer DEFAULT_PRICE = 0;
    private static final Integer UPDATED_PRICE = 1;
    private static final Integer SMALLER_PRICE = 0 - 1;

    private static final Integer DEFAULT_DISCOUNT_IN_PERCENTS = 0;
    private static final Integer UPDATED_DISCOUNT_IN_PERCENTS = 1;
    private static final Integer SMALLER_DISCOUNT_IN_PERCENTS = 0 - 1;

    private static final Integer DEFAULT_COMMISSION_IN_PERCENTS = 0;
    private static final Integer UPDATED_COMMISSION_IN_PERCENTS = 1;
    private static final Integer SMALLER_COMMISSION_IN_PERCENTS = 0 - 1;

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_STATE = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_STATE = "BBBBBBBBBB";

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SlotMapper slotMapper;

    @Autowired
    private SlotService slotService;

    @Autowired
    private SlotQueryService slotQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSlotMockMvc;

    private Slot slot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Slot createEntity(EntityManager em) {
        Slot slot = new Slot()
            .dateTimeLocal(DEFAULT_DATE_TIME_LOCAL)
            .dateTimeWithTimeZone(DEFAULT_DATE_TIME_WITH_TIME_ZONE)
            .isAvailable(DEFAULT_IS_AVAILABLE)
            .price(DEFAULT_PRICE)
            .discountInPercents(DEFAULT_DISCOUNT_IN_PERCENTS)
            .commissionInPercents(DEFAULT_COMMISSION_IN_PERCENTS)
            .externalId(DEFAULT_EXTERNAL_ID)
            .externalState(DEFAULT_EXTERNAL_STATE);
        // Add required entity
        Quest quest;
        if (TestUtil.findAll(em, Quest.class).isEmpty()) {
            quest = QuestResourceIT.createEntity(em);
            em.persist(quest);
            em.flush();
        } else {
            quest = TestUtil.findAll(em, Quest.class).get(0);
        }
        slot.setQuest(quest);
        return slot;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Slot createUpdatedEntity(EntityManager em) {
        Slot slot = new Slot()
            .dateTimeLocal(UPDATED_DATE_TIME_LOCAL)
            .dateTimeWithTimeZone(UPDATED_DATE_TIME_WITH_TIME_ZONE)
            .isAvailable(UPDATED_IS_AVAILABLE)
            .price(UPDATED_PRICE)
            .discountInPercents(UPDATED_DISCOUNT_IN_PERCENTS)
            .commissionInPercents(UPDATED_COMMISSION_IN_PERCENTS)
            .externalId(UPDATED_EXTERNAL_ID)
            .externalState(UPDATED_EXTERNAL_STATE);
        // Add required entity
        Quest quest;
        if (TestUtil.findAll(em, Quest.class).isEmpty()) {
            quest = QuestResourceIT.createUpdatedEntity(em);
            em.persist(quest);
            em.flush();
        } else {
            quest = TestUtil.findAll(em, Quest.class).get(0);
        }
        slot.setQuest(quest);
        return slot;
    }

    @BeforeEach
    public void initTest() {
        slot = createEntity(em);
    }

    @Test
    @Transactional
    public void createSlot() throws Exception {
        int databaseSizeBeforeCreate = slotRepository.findAll().size();
        // Create the Slot
        SlotDTO slotDTO = slotMapper.toDto(slot);
        restSlotMockMvc.perform(post("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isCreated());

        // Validate the Slot in the database
        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeCreate + 1);
        Slot testSlot = slotList.get(slotList.size() - 1);
        assertThat(testSlot.getDateTimeLocal()).isEqualTo(DEFAULT_DATE_TIME_LOCAL);
        assertThat(testSlot.getDateTimeWithTimeZone()).isEqualTo(DEFAULT_DATE_TIME_WITH_TIME_ZONE);
        assertThat(testSlot.isIsAvailable()).isEqualTo(DEFAULT_IS_AVAILABLE);
        assertThat(testSlot.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testSlot.getDiscountInPercents()).isEqualTo(DEFAULT_DISCOUNT_IN_PERCENTS);
        assertThat(testSlot.getCommissionInPercents()).isEqualTo(DEFAULT_COMMISSION_IN_PERCENTS);
        assertThat(testSlot.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);
        assertThat(testSlot.getExternalState()).isEqualTo(DEFAULT_EXTERNAL_STATE);
    }

    @Test
    @Transactional
    public void createSlotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = slotRepository.findAll().size();

        // Create the Slot with an existing ID
        slot.setId(1L);
        SlotDTO slotDTO = slotMapper.toDto(slot);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlotMockMvc.perform(post("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Slot in the database
        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateTimeLocalIsRequired() throws Exception {
        int databaseSizeBeforeTest = slotRepository.findAll().size();
        // set the field null
        slot.setDateTimeLocal(null);

        // Create the Slot, which fails.
        SlotDTO slotDTO = slotMapper.toDto(slot);


        restSlotMockMvc.perform(post("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateTimeWithTimeZoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = slotRepository.findAll().size();
        // set the field null
        slot.setDateTimeWithTimeZone(null);

        // Create the Slot, which fails.
        SlotDTO slotDTO = slotMapper.toDto(slot);


        restSlotMockMvc.perform(post("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = slotRepository.findAll().size();
        // set the field null
        slot.setIsAvailable(null);

        // Create the Slot, which fails.
        SlotDTO slotDTO = slotMapper.toDto(slot);


        restSlotMockMvc.perform(post("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = slotRepository.findAll().size();
        // set the field null
        slot.setPrice(null);

        // Create the Slot, which fails.
        SlotDTO slotDTO = slotMapper.toDto(slot);


        restSlotMockMvc.perform(post("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExternalIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = slotRepository.findAll().size();
        // set the field null
        slot.setExternalId(null);

        // Create the Slot, which fails.
        SlotDTO slotDTO = slotMapper.toDto(slot);


        restSlotMockMvc.perform(post("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSlots() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList
        restSlotMockMvc.perform(get("/api/slots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slot.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTimeLocal").value(hasItem(DEFAULT_DATE_TIME_LOCAL.toString())))
            .andExpect(jsonPath("$.[*].dateTimeWithTimeZone").value(Matchers.hasItem(TestUtil.sameInstant(DEFAULT_DATE_TIME_WITH_TIME_ZONE))))
            .andExpect(jsonPath("$.[*].isAvailable").value(hasItem(DEFAULT_IS_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].discountInPercents").value(hasItem(DEFAULT_DISCOUNT_IN_PERCENTS)))
            .andExpect(jsonPath("$.[*].commissionInPercents").value(hasItem(DEFAULT_COMMISSION_IN_PERCENTS)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].externalState").value(hasItem(DEFAULT_EXTERNAL_STATE.toString())));
    }

    @Test
    @Transactional
    public void getSlot() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get the slot
        restSlotMockMvc.perform(get("/api/slots/{id}", slot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(slot.getId().intValue()))
            .andExpect(jsonPath("$.dateTimeLocal").value(DEFAULT_DATE_TIME_LOCAL.toString()))
            .andExpect(jsonPath("$.dateTimeWithTimeZone").value(TestUtil.sameInstant(DEFAULT_DATE_TIME_WITH_TIME_ZONE)))
            .andExpect(jsonPath("$.isAvailable").value(DEFAULT_IS_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.discountInPercents").value(DEFAULT_DISCOUNT_IN_PERCENTS))
            .andExpect(jsonPath("$.commissionInPercents").value(DEFAULT_COMMISSION_IN_PERCENTS))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.externalState").value(DEFAULT_EXTERNAL_STATE.toString()));
    }


    @Test
    @Transactional
    public void getSlotsByIdFiltering() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        Long id = slot.getId();

        defaultSlotShouldBeFound("id.equals=" + id);
        defaultSlotShouldNotBeFound("id.notEquals=" + id);

        defaultSlotShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSlotShouldNotBeFound("id.greaterThan=" + id);

        defaultSlotShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSlotShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSlotsByDateTimeLocalIsEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where dateTimeLocal equals to DEFAULT_DATE_TIME_LOCAL
        defaultSlotShouldBeFound("dateTimeLocal.equals=" + DEFAULT_DATE_TIME_LOCAL);

        // Get all the slotList where dateTimeLocal equals to UPDATED_DATE_TIME_LOCAL
        defaultSlotShouldNotBeFound("dateTimeLocal.equals=" + UPDATED_DATE_TIME_LOCAL);
    }

    @Test
    @Transactional
    public void getAllSlotsByDateTimeLocalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where dateTimeLocal not equals to DEFAULT_DATE_TIME_LOCAL
        defaultSlotShouldNotBeFound("dateTimeLocal.notEquals=" + DEFAULT_DATE_TIME_LOCAL);

        // Get all the slotList where dateTimeLocal not equals to UPDATED_DATE_TIME_LOCAL
        defaultSlotShouldBeFound("dateTimeLocal.notEquals=" + UPDATED_DATE_TIME_LOCAL);
    }

    @Test
    @Transactional
    public void getAllSlotsByDateTimeLocalIsInShouldWork() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where dateTimeLocal in DEFAULT_DATE_TIME_LOCAL or UPDATED_DATE_TIME_LOCAL
        defaultSlotShouldBeFound("dateTimeLocal.in=" + DEFAULT_DATE_TIME_LOCAL + "," + UPDATED_DATE_TIME_LOCAL);

        // Get all the slotList where dateTimeLocal equals to UPDATED_DATE_TIME_LOCAL
        defaultSlotShouldNotBeFound("dateTimeLocal.in=" + UPDATED_DATE_TIME_LOCAL);
    }

    @Test
    @Transactional
    public void getAllSlotsByDateTimeLocalIsNullOrNotNull() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where dateTimeLocal is not null
        defaultSlotShouldBeFound("dateTimeLocal.specified=true");

        // Get all the slotList where dateTimeLocal is null
        defaultSlotShouldNotBeFound("dateTimeLocal.specified=false");
    }

    @Test
    @Transactional
    public void getAllSlotsByDateTimeWithTimeZoneIsEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where dateTimeWithTimeZone equals to DEFAULT_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldBeFound("dateTimeWithTimeZone.equals=" + DEFAULT_DATE_TIME_WITH_TIME_ZONE);

        // Get all the slotList where dateTimeWithTimeZone equals to UPDATED_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldNotBeFound("dateTimeWithTimeZone.equals=" + UPDATED_DATE_TIME_WITH_TIME_ZONE);
    }

    @Test
    @Transactional
    public void getAllSlotsByDateTimeWithTimeZoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where dateTimeWithTimeZone not equals to DEFAULT_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldNotBeFound("dateTimeWithTimeZone.notEquals=" + DEFAULT_DATE_TIME_WITH_TIME_ZONE);

        // Get all the slotList where dateTimeWithTimeZone not equals to UPDATED_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldBeFound("dateTimeWithTimeZone.notEquals=" + UPDATED_DATE_TIME_WITH_TIME_ZONE);
    }

    @Test
    @Transactional
    public void getAllSlotsByDateTimeWithTimeZoneIsInShouldWork() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where dateTimeWithTimeZone in DEFAULT_DATE_TIME_WITH_TIME_ZONE or UPDATED_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldBeFound("dateTimeWithTimeZone.in=" + DEFAULT_DATE_TIME_WITH_TIME_ZONE + "," + UPDATED_DATE_TIME_WITH_TIME_ZONE);

        // Get all the slotList where dateTimeWithTimeZone equals to UPDATED_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldNotBeFound("dateTimeWithTimeZone.in=" + UPDATED_DATE_TIME_WITH_TIME_ZONE);
    }

    @Test
    @Transactional
    public void getAllSlotsByDateTimeWithTimeZoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where dateTimeWithTimeZone is not null
        defaultSlotShouldBeFound("dateTimeWithTimeZone.specified=true");

        // Get all the slotList where dateTimeWithTimeZone is null
        defaultSlotShouldNotBeFound("dateTimeWithTimeZone.specified=false");
    }

    @Test
    @Transactional
    public void getAllSlotsByDateTimeWithTimeZoneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where dateTimeWithTimeZone is greater than or equal to DEFAULT_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldBeFound("dateTimeWithTimeZone.greaterThanOrEqual=" + DEFAULT_DATE_TIME_WITH_TIME_ZONE);

        // Get all the slotList where dateTimeWithTimeZone is greater than or equal to UPDATED_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldNotBeFound("dateTimeWithTimeZone.greaterThanOrEqual=" + UPDATED_DATE_TIME_WITH_TIME_ZONE);
    }

    @Test
    @Transactional
    public void getAllSlotsByDateTimeWithTimeZoneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where dateTimeWithTimeZone is less than or equal to DEFAULT_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldBeFound("dateTimeWithTimeZone.lessThanOrEqual=" + DEFAULT_DATE_TIME_WITH_TIME_ZONE);

        // Get all the slotList where dateTimeWithTimeZone is less than or equal to SMALLER_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldNotBeFound("dateTimeWithTimeZone.lessThanOrEqual=" + SMALLER_DATE_TIME_WITH_TIME_ZONE);
    }

    @Test
    @Transactional
    public void getAllSlotsByDateTimeWithTimeZoneIsLessThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where dateTimeWithTimeZone is less than DEFAULT_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldNotBeFound("dateTimeWithTimeZone.lessThan=" + DEFAULT_DATE_TIME_WITH_TIME_ZONE);

        // Get all the slotList where dateTimeWithTimeZone is less than UPDATED_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldBeFound("dateTimeWithTimeZone.lessThan=" + UPDATED_DATE_TIME_WITH_TIME_ZONE);
    }

    @Test
    @Transactional
    public void getAllSlotsByDateTimeWithTimeZoneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where dateTimeWithTimeZone is greater than DEFAULT_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldNotBeFound("dateTimeWithTimeZone.greaterThan=" + DEFAULT_DATE_TIME_WITH_TIME_ZONE);

        // Get all the slotList where dateTimeWithTimeZone is greater than SMALLER_DATE_TIME_WITH_TIME_ZONE
        defaultSlotShouldBeFound("dateTimeWithTimeZone.greaterThan=" + SMALLER_DATE_TIME_WITH_TIME_ZONE);
    }


    @Test
    @Transactional
    public void getAllSlotsByIsAvailableIsEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where isAvailable equals to DEFAULT_IS_AVAILABLE
        defaultSlotShouldBeFound("isAvailable.equals=" + DEFAULT_IS_AVAILABLE);

        // Get all the slotList where isAvailable equals to UPDATED_IS_AVAILABLE
        defaultSlotShouldNotBeFound("isAvailable.equals=" + UPDATED_IS_AVAILABLE);
    }

    @Test
    @Transactional
    public void getAllSlotsByIsAvailableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where isAvailable not equals to DEFAULT_IS_AVAILABLE
        defaultSlotShouldNotBeFound("isAvailable.notEquals=" + DEFAULT_IS_AVAILABLE);

        // Get all the slotList where isAvailable not equals to UPDATED_IS_AVAILABLE
        defaultSlotShouldBeFound("isAvailable.notEquals=" + UPDATED_IS_AVAILABLE);
    }

    @Test
    @Transactional
    public void getAllSlotsByIsAvailableIsInShouldWork() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where isAvailable in DEFAULT_IS_AVAILABLE or UPDATED_IS_AVAILABLE
        defaultSlotShouldBeFound("isAvailable.in=" + DEFAULT_IS_AVAILABLE + "," + UPDATED_IS_AVAILABLE);

        // Get all the slotList where isAvailable equals to UPDATED_IS_AVAILABLE
        defaultSlotShouldNotBeFound("isAvailable.in=" + UPDATED_IS_AVAILABLE);
    }

    @Test
    @Transactional
    public void getAllSlotsByIsAvailableIsNullOrNotNull() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where isAvailable is not null
        defaultSlotShouldBeFound("isAvailable.specified=true");

        // Get all the slotList where isAvailable is null
        defaultSlotShouldNotBeFound("isAvailable.specified=false");
    }

    @Test
    @Transactional
    public void getAllSlotsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where price equals to DEFAULT_PRICE
        defaultSlotShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the slotList where price equals to UPDATED_PRICE
        defaultSlotShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllSlotsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where price not equals to DEFAULT_PRICE
        defaultSlotShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the slotList where price not equals to UPDATED_PRICE
        defaultSlotShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllSlotsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultSlotShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the slotList where price equals to UPDATED_PRICE
        defaultSlotShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllSlotsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where price is not null
        defaultSlotShouldBeFound("price.specified=true");

        // Get all the slotList where price is null
        defaultSlotShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllSlotsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where price is greater than or equal to DEFAULT_PRICE
        defaultSlotShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the slotList where price is greater than or equal to UPDATED_PRICE
        defaultSlotShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllSlotsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where price is less than or equal to DEFAULT_PRICE
        defaultSlotShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the slotList where price is less than or equal to SMALLER_PRICE
        defaultSlotShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllSlotsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where price is less than DEFAULT_PRICE
        defaultSlotShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the slotList where price is less than UPDATED_PRICE
        defaultSlotShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllSlotsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where price is greater than DEFAULT_PRICE
        defaultSlotShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the slotList where price is greater than SMALLER_PRICE
        defaultSlotShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllSlotsByDiscountInPercentsIsEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where discountInPercents equals to DEFAULT_DISCOUNT_IN_PERCENTS
        defaultSlotShouldBeFound("discountInPercents.equals=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the slotList where discountInPercents equals to UPDATED_DISCOUNT_IN_PERCENTS
        defaultSlotShouldNotBeFound("discountInPercents.equals=" + UPDATED_DISCOUNT_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllSlotsByDiscountInPercentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where discountInPercents not equals to DEFAULT_DISCOUNT_IN_PERCENTS
        defaultSlotShouldNotBeFound("discountInPercents.notEquals=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the slotList where discountInPercents not equals to UPDATED_DISCOUNT_IN_PERCENTS
        defaultSlotShouldBeFound("discountInPercents.notEquals=" + UPDATED_DISCOUNT_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllSlotsByDiscountInPercentsIsInShouldWork() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where discountInPercents in DEFAULT_DISCOUNT_IN_PERCENTS or UPDATED_DISCOUNT_IN_PERCENTS
        defaultSlotShouldBeFound("discountInPercents.in=" + DEFAULT_DISCOUNT_IN_PERCENTS + "," + UPDATED_DISCOUNT_IN_PERCENTS);

        // Get all the slotList where discountInPercents equals to UPDATED_DISCOUNT_IN_PERCENTS
        defaultSlotShouldNotBeFound("discountInPercents.in=" + UPDATED_DISCOUNT_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllSlotsByDiscountInPercentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where discountInPercents is not null
        defaultSlotShouldBeFound("discountInPercents.specified=true");

        // Get all the slotList where discountInPercents is null
        defaultSlotShouldNotBeFound("discountInPercents.specified=false");
    }

    @Test
    @Transactional
    public void getAllSlotsByDiscountInPercentsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where discountInPercents is greater than or equal to DEFAULT_DISCOUNT_IN_PERCENTS
        defaultSlotShouldBeFound("discountInPercents.greaterThanOrEqual=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the slotList where discountInPercents is greater than or equal to (DEFAULT_DISCOUNT_IN_PERCENTS + 1)
        defaultSlotShouldNotBeFound("discountInPercents.greaterThanOrEqual=" + (DEFAULT_DISCOUNT_IN_PERCENTS + 1));
    }

    @Test
    @Transactional
    public void getAllSlotsByDiscountInPercentsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where discountInPercents is less than or equal to DEFAULT_DISCOUNT_IN_PERCENTS
        defaultSlotShouldBeFound("discountInPercents.lessThanOrEqual=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the slotList where discountInPercents is less than or equal to SMALLER_DISCOUNT_IN_PERCENTS
        defaultSlotShouldNotBeFound("discountInPercents.lessThanOrEqual=" + SMALLER_DISCOUNT_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllSlotsByDiscountInPercentsIsLessThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where discountInPercents is less than DEFAULT_DISCOUNT_IN_PERCENTS
        defaultSlotShouldNotBeFound("discountInPercents.lessThan=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the slotList where discountInPercents is less than (DEFAULT_DISCOUNT_IN_PERCENTS + 1)
        defaultSlotShouldBeFound("discountInPercents.lessThan=" + (DEFAULT_DISCOUNT_IN_PERCENTS + 1));
    }

    @Test
    @Transactional
    public void getAllSlotsByDiscountInPercentsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where discountInPercents is greater than DEFAULT_DISCOUNT_IN_PERCENTS
        defaultSlotShouldNotBeFound("discountInPercents.greaterThan=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the slotList where discountInPercents is greater than SMALLER_DISCOUNT_IN_PERCENTS
        defaultSlotShouldBeFound("discountInPercents.greaterThan=" + SMALLER_DISCOUNT_IN_PERCENTS);
    }


    @Test
    @Transactional
    public void getAllSlotsByCommissionInPercentsIsEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where commissionInPercents equals to DEFAULT_COMMISSION_IN_PERCENTS
        defaultSlotShouldBeFound("commissionInPercents.equals=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the slotList where commissionInPercents equals to UPDATED_COMMISSION_IN_PERCENTS
        defaultSlotShouldNotBeFound("commissionInPercents.equals=" + UPDATED_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllSlotsByCommissionInPercentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where commissionInPercents not equals to DEFAULT_COMMISSION_IN_PERCENTS
        defaultSlotShouldNotBeFound("commissionInPercents.notEquals=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the slotList where commissionInPercents not equals to UPDATED_COMMISSION_IN_PERCENTS
        defaultSlotShouldBeFound("commissionInPercents.notEquals=" + UPDATED_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllSlotsByCommissionInPercentsIsInShouldWork() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where commissionInPercents in DEFAULT_COMMISSION_IN_PERCENTS or UPDATED_COMMISSION_IN_PERCENTS
        defaultSlotShouldBeFound("commissionInPercents.in=" + DEFAULT_COMMISSION_IN_PERCENTS + "," + UPDATED_COMMISSION_IN_PERCENTS);

        // Get all the slotList where commissionInPercents equals to UPDATED_COMMISSION_IN_PERCENTS
        defaultSlotShouldNotBeFound("commissionInPercents.in=" + UPDATED_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllSlotsByCommissionInPercentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where commissionInPercents is not null
        defaultSlotShouldBeFound("commissionInPercents.specified=true");

        // Get all the slotList where commissionInPercents is null
        defaultSlotShouldNotBeFound("commissionInPercents.specified=false");
    }

    @Test
    @Transactional
    public void getAllSlotsByCommissionInPercentsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where commissionInPercents is greater than or equal to DEFAULT_COMMISSION_IN_PERCENTS
        defaultSlotShouldBeFound("commissionInPercents.greaterThanOrEqual=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the slotList where commissionInPercents is greater than or equal to (DEFAULT_COMMISSION_IN_PERCENTS + 1)
        defaultSlotShouldNotBeFound("commissionInPercents.greaterThanOrEqual=" + (DEFAULT_COMMISSION_IN_PERCENTS + 1));
    }

    @Test
    @Transactional
    public void getAllSlotsByCommissionInPercentsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where commissionInPercents is less than or equal to DEFAULT_COMMISSION_IN_PERCENTS
        defaultSlotShouldBeFound("commissionInPercents.lessThanOrEqual=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the slotList where commissionInPercents is less than or equal to SMALLER_COMMISSION_IN_PERCENTS
        defaultSlotShouldNotBeFound("commissionInPercents.lessThanOrEqual=" + SMALLER_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllSlotsByCommissionInPercentsIsLessThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where commissionInPercents is less than DEFAULT_COMMISSION_IN_PERCENTS
        defaultSlotShouldNotBeFound("commissionInPercents.lessThan=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the slotList where commissionInPercents is less than (DEFAULT_COMMISSION_IN_PERCENTS + 1)
        defaultSlotShouldBeFound("commissionInPercents.lessThan=" + (DEFAULT_COMMISSION_IN_PERCENTS + 1));
    }

    @Test
    @Transactional
    public void getAllSlotsByCommissionInPercentsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where commissionInPercents is greater than DEFAULT_COMMISSION_IN_PERCENTS
        defaultSlotShouldNotBeFound("commissionInPercents.greaterThan=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the slotList where commissionInPercents is greater than SMALLER_COMMISSION_IN_PERCENTS
        defaultSlotShouldBeFound("commissionInPercents.greaterThan=" + SMALLER_COMMISSION_IN_PERCENTS);
    }


    @Test
    @Transactional
    public void getAllSlotsByExternalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where externalId equals to DEFAULT_EXTERNAL_ID
        defaultSlotShouldBeFound("externalId.equals=" + DEFAULT_EXTERNAL_ID);

        // Get all the slotList where externalId equals to UPDATED_EXTERNAL_ID
        defaultSlotShouldNotBeFound("externalId.equals=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void getAllSlotsByExternalIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where externalId not equals to DEFAULT_EXTERNAL_ID
        defaultSlotShouldNotBeFound("externalId.notEquals=" + DEFAULT_EXTERNAL_ID);

        // Get all the slotList where externalId not equals to UPDATED_EXTERNAL_ID
        defaultSlotShouldBeFound("externalId.notEquals=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void getAllSlotsByExternalIdIsInShouldWork() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where externalId in DEFAULT_EXTERNAL_ID or UPDATED_EXTERNAL_ID
        defaultSlotShouldBeFound("externalId.in=" + DEFAULT_EXTERNAL_ID + "," + UPDATED_EXTERNAL_ID);

        // Get all the slotList where externalId equals to UPDATED_EXTERNAL_ID
        defaultSlotShouldNotBeFound("externalId.in=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void getAllSlotsByExternalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where externalId is not null
        defaultSlotShouldBeFound("externalId.specified=true");

        // Get all the slotList where externalId is null
        defaultSlotShouldNotBeFound("externalId.specified=false");
    }
                @Test
    @Transactional
    public void getAllSlotsByExternalIdContainsSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where externalId contains DEFAULT_EXTERNAL_ID
        defaultSlotShouldBeFound("externalId.contains=" + DEFAULT_EXTERNAL_ID);

        // Get all the slotList where externalId contains UPDATED_EXTERNAL_ID
        defaultSlotShouldNotBeFound("externalId.contains=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void getAllSlotsByExternalIdNotContainsSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where externalId does not contain DEFAULT_EXTERNAL_ID
        defaultSlotShouldNotBeFound("externalId.doesNotContain=" + DEFAULT_EXTERNAL_ID);

        // Get all the slotList where externalId does not contain UPDATED_EXTERNAL_ID
        defaultSlotShouldBeFound("externalId.doesNotContain=" + UPDATED_EXTERNAL_ID);
    }


    @Test
    @Transactional
    public void getAllSlotsByQuestIsEqualToSomething() throws Exception {
        // Get already existing entity
        Quest quest = slot.getQuest();
        slotRepository.saveAndFlush(slot);
        Long questId = quest.getId();

        // Get all the slotList where quest equals to questId
        defaultSlotShouldBeFound("questId.equals=" + questId);

        // Get all the slotList where quest equals to questId + 1
        defaultSlotShouldNotBeFound("questId.equals=" + (questId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSlotShouldBeFound(String filter) throws Exception {
        restSlotMockMvc.perform(get("/api/slots?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slot.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTimeLocal").value(hasItem(DEFAULT_DATE_TIME_LOCAL.toString())))
            .andExpect(jsonPath("$.[*].dateTimeWithTimeZone").value(Matchers.hasItem(TestUtil.sameInstant(DEFAULT_DATE_TIME_WITH_TIME_ZONE))))
            .andExpect(jsonPath("$.[*].isAvailable").value(hasItem(DEFAULT_IS_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].discountInPercents").value(hasItem(DEFAULT_DISCOUNT_IN_PERCENTS)))
            .andExpect(jsonPath("$.[*].commissionInPercents").value(hasItem(DEFAULT_COMMISSION_IN_PERCENTS)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].externalState").value(hasItem(DEFAULT_EXTERNAL_STATE.toString())));

        // Check, that the count call also returns 1
        restSlotMockMvc.perform(get("/api/slots/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSlotShouldNotBeFound(String filter) throws Exception {
        restSlotMockMvc.perform(get("/api/slots?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSlotMockMvc.perform(get("/api/slots/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSlot() throws Exception {
        // Get the slot
        restSlotMockMvc.perform(get("/api/slots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSlot() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        int databaseSizeBeforeUpdate = slotRepository.findAll().size();

        // Update the slot
        Slot updatedSlot = slotRepository.findById(slot.getId()).get();
        // Disconnect from session so that the updates on updatedSlot are not directly saved in db
        em.detach(updatedSlot);
        updatedSlot
            .dateTimeLocal(UPDATED_DATE_TIME_LOCAL)
            .dateTimeWithTimeZone(UPDATED_DATE_TIME_WITH_TIME_ZONE)
            .isAvailable(UPDATED_IS_AVAILABLE)
            .price(UPDATED_PRICE)
            .discountInPercents(UPDATED_DISCOUNT_IN_PERCENTS)
            .commissionInPercents(UPDATED_COMMISSION_IN_PERCENTS)
            .externalId(UPDATED_EXTERNAL_ID)
            .externalState(UPDATED_EXTERNAL_STATE);
        SlotDTO slotDTO = slotMapper.toDto(updatedSlot);

        restSlotMockMvc.perform(put("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isOk());

        // Validate the Slot in the database
        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeUpdate);
        Slot testSlot = slotList.get(slotList.size() - 1);
        assertThat(testSlot.getDateTimeLocal()).isEqualTo(UPDATED_DATE_TIME_LOCAL);
        assertThat(testSlot.getDateTimeWithTimeZone()).isEqualTo(UPDATED_DATE_TIME_WITH_TIME_ZONE);
        assertThat(testSlot.isIsAvailable()).isEqualTo(UPDATED_IS_AVAILABLE);
        assertThat(testSlot.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSlot.getDiscountInPercents()).isEqualTo(UPDATED_DISCOUNT_IN_PERCENTS);
        assertThat(testSlot.getCommissionInPercents()).isEqualTo(UPDATED_COMMISSION_IN_PERCENTS);
        assertThat(testSlot.getExternalId()).isEqualTo(UPDATED_EXTERNAL_ID);
        assertThat(testSlot.getExternalState()).isEqualTo(UPDATED_EXTERNAL_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSlot() throws Exception {
        int databaseSizeBeforeUpdate = slotRepository.findAll().size();

        // Create the Slot
        SlotDTO slotDTO = slotMapper.toDto(slot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlotMockMvc.perform(put("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Slot in the database
        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSlot() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        int databaseSizeBeforeDelete = slotRepository.findAll().size();

        // Delete the slot
        restSlotMockMvc.perform(delete("/api/slots/{id}", slot.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
