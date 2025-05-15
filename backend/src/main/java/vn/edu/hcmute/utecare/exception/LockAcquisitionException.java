package vn.edu.hcmute.utecare.exception;

public class LockAcquisitionException extends RuntimeException {
    public LockAcquisitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockAcquisitionException(String message) {
        super(message);
    }
}