package vn.edu.hcmute.utecare.service;

import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.AppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.model.Appointment;

public interface AppointmentService {
    @Transactional
    AppointmentResponse createAppointment(AppointmentRequest request);

    @Transactional(readOnly = true)
    AppointmentResponse getAppointmentById(Long id);

    @Transactional(readOnly = true)
    PageResponse<AppointmentResponse> getAppointmentByMedicalRecordId(Long medicalRecordId
            , int page, int size, String sort, String direction);

    @Transactional
    AppointmentResponse confirmAppointment(Long appointmentId);

    @Transactional
    AppointmentResponse cancelAppointment(Long appointmentId);
}
