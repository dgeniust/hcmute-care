package vn.edu.hcmute.utecare.service;


import vn.edu.hcmute.utecare.dto.request.PatientRequest;
import vn.edu.hcmute.utecare.dto.response.PatientResponse;

public interface PatientService {

    PatientResponse getById(Long id);
    PatientResponse update(Long id, PatientRequest request);

}