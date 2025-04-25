package vn.edu.hcmute.utecare.service;

import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.utecare.dto.request.CreateAppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentDetailResponse;
import vn.edu.hcmute.utecare.dto.response.AppointmentSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.DoctorAppointmentResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;

import java.time.LocalDate;

public interface AppointmentService {
    AppointmentDetailResponse createAppointment(CreateAppointmentRequest request);

    AppointmentDetailResponse getAppointmentById(Long id);

    AppointmentDetailResponse updateAppointmentStatus(Long id, AppointmentStatus status);

    PageResponse<AppointmentSummaryResponse> getAllAppointments(Long medicalRecordId, int page, int size, String sort, String direction);

    PageResponse<DoctorAppointmentResponse> getAllAppointments(Long doctorId, int page, int size, String sort, String direction, LocalDate date, AppointmentStatus status, Integer timeSlotId);

    PageResponse<AppointmentSummaryResponse> getAllAppointments(int page, int size, String sort, String direction, LocalDate startDate, LocalDate endDate);

    PageResponse<AppointmentSummaryResponse> getAllAppointments(Pageable pageable, String[] appointment, String[] medicalRecord, String[] patient);
}
