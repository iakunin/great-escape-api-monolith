package com.greatescape.api.monolith.config.timezone;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.repository.timezone.DateTimeWrapper;
import com.greatescape.api.monolith.repository.timezone.DateTimeWrapperRepository;
import static java.lang.String.format;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the ZoneId Hibernate configuration.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
public class HibernateTimeZoneIT {

    @Autowired
    private DateTimeWrapperRepository dateTimeWrapperRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${spring.jpa.properties.hibernate.jdbc.time_zone:UTC}")
    private String zoneId;

    private DateTimeWrapper dateTimeWrapper;
    private DateTimeFormatter dateTimeFormatter;
    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter dateFormatter;

    @BeforeEach
    public void setup() {
        dateTimeWrapper = new DateTimeWrapper();
        dateTimeWrapper.setInstant(Instant.parse("2014-11-12T05:50:00.0Z"));
        dateTimeWrapper.setLocalDateTime(LocalDateTime.parse("2014-11-12T07:50:00.0"));
        dateTimeWrapper.setOffsetDateTime(OffsetDateTime.parse("2011-12-14T08:30:00.0Z"));
        dateTimeWrapper.setZonedDateTime(ZonedDateTime.parse("2011-12-14T08:30:00.0Z"));
        dateTimeWrapper.setLocalTime(LocalTime.parse("14:30:00"));
        dateTimeWrapper.setOffsetTime(OffsetTime.parse("14:30:00+02:00"));
        dateTimeWrapper.setLocalDate(LocalDate.parse("2016-09-10"));

        dateTimeFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss.S")
            .withZone(ZoneId.of(zoneId));

        timeFormatter = DateTimeFormatter
            .ofPattern("HH:mm:ss")
            .withZone(ZoneId.of(zoneId));

        dateFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd");
    }

    @Test
    @Transactional
    public void storeInstantWithZoneIdConfigShouldBeStoredOnGMTTimeZone() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("instant", dateTimeWrapper.getId());
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(request);
        String expectedValue = dateTimeFormatter.format(dateTimeWrapper.getInstant());

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    @Test
    @Transactional
    public void storeLocalDateTimeWithZoneIdConfigShouldBeStoredOnGMTTimeZone() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("local_date_time", dateTimeWrapper.getId());
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(request);
        String expectedValue = dateTimeWrapper
            .getLocalDateTime()
            .atZone(ZoneId.systemDefault())
            .format(dateTimeFormatter);

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    @Test
    @Transactional
    public void storeOffsetDateTimeWithZoneIdConfigShouldBeStoredOnGMTTimeZone() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("offset_date_time", dateTimeWrapper.getId());
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(request);
        String expectedValue = dateTimeWrapper
            .getOffsetDateTime()
            .format(dateTimeFormatter);

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    @Test
    @Transactional
    public void storeZoneDateTimeWithZoneIdConfigShouldBeStoredOnGMTTimeZone() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("zoned_date_time", dateTimeWrapper.getId());
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(request);
        String expectedValue = dateTimeWrapper
            .getZonedDateTime()
            .format(dateTimeFormatter);

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    @Test
    @Transactional
    public void storeLocalTimeWithZoneIdConfigShouldBeStoredOnGMTTimeZoneAccordingToHis1stJan1970Value() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("local_time", dateTimeWrapper.getId());
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(request);
        String expectedValue = dateTimeWrapper
            .getLocalTime()
            .atDate(LocalDate.of(1970, Month.JANUARY, 1))
            .atZone(ZoneId.systemDefault())
            .format(timeFormatter);

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    @Test
    @Transactional
    public void storeOffsetTimeWithZoneIdConfigShouldBeStoredOnGMTTimeZoneAccordingToHis1stJan1970Value() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("offset_time", dateTimeWrapper.getId());
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(request);
        String expectedValue = dateTimeWrapper
            .getOffsetTime()
            .toLocalTime()
            .atDate(LocalDate.of(1970, Month.JANUARY, 1))
            .atZone(ZoneId.systemDefault())
            .format(timeFormatter);

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    @Test
    @Transactional
    public void storeLocalDateWithZoneIdConfigShouldBeStoredWithoutTransformation() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("local_date", dateTimeWrapper.getId());
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(request);
        String expectedValue = dateTimeWrapper
            .getLocalDate()
            .format(dateFormatter);

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    private String generateSqlRequest(String fieldName, UUID id) {
        return format("SELECT %s FROM jhi_date_time_wrapper where id='%s'", fieldName, id.toString());
    }

    private void assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(SqlRowSet sqlRowSet, String expectedValue) {
        while (sqlRowSet.next()) {
            String dbValue = sqlRowSet.getString(1);

            assertThat(dbValue).isNotNull();
            assertThat(dbValue).isEqualTo(expectedValue);
        }
    }
}
