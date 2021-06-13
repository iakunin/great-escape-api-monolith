package com.greatescape.api.monolith.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI PHONE_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/phone-already-used");
    public static final URI WRONG_OTP = URI.create(PROBLEM_BASE_URL + "/wrong-otp");
    public static final URI RE_CAPTCHA = URI.create(PROBLEM_BASE_URL + "/re-captcha");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");
    public static final URI SLOT_NOT_FOUND = URI.create(PROBLEM_BASE_URL + "/slot-not-found");
    public static final URI PLAYER_NOT_FOUND = URI.create(PROBLEM_BASE_URL + "/player-not-found");
    public static final URI SLOT_ALREADY_BOOKED = URI.create(PROBLEM_BASE_URL + "/slot-already-booked");
    public static final URI SLOT_UNAVAILABLE_FOR_BOOKING = URI.create(PROBLEM_BASE_URL + "/slot-unavailable-for-booking");
    public static final URI SLOT_TIME_ALREADY_PASSED = URI.create(PROBLEM_BASE_URL + "/slot-time-already-passed");

    private ErrorConstants() {
    }
}
