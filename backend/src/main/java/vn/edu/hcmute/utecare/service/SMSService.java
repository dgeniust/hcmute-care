package vn.edu.hcmute.utecare.service;

public interface SMSService {
    void sendSms(String phoneNumber, String message) throws Exception;
}
