package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.StaffCreationRequest;
import vn.edu.hcmute.utecare.dto.request.StaffRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.StaffResponse;

public interface StaffService {
    StaffResponse createStaff(StaffCreationRequest request);
    StaffResponse getStaffById(Long id);
    StaffResponse updateStaff(Long id, StaffRequest request);
    void deleteStaff(Long id);
    PageResponse<StaffResponse> getAllStaff(int page, int size, String sort, String direction);
    PageResponse<StaffResponse> searchStaff(String keyword, int page, int size, String sort, String direction);
}