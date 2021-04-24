package com.greatescape.api.monolith.scheduled;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.repository.QuestRepository;
import com.greatescape.api.monolith.utils.wiremock.JsonResponse;
import java.io.IOException;
import java.net.URI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApiMonolithApp.class)
@DBRider
@ActiveProfiles("autocommit")
public class SyncSlotsIT {

    private static final WireMockServer WIREMOCK = wiremockServer();

    private static final String SCHEDULE_URL = "/schedule";

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
    public void mirKvestovOneSlot() throws IOException {
        this.setUpMirKvestov("wiremock/scheduled/sync-slots/mir-kvestov/oneSlot.json");

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
    public void mirKvestovTwoSlots() throws IOException {
        this.setUpMirKvestov("wiremock/scheduled/sync-slots/mir-kvestov/twoSlots.json");

        this.syncSlots.run();
    }

    @Test
    @DataSet(
        value = "db-rider/common/quest.yml", cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    @ExpectedDataSet("db-rider/scheduled/sync-slots/expected/oneSlot.yml")
    public void mirKvestovOneSlotWithDoublePrice() throws IOException {
        this.setUpMirKvestov("wiremock/scheduled/sync-slots/mir-kvestov/oneSlotWithDoublePrice.json");

        this.syncSlots.run();
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml", "db-rider/scheduled/sync-slots/initial/oneSlot.yml"},
        cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    @ExpectedDataSet("db-rider/scheduled/sync-slots/expected/oneSlot.yml")
    public void mirKvestovMutateAvailabilityAndPrice() throws IOException {
        this.setUpMirKvestov("wiremock/scheduled/sync-slots/mir-kvestov/oneSlot.json");

        this.syncSlots.run();
    }

    private void setUpMirKvestov(final String responsePath) throws IOException {
        this.questIntegrationSettingRepository.saveAndFlush(
            new QuestIntegrationSetting()
                .setQuest(this.questRepository.findAll().get(0))
                .setType(QuestIntegrationType.MIR_KVESTOV)
                .setSettings(
                    new QuestIntegrationSetting.MirKvestov()
                        .setMd5key("")
                        .setScheduleUrl(URI.create(WIREMOCK.baseUrl() + SCHEDULE_URL))
                        .setBookingUrl(URI.create(""))
                )
        );
        WIREMOCK.stubFor(
            WireMock.get(SCHEDULE_URL)
                .willReturn(new JsonResponse(responsePath))
        );
    }

    private static WireMockServer wiremockServer() {
        final var server = new WireMockServer(
            WireMockConfiguration.options()
                .dynamicPort()
                .extensions(new ResponseTemplateTransformer(false))
        );
        server.start();

        return server;
    }
}
