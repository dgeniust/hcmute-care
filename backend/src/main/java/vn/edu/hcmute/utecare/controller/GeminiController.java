package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmute.utecare.dto.request.PromptRequest;
import vn.edu.hcmute.utecare.dto.response.PromptResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.GeminiService;
import vn.edu.hcmute.utecare.service.RagService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/gemini")
@RequiredArgsConstructor
@Tag(name = "Gemini", description = "Gemini API cho việc tạo nội dung với RAG")
@Slf4j(topic = "GEMINI_CONTROLLER")
public class GeminiController {

    private final GeminiService geminiService;
    private  final RagService ragService;

    @PostMapping("/generate")
    @Operation(summary = "Tạo nội dung với Gemini API và RAG", description = "Tạo nội dung dựa trên prompt, được bổ sung bởi tài liệu liên quan từ file")
    public ResponseData<PromptResponse> generateContent(@RequestBody @Valid PromptRequest request) {
        log.info("Yêu cầu tạo nội dung với prompt: {}", request.getPrompt());

        try {
            // Bổ sung prompt với RAG
            String augmentedPrompt = ragService.augmentPrompt(request.getPrompt());
            log.info("Prompt đã bổ sung: {}", augmentedPrompt);

            // Gọi Gemini API với prompt đã bổ sung
            String apiResponse = geminiService.callGeminiAPI(augmentedPrompt);
            PromptResponse response = new PromptResponse(apiResponse);

            return ResponseData.<PromptResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Tạo nội dung thành công")
                    .data(response)
                    .build();
        } catch (IOException e) {
            log.error("Lỗi khi đọc tài liệu: {}", e.getMessage());
            return ResponseData.<PromptResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Lỗi khi đọc tài liệu: " + e.getMessage())
                    .data(new PromptResponse("Không thể tạo nội dung do lỗi đọc tài liệu"))
                    .build();
        }
    }
}