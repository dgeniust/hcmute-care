package vn.edu.hcmute.utecare.exception;

public class TooManyOtpRequestsException extends RuntimeException {
    public TooManyOtpRequestsException(String message) {
        super(message);
    }
}
