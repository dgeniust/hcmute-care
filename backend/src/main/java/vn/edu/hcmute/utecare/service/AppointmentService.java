package vn.edu.hcmute.utecare.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.CreateAppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentDetailResponse;
import vn.edu.hcmute.utecare.dto.response.AppointmentSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.DoctorAppointmentResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.model.Appointment;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;

import java.time.LocalDate;

public interface AppointmentService {
    AppointmentDetailResponse createAppointment(CreateAppointmentRequest request);

    Appointment confirmAppointment(Long appointmentId);

     Appointment cancelAppointment(Long appointmentId);

    AppointmentDetailResponse getAppointmentById(Long id);

    DoctorAppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus status);

    PageResponse<AppointmentSummaryResponse> getAllAppointments(Long medicalRecordId, int page, int size, String sort, String direction);

    PageResponse<DoctorAppointmentResponse> getAllAppointments(Long doctorId, int page, int size, String sort, String direction, LocalDate date, AppointmentStatus status, Integer timeSlotId);

    PageResponse<AppointmentSummaryResponse> getAllAppointments(int page, int size, String sort, String direction, LocalDate startDate, LocalDate endDate);

    PageResponse<AppointmentSummaryResponse> getAllAppointments(Pageable pageable, String[] appointment, String[] medicalRecord, String[] patient);
}
