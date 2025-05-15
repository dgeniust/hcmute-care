package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.UserSummaryResponse;
import vn.edu.hcmute.utecare.util.enumeration.Role;

public interface UserService {
    PageResponse<UserSummaryResponse> findAll(String keyword, Role role, int page, int size, String sort, String direction);
}
