package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.UserSummaryResponse;
import vn.edu.hcmute.utecare.service.UserService;
import vn.edu.hcmute.utecare.util.enumeration.AccountStatus;
import vn.edu.hcmute.utecare.util.enumeration.Role;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User Management API")
@Slf4j(topic = "USER_CONTROLLER")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseData<PageResponse<UserSummaryResponse>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Role role,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction){
        log.info("Searching users with keyword: {}", keyword);

        return ResponseData.<PageResponse<UserSummaryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Users retrieved successfully")
                .data(userService.findAll(keyword, role, page, size, sort, direction))
                .build();
    }
}
