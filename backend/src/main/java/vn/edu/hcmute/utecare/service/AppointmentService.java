package vn.edu.hcmute.utecare.service;

import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.AppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.model.Appointment;

import java.math.BigDecimal;

public interface AppointmentService {
    AppointmentResponse createAppointment(AppointmentRequest request);

    AppointmentResponse getAppointmentById(Long id);

    PageResponse<AppointmentResponse> getAppointmentByMedicalRecordId(Long medicalRecordId
            , int page, int size, String sort, String direction);

    AppointmentResponse confirmAppointment(Long appointmentId);

    AppointmentResponse cancelAppointment(Long appointmentId);

    BigDecimal calculateTotalPrice(Appointment appointment);
}
