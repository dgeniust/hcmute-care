package vn.edu.hcmute.utecare.dto.response;

import lombok.Data;

@Data
public class PromptResponse {
    private String content;

    public PromptResponse(String content) {
        this.content = content;
    }
}