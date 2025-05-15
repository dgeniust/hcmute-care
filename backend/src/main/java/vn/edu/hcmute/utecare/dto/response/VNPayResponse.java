package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;

@Builder
public class VNPayResponse {
    public String code;
    public String message;
    public String paymentUrl;
}
