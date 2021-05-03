package com.greatescape.api.monolith.service.impl.bookingplayerservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.enumeration.BookingStatus;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.integration.BookFormClient;
import com.greatescape.api.monolith.repository.BookingRepository;
import com.greatescape.api.monolith.repository.PlayerRepository;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.repository.QuestRepository;
import com.greatescape.api.monolith.repository.SlotRepository;
import com.greatescape.api.monolith.service.PlayerPlayerService;
import static com.greatescape.api.monolith.utils.wiremock.WireMock.initWireMockServer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ApiMonolithApp.class)
@DBRider
@ActiveProfiles("autocommit")
@ContextConfiguration(initializers = IntegrationSenderIT.Initializer.class)

// @TODO: После того, как напишу тесты на IntegrationSender: попробовать убрать FeignForm

public class IntegrationSenderIT {

    private static final WireMockServer WIREMOCK = initWireMockServer();

    private static final String BOOKING_URL = "/api/v1/bookings";

    private static final String BOOK_FORM_WIDGET_ID = "someWidgetId";
    private static final String BOOK_FORM_SERVICE_ID = "someServiceId";

    private static final int PRICE = 1234;
    private static final String NAME = "Some Player Name";
    private static final String PHONE = "79991231212";
    private static final String EMAIL = "example@gmail.com";
    private static final String COMMENT = "Some extra comment from player";
    private static final String SLOT_EXTERNAL_ID = "some slot external id";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Quest quest;
    private Booking booking;

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private QuestIntegrationSettingRepository questIntegrationSettingRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PlayerPlayerService playerService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private IntegrationSender integrationSender;

    @BeforeEach
    public void before() {
        this.quest = this.questRepository.findAll().get(0);
        final var dateTime = Instant.parse("2007-12-03T10:15:30.00Z");
        final var slot = this.slotRepository.saveAndFlush(
            new Slot()
                .setDateTimeLocal(dateTime)
                .setDateTimeWithTimeZone(
                    ZonedDateTime.ofInstant(dateTime, ZoneId.of("UTC"))
                )
                .setIsAvailable(true)
                .setPrice(PRICE)
                .setQuest(this.quest)
                .setExternalId(SLOT_EXTERNAL_ID)
        );
        this.booking = this.bookingRepository.saveAndFlush(
            new Booking()
                .setStatus(BookingStatus.NEW)
                .setPrice(PRICE)
                .setDiscountInPercents(12)
                .setCommissionInPercents(34)
                .setComment(COMMENT)
                .setSlot(slot)
                .setQuest(this.quest)
                .setPlayer(
                    this.playerRepository.getOne(playerService.upsert(
                        new PlayerPlayerService.CreateRequest(NAME, PHONE, EMAIL)
                    ).getPlayerId())
                )
        );
    }

    @AfterEach
    public void after() {
        WIREMOCK.resetAll();
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml", "db-rider/scheduled/sync-slots/initial/anotherQuest.yml"},
        cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    @Transactional
    void bookFormHappyPath() throws JsonProcessingException {
        this.questIntegrationSettingRepository.saveAndFlush(
            new QuestIntegrationSetting()
                .setQuest(this.quest)
                .setType(QuestIntegrationType.BOOK_FORM)
                .setSettings(
                    new QuestIntegrationSetting.BookForm()
                        .setWidgetId(BOOK_FORM_WIDGET_ID)
                        .setServiceId(BOOK_FORM_SERVICE_ID)
                )
        );
        WIREMOCK.stubFor(
            WireMock.post(BOOKING_URL)
                .willReturn(
                    WireMock.okJson(
                        objectMapper.writeValueAsString(
                            new BookFormClient.BookingResponse()
                                .setId("someId")
                                .setPrice(PRICE)
                                .setPaid_price(0)
                                .setName(NAME)
                                .setEmail(EMAIL)
                        )
                    ).withStatus(201)
                )
        );

        this.integrationSender.send(this.booking);

        WIREMOCK.verify(1, WireMock.postRequestedFor(WireMock.urlPathEqualTo(BOOKING_URL)));
        final var bookFormRequest = WIREMOCK.findAll(
            WireMock.postRequestedFor(WireMock.urlPathEqualTo(BOOKING_URL))
        ).get(0);

        this.assertEquals(bookFormRequest, "name", NAME);
        this.assertEquals(bookFormRequest, "email", EMAIL);
        this.assertEquals(bookFormRequest, "phone", PHONE);
        this.assertEquals(bookFormRequest, "comment", COMMENT);
        this.assertEquals(bookFormRequest, "service_id", BOOK_FORM_SERVICE_ID);
        this.assertEquals(bookFormRequest, "source_id", BOOK_FORM_WIDGET_ID);
        this.assertEquals(bookFormRequest, "slots_id", SLOT_EXTERNAL_ID);
    }

    private void assertEquals(
        final LoggedRequest request,
        final String partName,
        final String expectedValue
    ) {
        Assertions.assertEquals(
            expectedValue,
            request.getPart(partName).getBody().asString()
        );
    }

    static class Initializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public void initialize(final ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                "app.integration.book-form.base-url=" + WIREMOCK.baseUrl()
            ).applyTo(context.getEnvironment());
        }
    }
}

