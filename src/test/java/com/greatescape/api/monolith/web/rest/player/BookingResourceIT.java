package com.greatescape.api.monolith.web.rest.player;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.Otp;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.repository.BookingRepository;
import com.greatescape.api.monolith.repository.OtpRepository;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.repository.QuestRepository;
import com.greatescape.api.monolith.repository.SlotRepository;
import com.greatescape.api.monolith.scheduled.RefreshSlotAggregate;
import static com.greatescape.api.monolith.utils.wiremock.WireMock.initWireMockServer;
import com.greatescape.api.monolith.web.rest.TestUtil;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link BookingResource} REST controller.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
@AutoConfigureMockMvc
@DBRider
@ActiveProfiles("autocommit")
@ContextConfiguration(initializers = BookingResourceIT.Initializer.class)
public class BookingResourceIT {

    private static final WireMockServer WIREMOCK = initWireMockServer();
    private static final String BOOKING_URL = "/booking";
    private static final String RE_CAPTCHA_URL = "/recaptcha";

    private static final String NAME = "Some Player Name";
    private static final String PHONE = "79991231212";
    private static final String RE_CAPTCHA_TOKEN = "some_token";
    private static final String EMAIL_UNNORMALIZED = "eXaMple@Gmail.COM";
    private static final String EMAIL_NORMALIZED = "example@gmail.com";
    private static final String COMMENT = "Some extra comment from player";

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private QuestIntegrationSettingRepository questIntegrationSettingRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RefreshSlotAggregate refreshSlotAggregate;

    @AfterEach
    public void after() {
        WIREMOCK.resetAll();
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml"}, cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    public void happyPath() throws Exception {
        final var quest = questRepository.findAll().get(0);
        this.setUpIntegrationSetting(quest);
        final var slot = this.slot(quest);
        this.setUpReCaptchaMock();
        WIREMOCK.stubFor(
            WireMock.post(BOOKING_URL)
                .willReturn(
                    WireMock.okJson("{\"success\": true}").withStatus(201)
                )
        );

        mockMvc.perform(post("/player-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(this.bookingRequest(slot.getId()))))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.bookingId").value(not(empty())))
            .andExpect(jsonPath("$.playerId").value(not(empty())))
            .andExpect(jsonPath("$.questId").value(quest.getId().toString()))
            .andExpect(jsonPath("$.slotId").value(slot.getId().toString()));

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(1);
        final var booking = bookingList.get(0);
        assertThat(booking.getSlot().getId()).isEqualTo(slot.getId());
        assertThat(booking.getPlayer().getName()).isEqualTo(NAME);
        assertThat(booking.getPlayer().getPhone()).isEqualTo(PHONE);
        assertThat(booking.getPlayer().getEmail()).isEqualTo(EMAIL_NORMALIZED);
        assertThat(booking.getComment()).isEqualTo(COMMENT);
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml"}, cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    public void happyPathDryRun() throws Exception {
        final var quest = questRepository.findAll().get(0);
        this.setUpIntegrationSetting(quest);
        final var slot = this.slot(quest);
        this.setUpReCaptchaMock();

        mockMvc.perform(post("/player-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(
                this.bookingRequest(slot.getId(), true)
            )))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.bookingId").value(nullValue()))
            .andExpect(jsonPath("$.playerId").value(nullValue()))
            .andExpect(jsonPath("$.questId").value(nullValue()))
            .andExpect(jsonPath("$.slotId").value(nullValue()));

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(0);
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml"}, cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    public void slotAlreadyBooked() throws Exception {
        final var quest = questRepository.findAll().get(0);
        this.setUpIntegrationSetting(quest);
        final var slot = this.slot(quest);
        this.setUpReCaptchaMock();
        WIREMOCK.stubFor(
            WireMock.post(BOOKING_URL)
                .willReturn(
                    WireMock.okJson("{\"success\": true}").withStatus(201)
                )
        );

