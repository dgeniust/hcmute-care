package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.UserSummaryResponse;
import vn.edu.hcmute.utecare.mapper.UserMapper;
import vn.edu.hcmute.utecare.model.User;
import vn.edu.hcmute.utecare.repository.UserRepository;
import vn.edu.hcmute.utecare.service.UserService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.Role;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public PageResponse<UserSummaryResponse> findAll(String keyword, Role role, int page, int size, String sort, String direction){
        log.info("Đang tìm kiếm người dùng với từ khóa: {}", keyword);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<User> userPage = userRepository.findAll(keyword, role, pageable);

        return PageResponse.<UserSummaryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .content(userPage.getContent().stream().map(userMapper::toSummaryResponse).toList())
                .build();
    }

}
