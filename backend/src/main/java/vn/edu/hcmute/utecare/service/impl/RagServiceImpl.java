package vn.edu.hcmute.utecare.service.impl;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.service.RagService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagServiceImpl implements RagService {
    // Giả sử lưu tạm thời lịch sử hội thoại trong bộ nhớ
    private final List<String> conversationHistory = new ArrayList<>();

    @Override
    public String augmentPrompt(String prompt) throws IOException {
        String keyword = prompt.toLowerCase();

        // Thêm prompt hiện tại vào lịch sử hội thoại
        conversationHistory.add("Người dùng: " + prompt);

        // Ghép lịch sử hội thoại
        String historyContext = String.join("\n", conversationHistory);


        // Tạo prompt cuối cùng cho AI
        return String.format(
                "Bạn là một trợ lý ảo trong hệ thống bệnh viện HCMUTE-CARE.\n" +
                        "Lịch sử hội thoại:" +   historyContext,","+
                        "Câu hỏi hiện tại: " +  prompt +
                        "=> Hãy trả lời ngắn gọn, rõ ràng, đúng chuyên môn, và có thể viết lại thành đoạn văn nếu phù hợp."


        );
    }
}
