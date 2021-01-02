package com.greatescape.api.monolith.web.rest.admin;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.enumeration.BookingStatus;
import com.greatescape.api.monolith.repository.BookingRepository;
import com.greatescape.api.monolith.security.AuthoritiesConstants;
import com.greatescape.api.monolith.service.BookingAdminService;
import com.greatescape.api.monolith.service.BookingQueryService;
import com.greatescape.api.monolith.service.dto.BookingDTO;
import com.greatescape.api.monolith.service.mapper.BookingMapper;
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
 * Integration tests for the {@link BookingResource} REST controller.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
@AutoConfigureMockMvc
@WithMockUser(username="admin",authorities=AuthoritiesConstants.ADMIN)
public class BookingResourceIT {

    private static final BookingStatus DEFAULT_STATUS = BookingStatus.NEW;
    private static final BookingStatus UPDATED_STATUS = BookingStatus.CONFIRMED;

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;
    private static final Integer SMALLER_PRICE = 1 - 1;

    private static final Integer DEFAULT_DISCOUNT_IN_PERCENTS = 0;
    private static final Integer UPDATED_DISCOUNT_IN_PERCENTS = 1;
    private static final Integer SMALLER_DISCOUNT_IN_PERCENTS = 0 - 1;

    private static final Integer DEFAULT_COMMISSION_IN_PERCENTS = 0;
    private static final Integer UPDATED_COMMISSION_IN_PERCENTS = 1;
    private static final Integer SMALLER_COMMISSION_IN_PERCENTS = 0 - 1;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private BookingAdminService bookingService;

    @Autowired
    private BookingQueryService bookingQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingMockMvc;

