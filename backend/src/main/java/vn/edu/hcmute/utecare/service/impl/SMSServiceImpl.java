package vn.edu.hcmute.utecare.service.impl;

import com.infobip.ApiException;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsMessage;
import com.infobip.model.SmsRequest;
import com.infobip.model.SmsTextContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.service.SMSService;

import java.util.List;

/**
 * Implementation of SMS service using Infobip API.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SMSServiceImpl implements SMSService {

    private static final String INTERNATIONAL_PREFIX = "+84";
    private static final String LOCAL_PREFIX = "0";
    private static final String SENDER_NAME = "InfoSMS";

    private final SmsApi smsApi;

    @Value("${SMS.sender-id}")
    private String senderId;

    @Override
    public void sendSms(String phone, String message) throws Exception {
        String phoneNumber = convertToInternationalFormat(phone.trim());

        SmsMessage smsMessage = new SmsMessage()
                .sender(senderId)
                .addDestinationsItem(new SmsDestination().to(phoneNumber))
                .content(new SmsTextContent().text(message.trim()));

        SmsRequest smsRequest = new SmsRequest().messages(List.of(smsMessage));

        try {
            smsApi.sendSmsMessages(smsRequest).execute();
            log.info("SMS sent successfully to {}", phoneNumber);
        } catch (ApiException e) {
            log.error("Failed to send SMS to {}: {}", phoneNumber, e.getMessage());
            throw new Exception("Failed to send SMS: " + e.getMessage(), e);
        }
    }

    private String convertToInternationalFormat(String phoneNumber) {
        String cleanedNumber = phoneNumber.replaceAll("[\\s-]", "");

        if (cleanedNumber.startsWith(INTERNATIONAL_PREFIX)) {
            return cleanedNumber;
        }

        if (cleanedNumber.startsWith(LOCAL_PREFIX)) {
            return INTERNATIONAL_PREFIX + cleanedNumber.substring(1);
        }

        throw new IllegalArgumentException(
                "Invalid phone number format. Must start with 0 or +84 (e.g., 0979859559 or +84979859559)");
    }
}