package vn.edu.hcmute.utecare.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Getter
public class VNPayConfig {
    @Value("${payment.vnPay.url}")
    private String vnpPayUrl;

    @Value("${payment.vnPay.returnUrl}")
    private String vnpReturnUrl;

    @Value("${payment.vnPay.tmnCode}")
    private String vnpTmnCode;

    @Value("${payment.vnPay.secretKey}")
    private String secretKey;

    @Value("${payment.vnPay.version}")
    private String vnpVersion;

    @Value("${payment.vnPay.command}")
    private String vnpCommand;

    @Value("${payment.vnPay.orderType}")
    private String orderType;


    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", vnpVersion);
        vnpParamsMap.put("vnp_Command", vnpCommand);
        vnpParamsMap.put("vnp_TmnCode", vnpTmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_OrderType", orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", vnpReturnUrl);
        return vnpParamsMap;
    }
}