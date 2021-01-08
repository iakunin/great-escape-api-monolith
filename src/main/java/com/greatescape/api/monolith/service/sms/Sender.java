package com.greatescape.api.monolith.service.sms;

import lombok.Value;

public interface Sender {

    void send(Request request) throws UnableToSendException;

    class UnableToSendException extends Exception {
        private static final long serialVersionUID = 1L;

        public UnableToSendException(String message) {
            super(message);
        }

        public UnableToSendException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @Value
    class Request {
        String phone;
        String text;
    }
}
