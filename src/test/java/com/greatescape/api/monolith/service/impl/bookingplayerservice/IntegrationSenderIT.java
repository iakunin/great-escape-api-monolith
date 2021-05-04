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
import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.User;
import com.greatescape.api.monolith.domain.enumeration.BookingStatus;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.integration.BookFormClient;
import com.greatescape.api.monolith.repository.BookingRepository;
import com.greatescape.api.monolith.repository.PlayerRepository;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.repository.QuestRepository;
import com.greatescape.api.monolith.repository.SlotRepository;
import com.greatescape.api.monolith.repository.UserRepository;
import static com.greatescape.api.monolith.utils.wiremock.WireMock.initWireMockServer;
import java.io.Serializable;
import java.net.URI;
import java.net.URLDecoder;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = ApiMonolithApp.class)
@DBRider
@ActiveProfiles("autocommit")
@ContextConfiguration(initializers = IntegrationSenderIT.Initializer.class)
public class IntegrationSenderIT {

    private static final WireMockServer WIREMOCK = initWireMockServer();

    private static final String BOOK_FORM_BOOKING_URL = "/api/v1/bookings";
    private static final String MIR_KVESTOV_BOOKING_URL = "/bookings";

    private static final String BOOK_FORM_WIDGET_ID = "someWidgetId";
    private static final String BOOK_FORM_SERVICE_ID = "someServiceId";

    private static final int PRICE = 1234;
    private static final String NAME = "Some Player Name";
    private static final String PHONE = "79991231212";
    private static final String EMAIL = "example@gmail.com";
    private static final String COMMENT = "Some extra comment from player";
    private static final String SLOT_EXTERNAL_ID = "some slot external id";
    private static final String MIR_KVESTOV_MD_5_KEY = "someMd5Key";
    private static final Instant SLOT_DATE_TIME = Instant.parse("2007-12-03T10:15:30.00Z");
    private static final String PASSWORD_HASH = "$2a$10$e8sEgj9.yhTPEyRFxDg8VOQ3JjvoAMGYtgTN.mk2n.22G2L7DquIa";

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
    private PlayerRepository playerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IntegrationSender integrationSender;

    @AfterEach
    public void after() {
        WIREMOCK.resetAll();
    }

