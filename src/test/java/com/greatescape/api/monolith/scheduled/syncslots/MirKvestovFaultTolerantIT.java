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
import java.net.URI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApiMonolithApp.class)
@DBRider
@ActiveProfiles("autocommit")
public class MirKvestovFaultTolerantIT {

    private static final WireMockServer WIREMOCK = initWireMockServer();

    private static final String SCHEDULE_FAILED_URL = "/schedule-failed";
    private static final String SCHEDULE_NORMAL_URL = "/schedule-normal";

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
        value = {"db-rider/common/quest.yml", "db-rider/scheduled/sync-slots/initial/anotherQuest.yml"},
        cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    @ExpectedDataSet("db-rider/scheduled/sync-slots/expected/oneSlot.yml")
    public void faultTolerant() throws IOException {
        this.setUpFailed();
        this.setUpNormal("wiremock/scheduled/sync-slots/mir-kvestov/oneSlot.json");

        this.syncSlots.run();
    }

    @Test
    @DataSet(
        value = {"db-rider/common/quest.yml"},
        cleanBefore = true, cleanAfter = true,
        skipCleaningFor = {"databasechangelog", "databasechangeloglock", "jhi_authority"}
    )
    @ExpectedDataSet("db-rider/scheduled/sync-slots/expected/oneSlot.yml")
    public void twoDuplicatedSlots() throws IOException {
        this.setUpNormal("wiremock/scheduled/sync-slots/mir-kvestov/twoDuplicatedSlots.json");

        this.syncSlots.run();
    }

    private void setUpNormal(final String responsePath) throws IOException {
        this.questIntegrationSettingRepository.saveAndFlush(
            new QuestIntegrationSetting()
                .setQuest(this.questRepository.findAll().get(0))
                .setType(QuestIntegrationType.MIR_KVESTOV)
                .setSettings(
                    new QuestIntegrationSetting.MirKvestov()
                        .setMd5key("")
                        .setScheduleUrl(URI.create(WIREMOCK.baseUrl() + SCHEDULE_NORMAL_URL))
                        .setBookingUrl(URI.create(""))
                )
        );
        WIREMOCK.stubFor(
            WireMock.get(SCHEDULE_NORMAL_URL)
                .willReturn(new JsonResponse(responsePath))
        );
    }

    private void setUpFailed() {
        this.questIntegrationSettingRepository.saveAndFlush(
            new QuestIntegrationSetting()
                .setQuest(this.questRepository.findAll().get(1))
                .setType(QuestIntegrationType.MIR_KVESTOV)
                .setSettings(
                    new QuestIntegrationSetting.MirKvestov()
                        .setMd5key("")
                        .setScheduleUrl(URI.create(WIREMOCK.baseUrl() + SCHEDULE_FAILED_URL))
                        .setBookingUrl(URI.create(""))
                )
        );
        WIREMOCK.stubFor(
            WireMock.get(SCHEDULE_FAILED_URL)
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(500)
                        .withBody("Some error occurred")
                )
        );
    }
}
