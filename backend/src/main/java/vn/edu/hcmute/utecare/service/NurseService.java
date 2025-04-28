package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.NurseCreationRequest;
import vn.edu.hcmute.utecare.dto.request.NurseRequest;
import vn.edu.hcmute.utecare.dto.response.NurseResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;

public interface NurseService {
    NurseResponse createNurse(NurseRequest request);
    NurseResponse getNurseById(Long id);
    NurseResponse updateNurse(Long id, NurseRequest request);
    void deleteNurse(Long id);
    PageResponse<NurseResponse> getAllNurses(int page, int size, String sort, String direction);
    PageResponse<NurseResponse> searchNurses(String keyword, int page, int size, String sort, String direction);

    PageResponse<NurseResponse> getNursesByMedicalSpecialtyId(Integer medicalSpecialtyId, int page, int size, String sort, String direction);

    PageResponse<NurseResponse> searchNursesByMedicalSpecialtyId(Integer medicalSpecialtyId, String keyword, int page, int size, String sort, String direction);
}
