package com.greatescape.api.monolith.service.sms;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.dezhik.sms.sender.SenderService;
import ru.dezhik.sms.sender.api.InvocationStatus;
import ru.dezhik.sms.sender.api.smsru.SMSRuResponseStatus;
import ru.dezhik.sms.sender.api.smsru.send.SMSRuSendRequest;
import ru.dezhik.sms.sender.api.smsru.send.SMSRuSendResponse;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class SenderImpl implements Sender {

    private final SenderService senderService;

    @Override
    @Async
    public void send(Request request) throws UnableToSendException {
        SMSRuSendRequest sendRequest = new SMSRuSendRequest();
        sendRequest.setReceivers(Collections.singleton(request.getPhone()));
        sendRequest.setText(request.getText());
        SMSRuSendResponse sendResponse = senderService.execute(sendRequest);

        if (sendRequest.getStatus() == InvocationStatus.SUCCESS) {
            if (sendResponse.getResponseStatus() != SMSRuResponseStatus.IN_QUEUE) {
                throw new UnableToSendException(
                    String.format(
                        "Failed with response status='%s'",
                        sendResponse.getResponseStatus()
                    )
                );
            }

            return;
        }

        if (sendRequest.getStatus().isAbnormal()) {
            throw new UnableToSendException(
                String.format(
                    "Request is not success. Current request status = %s",
                    sendRequest.getStatus()
                ),
                sendRequest.getException()
            );
        }

        throw new UnableToSendException(
            String.format(
                "Request is not success. Current request status = %s",
                sendRequest.getStatus()
            )
        );
    }
}