    private Booking booking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createEntity(EntityManager em) {
        Booking booking = new Booking()
            .setStatus(DEFAULT_STATUS)
            .setPrice(DEFAULT_PRICE)
            .setDiscountInPercents(DEFAULT_DISCOUNT_IN_PERCENTS)
            .setCommissionInPercents(DEFAULT_COMMISSION_IN_PERCENTS);
        // Add required entity
        Slot slot;
        if (TestUtil.findAll(em, Slot.class).isEmpty()) {
            slot = SlotResourceIT.createEntity(em);
            em.persist(slot);
            em.flush();
        } else {
            slot = TestUtil.findAll(em, Slot.class).get(0);
        }
        booking.setSlot(slot);
        // Add required entity
        Quest quest;
        if (TestUtil.findAll(em, Quest.class).isEmpty()) {
            quest = QuestResourceIT.createEntity(em);
            em.persist(quest);
            em.flush();
        } else {
            quest = TestUtil.findAll(em, Quest.class).get(0);
        }
        booking.setQuest(quest);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        booking.setPlayer(player);
        return booking;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createUpdatedEntity(EntityManager em) {
        Booking booking = new Booking()
            .setStatus(UPDATED_STATUS)
            .setPrice(UPDATED_PRICE)
            .setDiscountInPercents(UPDATED_DISCOUNT_IN_PERCENTS)
            .setCommissionInPercents(UPDATED_COMMISSION_IN_PERCENTS);
        // Add required entity
        Slot slot;
        if (TestUtil.findAll(em, Slot.class).isEmpty()) {
            slot = SlotResourceIT.createUpdatedEntity(em);
            em.persist(slot);
            em.flush();
        } else {
            slot = TestUtil.findAll(em, Slot.class).get(0);
        }
        booking.setSlot(slot);
        // Add required entity
        Quest quest;
        if (TestUtil.findAll(em, Quest.class).isEmpty()) {
            quest = QuestResourceIT.createUpdatedEntity(em);
            em.persist(quest);
            em.flush();
        } else {
            quest = TestUtil.findAll(em, Quest.class).get(0);
        }
        booking.setQuest(quest);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createUpdatedEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        booking.setPlayer(player);
        return booking;
    }

    @BeforeEach
    public void initTest() {
        booking = createEntity(em);
    }

    @Test
    @Transactional
    public void createBooking() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();
        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);
        restBookingMockMvc.perform(post("/admin-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate + 1);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBooking.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testBooking.getDiscountInPercents()).isEqualTo(DEFAULT_DISCOUNT_IN_PERCENTS);
        assertThat(testBooking.getCommissionInPercents()).isEqualTo(DEFAULT_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void createBookingWithExistingId() throws Exception {
        final Booking booking = bookingRepository.save(createEntity(em));
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // Create the Booking with an existing ID
        this.booking.setId(booking.getId());
        BookingDTO bookingDTO = bookingMapper.toDto(this.booking);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingMockMvc.perform(post("/admin-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setStatus(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);


        restBookingMockMvc.perform(post("/admin-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setPrice(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);


        restBookingMockMvc.perform(post("/admin-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiscountInPercentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setDiscountInPercents(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);


        restBookingMockMvc.perform(post("/admin-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCommissionInPercentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setCommissionInPercents(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);


        restBookingMockMvc.perform(post("/admin-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookings() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList
        restBookingMockMvc.perform(get("/admin-api/bookings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].discountInPercents").value(hasItem(DEFAULT_DISCOUNT_IN_PERCENTS)))
            .andExpect(jsonPath("$.[*].commissionInPercents").value(hasItem(DEFAULT_COMMISSION_IN_PERCENTS)));
    }

    @Test
    @Transactional
    public void getBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc.perform(get("/admin-api/bookings/{id}", booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(booking.getId().toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.discountInPercents").value(DEFAULT_DISCOUNT_IN_PERCENTS))
            .andExpect(jsonPath("$.commissionInPercents").value(DEFAULT_COMMISSION_IN_PERCENTS));
    }


    @Test
    @Transactional
    public void getBookingsByIdFiltering() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        final UUID id = booking.getId();

        defaultBookingShouldBeFound("id.equals=" + id);
        defaultBookingShouldNotBeFound("id.notEquals=" + id);
    }


    @Test
    @Transactional
    public void getAllBookingsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status equals to DEFAULT_STATUS
        defaultBookingShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the bookingList where status equals to UPDATED_STATUS
        defaultBookingShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllBookingsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status not equals to DEFAULT_STATUS
        defaultBookingShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the bookingList where status not equals to UPDATED_STATUS
        defaultBookingShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllBookingsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBookingShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the bookingList where status equals to UPDATED_STATUS
        defaultBookingShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllBookingsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status is not null
        defaultBookingShouldBeFound("status.specified=true");

        // Get all the bookingList where status is null
        defaultBookingShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where price equals to DEFAULT_PRICE
        defaultBookingShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the bookingList where price equals to UPDATED_PRICE
        defaultBookingShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllBookingsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where price not equals to DEFAULT_PRICE
        defaultBookingShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the bookingList where price not equals to UPDATED_PRICE
        defaultBookingShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllBookingsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultBookingShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the bookingList where price equals to UPDATED_PRICE
        defaultBookingShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllBookingsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where price is not null
        defaultBookingShouldBeFound("price.specified=true");

        // Get all the bookingList where price is null
        defaultBookingShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where price is greater than or equal to DEFAULT_PRICE
        defaultBookingShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the bookingList where price is greater than or equal to UPDATED_PRICE
        defaultBookingShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllBookingsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where price is less than or equal to DEFAULT_PRICE
        defaultBookingShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the bookingList where price is less than or equal to SMALLER_PRICE
        defaultBookingShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllBookingsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where price is less than DEFAULT_PRICE
        defaultBookingShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the bookingList where price is less than UPDATED_PRICE
        defaultBookingShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllBookingsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where price is greater than DEFAULT_PRICE
        defaultBookingShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the bookingList where price is greater than SMALLER_PRICE
        defaultBookingShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllBookingsByDiscountInPercentsIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountInPercents equals to DEFAULT_DISCOUNT_IN_PERCENTS
        defaultBookingShouldBeFound("discountInPercents.equals=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the bookingList where discountInPercents equals to UPDATED_DISCOUNT_IN_PERCENTS
        defaultBookingShouldNotBeFound("discountInPercents.equals=" + UPDATED_DISCOUNT_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllBookingsByDiscountInPercentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountInPercents not equals to DEFAULT_DISCOUNT_IN_PERCENTS
        defaultBookingShouldNotBeFound("discountInPercents.notEquals=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the bookingList where discountInPercents not equals to UPDATED_DISCOUNT_IN_PERCENTS
        defaultBookingShouldBeFound("discountInPercents.notEquals=" + UPDATED_DISCOUNT_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllBookingsByDiscountInPercentsIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountInPercents in DEFAULT_DISCOUNT_IN_PERCENTS or UPDATED_DISCOUNT_IN_PERCENTS
        defaultBookingShouldBeFound("discountInPercents.in=" + DEFAULT_DISCOUNT_IN_PERCENTS + "," + UPDATED_DISCOUNT_IN_PERCENTS);

        // Get all the bookingList where discountInPercents equals to UPDATED_DISCOUNT_IN_PERCENTS
        defaultBookingShouldNotBeFound("discountInPercents.in=" + UPDATED_DISCOUNT_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllBookingsByDiscountInPercentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountInPercents is not null
        defaultBookingShouldBeFound("discountInPercents.specified=true");

        // Get all the bookingList where discountInPercents is null
        defaultBookingShouldNotBeFound("discountInPercents.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByDiscountInPercentsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountInPercents is greater than or equal to DEFAULT_DISCOUNT_IN_PERCENTS
        defaultBookingShouldBeFound("discountInPercents.greaterThanOrEqual=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the bookingList where discountInPercents is greater than or equal to (DEFAULT_DISCOUNT_IN_PERCENTS + 1)
        defaultBookingShouldNotBeFound("discountInPercents.greaterThanOrEqual=" + (DEFAULT_DISCOUNT_IN_PERCENTS + 1));
    }

    @Test
    @Transactional
    public void getAllBookingsByDiscountInPercentsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountInPercents is less than or equal to DEFAULT_DISCOUNT_IN_PERCENTS
        defaultBookingShouldBeFound("discountInPercents.lessThanOrEqual=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the bookingList where discountInPercents is less than or equal to SMALLER_DISCOUNT_IN_PERCENTS
        defaultBookingShouldNotBeFound("discountInPercents.lessThanOrEqual=" + SMALLER_DISCOUNT_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllBookingsByDiscountInPercentsIsLessThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountInPercents is less than DEFAULT_DISCOUNT_IN_PERCENTS
        defaultBookingShouldNotBeFound("discountInPercents.lessThan=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the bookingList where discountInPercents is less than (DEFAULT_DISCOUNT_IN_PERCENTS + 1)
        defaultBookingShouldBeFound("discountInPercents.lessThan=" + (DEFAULT_DISCOUNT_IN_PERCENTS + 1));
    }

    @Test
    @Transactional
    public void getAllBookingsByDiscountInPercentsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountInPercents is greater than DEFAULT_DISCOUNT_IN_PERCENTS
        defaultBookingShouldNotBeFound("discountInPercents.greaterThan=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the bookingList where discountInPercents is greater than SMALLER_DISCOUNT_IN_PERCENTS
        defaultBookingShouldBeFound("discountInPercents.greaterThan=" + SMALLER_DISCOUNT_IN_PERCENTS);
    }


    @Test
    @Transactional
    public void getAllBookingsByCommissionInPercentsIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where commissionInPercents equals to DEFAULT_COMMISSION_IN_PERCENTS
        defaultBookingShouldBeFound("commissionInPercents.equals=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the bookingList where commissionInPercents equals to UPDATED_COMMISSION_IN_PERCENTS
        defaultBookingShouldNotBeFound("commissionInPercents.equals=" + UPDATED_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllBookingsByCommissionInPercentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where commissionInPercents not equals to DEFAULT_COMMISSION_IN_PERCENTS
        defaultBookingShouldNotBeFound("commissionInPercents.notEquals=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the bookingList where commissionInPercents not equals to UPDATED_COMMISSION_IN_PERCENTS
        defaultBookingShouldBeFound("commissionInPercents.notEquals=" + UPDATED_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllBookingsByCommissionInPercentsIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where commissionInPercents in DEFAULT_COMMISSION_IN_PERCENTS or UPDATED_COMMISSION_IN_PERCENTS
        defaultBookingShouldBeFound("commissionInPercents.in=" + DEFAULT_COMMISSION_IN_PERCENTS + "," + UPDATED_COMMISSION_IN_PERCENTS);

        // Get all the bookingList where commissionInPercents equals to UPDATED_COMMISSION_IN_PERCENTS
        defaultBookingShouldNotBeFound("commissionInPercents.in=" + UPDATED_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllBookingsByCommissionInPercentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where commissionInPercents is not null
        defaultBookingShouldBeFound("commissionInPercents.specified=true");

        // Get all the bookingList where commissionInPercents is null
        defaultBookingShouldNotBeFound("commissionInPercents.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByCommissionInPercentsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where commissionInPercents is greater than or equal to DEFAULT_COMMISSION_IN_PERCENTS
        defaultBookingShouldBeFound("commissionInPercents.greaterThanOrEqual=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the bookingList where commissionInPercents is greater than or equal to (DEFAULT_COMMISSION_IN_PERCENTS + 1)
        defaultBookingShouldNotBeFound("commissionInPercents.greaterThanOrEqual=" + (DEFAULT_COMMISSION_IN_PERCENTS + 1));
    }

    @Test
    @Transactional
    public void getAllBookingsByCommissionInPercentsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where commissionInPercents is less than or equal to DEFAULT_COMMISSION_IN_PERCENTS
        defaultBookingShouldBeFound("commissionInPercents.lessThanOrEqual=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the bookingList where commissionInPercents is less than or equal to SMALLER_COMMISSION_IN_PERCENTS
        defaultBookingShouldNotBeFound("commissionInPercents.lessThanOrEqual=" + SMALLER_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllBookingsByCommissionInPercentsIsLessThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where commissionInPercents is less than DEFAULT_COMMISSION_IN_PERCENTS
        defaultBookingShouldNotBeFound("commissionInPercents.lessThan=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the bookingList where commissionInPercents is less than (DEFAULT_COMMISSION_IN_PERCENTS + 1)
        defaultBookingShouldBeFound("commissionInPercents.lessThan=" + (DEFAULT_COMMISSION_IN_PERCENTS + 1));
    }

    @Test
    @Transactional
    public void getAllBookingsByCommissionInPercentsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where commissionInPercents is greater than DEFAULT_COMMISSION_IN_PERCENTS
        defaultBookingShouldNotBeFound("commissionInPercents.greaterThan=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the bookingList where commissionInPercents is greater than SMALLER_COMMISSION_IN_PERCENTS
        defaultBookingShouldBeFound("commissionInPercents.greaterThan=" + SMALLER_COMMISSION_IN_PERCENTS);
    }


    @Test
    @Transactional
    public void getAllBookingsBySlotIsEqualToSomething() throws Exception {
        // Get already existing entity
        Slot slot = booking.getSlot();
        bookingRepository.saveAndFlush(booking);
        UUID slotId = slot.getId();

        // Get all the bookingList where slot equals to slotId
        defaultBookingShouldBeFound("slotId.equals=" + slotId);

        // Get all the bookingList where slot equals to slotId + 1
        defaultBookingShouldNotBeFound("slotId.equals=" + UUID.fromString("babf4075-3405-4923-9a2e-ff710f27ed0e"));
    }


    @Test
    @Transactional
    public void getAllBookingsByQuestIsEqualToSomething() throws Exception {
        // Get already existing entity
        Quest quest = booking.getQuest();
        bookingRepository.saveAndFlush(booking);
        UUID questId = quest.getId();

        // Get all the bookingList where quest equals to questId
        defaultBookingShouldBeFound("questId.equals=" + questId);

        // Get all the bookingList where quest equals to questId + 1
        defaultBookingShouldNotBeFound("questId.equals=" + UUID.fromString("23c705f9-4a95-49aa-8b14-3f9e67e6f8ae"));
    }


    @Test
    @Transactional
    public void getAllBookingsByPlayerIsEqualToSomething() throws Exception {
        // Get already existing entity
        Player player = booking.getPlayer();
        bookingRepository.saveAndFlush(booking);
        UUID playerId = player.getId();

        // Get all the bookingList where player equals to playerId
        defaultBookingShouldBeFound("playerId.equals=" + playerId);

        // Get all the bookingList where player equals to playerId + 1
        defaultBookingShouldNotBeFound("playerId.equals=" + UUID.fromString("d79631be-7fea-4ec5-bbad-11ac95aae222"));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookingShouldBeFound(String filter) throws Exception {
        restBookingMockMvc.perform(get("/admin-api/bookings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].discountInPercents").value(hasItem(DEFAULT_DISCOUNT_IN_PERCENTS)))
            .andExpect(jsonPath("$.[*].commissionInPercents").value(hasItem(DEFAULT_COMMISSION_IN_PERCENTS)));

        // Check, that the count call also returns 1
        restBookingMockMvc.perform(get("/admin-api/bookings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookingShouldNotBeFound(String filter) throws Exception {
        restBookingMockMvc.perform(get("/admin-api/bookings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookingMockMvc.perform(get("/admin-api/bookings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get("/admin-api/bookings/{id}", UUID.fromString("91e45cc5-d556-45cd-91e8-b4ab528c637f")))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        // Disconnect from session so that the updates on updatedBooking are not directly saved in db
        em.detach(updatedBooking);
        updatedBooking
            .setStatus(UPDATED_STATUS)
            .setPrice(UPDATED_PRICE)
            .setDiscountInPercents(UPDATED_DISCOUNT_IN_PERCENTS)
            .setCommissionInPercents(UPDATED_COMMISSION_IN_PERCENTS);
        BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

        restBookingMockMvc.perform(put("/admin-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBooking.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testBooking.getDiscountInPercents()).isEqualTo(UPDATED_DISCOUNT_IN_PERCENTS);
        assertThat(testBooking.getCommissionInPercents()).isEqualTo(UPDATED_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMockMvc.perform(put("/admin-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeDelete = bookingRepository.findAll().size();

        // Delete the booking
        restBookingMockMvc.perform(delete("/admin-api/bookings/{id}", booking.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Booking> bookingList = bookingRepository.findAll();

        // Due to commented delete service
        // assertThat(bookingList).hasSize(databaseSizeBeforeDelete - 1);
        assertThat(bookingList).hasSize(databaseSizeBeforeDelete);
    }
}
