package com.greatescape.api.monolith.scheduled.syncslots;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.repository.QuestRepository;
import com.greatescape.api.monolith.scheduled.SyncSlots;
import com.greatescape.api.monolith.utils.wiremock.JsonResponse;
import static com.greatescape.api.monolith.utils.wiremock.WireMock.initWireMockServer;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = ApiMonolithApp.class)
@DBRider
@ActiveProfiles("autocommit")
@ContextConfiguration(initializers = BookFormIT.Initializer.class)
public class BookFormIT {

    private static final WireMockServer WIREMOCK = initWireMockServer();

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private QuestIntegrationSettingRepository questIntegrationSettingRepository;

    @Autowired
    private SyncSlots syncSlots;

    @AfterEach
    public void after() {
        WIREMOCK.resetAll();
    }

    @Test
    @DataSet(
        value = "db-rider/common/quest.yml", cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    @ExpectedDataSet("db-rider/scheduled/sync-slots/expected/oneSlot.yml")
    public void oneSlot() throws IOException {
        this.setUp("wiremock/scheduled/sync-slots/book-form/oneSlot.json");

        this.syncSlots.run();
    }

    @Test
    @DataSet(
        value = "db-rider/common/quest.yml", cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    @ExpectedDataSet(
        value = "db-rider/scheduled/sync-slots/expected/twoSlots.yml", orderBy = "date_time_local"
    )
    public void twoSlots() throws IOException {
        this.setUp("wiremock/scheduled/sync-slots/book-form/twoSlots.json");

        this.syncSlots.run();
    }

    @Test
    @DataSet(
        value = "db-rider/common/quest.yml", cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    @ExpectedDataSet("db-rider/scheduled/sync-slots/expected/oneSlot.yml")
    public void oneSlotWithDoublePrice() throws IOException {
        this.setUp("wiremock/scheduled/sync-slots/book-form/oneSlotWithDoublePrice.json");

        this.syncSlots.run();
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml", "db-rider/scheduled/sync-slots/initial/oneSlot.yml"},
        cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    @ExpectedDataSet("db-rider/scheduled/sync-slots/expected/oneSlot.yml")
    public void mutateAvailabilityAndPrice() throws IOException {
        this.setUp("wiremock/scheduled/sync-slots/book-form/oneSlot.json");

        this.syncSlots.run();
    }

    private void setUp(final String responsePath) throws IOException {
        this.questIntegrationSettingRepository.saveAndFlush(
            new QuestIntegrationSetting()
                .setQuest(this.questRepository.findAll().get(0))
                .setType(QuestIntegrationType.BOOK_FORM)
                .setSettings(
                    new QuestIntegrationSetting.BookForm()
                        .setServiceId("076967640D6F11E6818E001517CAC1A9")
                        .setWidgetId("076967640D6F11E6818E001517CAC1A9")
                )
        );
        WIREMOCK.stubFor(
            WireMock.get(
                WireMock.urlPathEqualTo("/api/v1/schedule")
            ).willReturn(new JsonResponse(responsePath))
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
