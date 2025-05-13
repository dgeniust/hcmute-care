package vn.edu.hcmute.utecare.configuration;

import com.infobip.ApiClient;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;
import com.infobip.api.SmsApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SMSConfig {

    @Value("${SMS.api-key}")
    private String apiKey;

    @Value("${SMS.base-url}")
    private String baseUrl;

    @Bean
    public ApiClient infobipApiClient() {
        return ApiClient.forApiKey(ApiKey.from(apiKey))
                .withBaseUrl(BaseUrl.from(baseUrl))
                .build();
    }

    @Bean
    public SmsApi smsApi(ApiClient apiClient) {
        return new SmsApi(apiClient);
    }
}