        mockMvc.perform(post("/player-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(this.bookingRequest(slot.getId()))))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/player-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(this.bookingRequest(slot.getId()))))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
            .andExpect(jsonPath("$.entityName").value("booking"))
            .andExpect(jsonPath("$.errorKey").value("slotAlreadyBooked"));
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml"}, cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    public void unsuccessfulRemoteResponse() throws Exception {
        final var quest = questRepository.findAll().get(0);
        this.setUpIntegrationSetting(quest);
        final var slot = this.slot(quest);
        this.setUpReCaptchaMock();
        WIREMOCK.stubFor(
            WireMock.post(BOOKING_URL)
                .willReturn(
                    WireMock.okJson("{\"success\": false}").withStatus(201)
                )
        );

        mockMvc.perform(post("/player-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(this.bookingRequest(slot.getId()))))
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
            .andExpect(jsonPath("$.title").value("Internal Server Error"))
            .andExpect(jsonPath("$.detail").value("Got unsuccessful response from MirKvestov"))
            .andExpect(jsonPath("$.message").value("error.http.500"));
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml"}, cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    public void remoteException() throws Exception {
        final var quest = questRepository.findAll().get(0);
        this.setUpIntegrationSetting(quest);
        final var slot = this.slot(quest);
        this.setUpReCaptchaMock();
        WIREMOCK.stubFor(
            WireMock.post(BOOKING_URL)
                .willReturn(WireMock.serverError())
        );

        mockMvc.perform(post("/player-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(this.bookingRequest(slot.getId()))))
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
            .andExpect(jsonPath("$.title").value("Internal Server Error"))
            .andExpect(jsonPath("$.detail").value(
                startsWith("[500 Server Error] during [POST] to ")
            ))
            .andExpect(jsonPath("$.message").value("error.http.500"));
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml"}, cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    public void slotNotFound() throws Exception {
        final var quest = questRepository.findAll().get(0);
        this.setUpIntegrationSetting(quest);
        final var slot = this.slot(quest);
        this.setUpReCaptchaMock();

        mockMvc.perform(post("/player-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(
                this.bookingRequest(
                    this.differentUuid(slot.getId())
                )
            )))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
            .andExpect(jsonPath("$.entityName").value("booking"))
            .andExpect(jsonPath("$.errorKey").value("slotNotFound"));
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml"}, cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    public void slotUnavailableForBooking() throws Exception {
        final var quest = questRepository.findAll().get(0);
        this.setUpIntegrationSetting(quest);
        final var slot = this.slot(quest, false);
        this.setUpReCaptchaMock();

        mockMvc.perform(post("/player-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(this.bookingRequest(slot.getId()))))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
            .andExpect(jsonPath("$.entityName").value("booking"))
            .andExpect(jsonPath("$.errorKey").value("slotUnavailableForBooking"));
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml"}, cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    public void slotTimeAlreadyPassed() throws Exception {
        final var quest = questRepository.findAll().get(0);
        this.setUpIntegrationSetting(quest);
        final var slot = this.slot(quest, true, Instant.now().minus(Duration.ofMinutes(15)));
        this.setUpReCaptchaMock();

        mockMvc.perform(post("/player-api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(this.bookingRequest(slot.getId()))))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
            .andExpect(jsonPath("$.entityName").value("booking"))
            .andExpect(jsonPath("$.errorKey").value("slotTimeAlreadyPassed"));
    }

    private void setUpIntegrationSetting(Quest quest) {
        this.questIntegrationSettingRepository.saveAndFlush(
            new QuestIntegrationSetting()
                .setQuest(quest)
                .setType(QuestIntegrationType.MIR_KVESTOV)
                .setSettings(
                    new QuestIntegrationSetting.MirKvestov()
                        .setMd5key("")
                        .setScheduleUrl(URI.create(""))
                        .setBookingUrl(URI.create(WIREMOCK.baseUrl() + BOOKING_URL))
                )
        );
    }

    private void setUpReCaptchaMock() {
        WIREMOCK.stubFor(
            WireMock.get(
                WireMock.urlPathEqualTo(RE_CAPTCHA_URL + "/recaptcha/api/siteverify")
            )
                .willReturn(
                    WireMock.okJson("{\"success\": true, \"score\": 0.9}")
                )
        );
    }

    private Slot slot(final Quest quest) {
        return this.slot(quest, true);
    }
    private Slot slot(final Quest quest, final boolean isAvailable) {
        return this.slot(quest, isAvailable, Instant.now().plus(Duration.ofHours(10)));
    }

    private Slot slot(
        final Quest quest,
        final boolean isAvailable,
        final Instant localTime
    ) {
        final var slot = slotRepository.save(
            new Slot()
                .setDateTimeLocal(localTime)
                .setDateTimeWithTimeZone(
                    ZonedDateTime.ofInstant(localTime, ZoneId.of("UTC"))
                )
                .setIsAvailable(isAvailable)
                .setPrice(1122)
                .setQuest(quest)
        );
        refreshSlotAggregate.run();

        return slot;
    }

    private BookingResource.CreateRequest bookingRequest(final UUID slotId) throws Exception {
        return this.bookingRequest(slotId, false);
    }

    private BookingResource.CreateRequest bookingRequest(
        final UUID slotId,
        final boolean dryRun
    ) throws Exception {
        mockMvc.perform(post("/player-api/otp")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(
                new OtpResource.Request()
                    .setReCaptchaToken(RE_CAPTCHA_TOKEN)
                    .setPhone(PHONE)
            )))
            .andExpect(status().isOk());

        return new BookingResource.CreateRequest()
            .setReCaptchaToken(RE_CAPTCHA_TOKEN)
            .setSlotId(slotId)
            .setName(NAME)
            .setPhone(PHONE)
            .setOtp(otpRepository
                .findAllByStatusAndPayload(Otp.Status.PENDING, PHONE)
                .get(0)
                .getCode()
            )
            .setEmail(EMAIL_UNNORMALIZED)
            .setComment(COMMENT)
            .setDryRun(dryRun);
    }

    private UUID differentUuid(UUID original) {
        UUID generated;
        do {
            generated = UUID.randomUUID();
        } while (original.equals(generated));

        return generated;
    }

    static class Initializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public void initialize(final ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                "google.recaptcha.base-url=" + WIREMOCK.baseUrl() + RE_CAPTCHA_URL
            ).applyTo(context.getEnvironment());
        }
    }
}