    @BeforeEach
    public void before() {
        this.quest = this.questRepository.findAll().get(0);
        this.booking = this.bookingRepository.saveAndFlush(
            new Booking()
                .setStatus(BookingStatus.NEW)
                .setPrice(PRICE)
                .setDiscountInPercents(12)
                .setCommissionInPercents(34)
                .setComment(COMMENT)
                .setSlot(this.slotRepository.saveAndFlush(
                    new Slot()
                        .setDateTimeLocal(SLOT_DATE_TIME)
                        .setDateTimeWithTimeZone(
                            ZonedDateTime.ofInstant(SLOT_DATE_TIME, ZoneId.of("UTC"))
                        )
                        .setIsAvailable(true)
                        .setPrice(PRICE)
                        .setQuest(this.quest)
                        .setExternalId(SLOT_EXTERNAL_ID)
                ))
                .setQuest(this.quest)
                .setPlayer(
                    this.playerRepository.saveAndFlush(
                        new Player()
                            .setName(NAME)
                            .setPhone(PHONE)
                            .setEmail(EMAIL)
                            .setInternalUser(
                                this.userRepository.saveAndFlush(
                                    new User()
                                        .setEmail(EMAIL)
                                        .setPassword(PASSWORD_HASH)
                                        .setLogin(EMAIL)
                                )
                            )
                    )
                )
        );
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml", "db-rider/scheduled/sync-slots/initial/anotherQuest.yml"},
        cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    public void bookFormHappyPath() throws JsonProcessingException {
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
            WireMock.post(BOOK_FORM_BOOKING_URL)
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

        WIREMOCK.verify(1, WireMock.postRequestedFor(WireMock.urlPathEqualTo(BOOK_FORM_BOOKING_URL)));
        final var bookFormRequest = WIREMOCK.findAll(
            WireMock.postRequestedFor(WireMock.urlPathEqualTo(BOOK_FORM_BOOKING_URL))
        ).get(0);
        Assertions.assertEquals(
            MediaType.MULTIPART_FORM_DATA_VALUE,
            bookFormRequest.contentTypeHeader().mimeTypePart()
        );
        Assertions.assertTrue(bookFormRequest.isMultipart());
        this.assertEquals(bookFormRequest, "name", NAME);
        this.assertEquals(bookFormRequest, "email", EMAIL);
        this.assertEquals(bookFormRequest, "phone", PHONE);
        this.assertEquals(bookFormRequest, "comment", COMMENT);
        this.assertEquals(bookFormRequest, "service_id", BOOK_FORM_SERVICE_ID);
        this.assertEquals(bookFormRequest, "source_id", BOOK_FORM_WIDGET_ID);
        this.assertEquals(bookFormRequest, "slots_id", SLOT_EXTERNAL_ID);
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml", "db-rider/scheduled/sync-slots/initial/anotherQuest.yml"},
        cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    public void mirKvestovHappyPath() {
        this.questIntegrationSettingRepository.saveAndFlush(
            new QuestIntegrationSetting()
                .setQuest(this.quest)
                .setType(QuestIntegrationType.MIR_KVESTOV)
                .setSettings(
                    new QuestIntegrationSetting.MirKvestov()
                        .setMd5key(MIR_KVESTOV_MD_5_KEY)
                        .setBookingUrl(URI.create(WIREMOCK.baseUrl() + MIR_KVESTOV_BOOKING_URL))
                        .setScheduleUrl(URI.create(""))
                )
        );
        WIREMOCK.stubFor(
            WireMock.post(MIR_KVESTOV_BOOKING_URL)
                .willReturn(
                    WireMock.okJson("{\"success\": true}").withStatus(201)
                )
        );

        this.integrationSender.send(this.booking);

        WIREMOCK.verify(1, WireMock.postRequestedFor(WireMock.urlPathEqualTo(MIR_KVESTOV_BOOKING_URL)));
        final var mirKvestovRequest = WIREMOCK.findAll(
            WireMock.postRequestedFor(WireMock.urlPathEqualTo(MIR_KVESTOV_BOOKING_URL))
        ).get(0);
        Assertions.assertEquals(
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            mirKvestovRequest.contentTypeHeader().mimeTypePart()
        );
        Assertions.assertEquals(
            this.expectedMirKvestovRequestBody(),
            this.urlEncodedBodyAsMap(mirKvestovRequest.getBodyAsString())
        );
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

    private Map<String, String> urlEncodedBodyAsMap(String body) {
        return Arrays.stream(
            body.split("&")
        ).map(param ->
            Map.entry(
                param.split("=")[0],
                URLDecoder.decode(param.split("=")[1])
            )
        ).collect(
            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
        );
    }

    private Map<String, ? extends Serializable> expectedMirKvestovRequestBody() {
        return Map.ofEntries(
            Map.entry("first_name", NAME),
            Map.entry("family_name", "."),
            Map.entry("phone", PHONE),
            Map.entry("email", EMAIL),
            Map.entry("comment", COMMENT),
            Map.entry("source", "great-escape.ru"),
            Map.entry(
                "date",
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withZone(ZoneId.of("Z"))
                    .format(SLOT_DATE_TIME)
            ),
            Map.entry(
                "time",
                DateTimeFormatter.ofPattern("HH:mm")
                    .withZone(ZoneId.of("Z"))
                    .format(SLOT_DATE_TIME)
            ),
            Map.entry("price", Integer.valueOf(PRICE).toString()),
            Map.entry("md5", "905336decfbb339a5dbba14c88d5bf06")
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

