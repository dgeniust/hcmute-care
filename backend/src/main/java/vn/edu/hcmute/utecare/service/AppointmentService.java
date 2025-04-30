package vn.edu.hcmute.utecare.service;

import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.AppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentResponse;

public interface AppointmentService {
    @Transactional
    AppointmentResponse createAppointment(AppointmentRequest request);

    @Transactional(readOnly = true)
    AppointmentResponse getAppointmentById(Long id);
}
