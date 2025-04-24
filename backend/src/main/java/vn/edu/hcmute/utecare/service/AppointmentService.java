package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.CreateAppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentDetailResponse;
import vn.edu.hcmute.utecare.dto.response.AppointmentSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;

import java.time.LocalDate;

public interface AppointmentService {
    AppointmentDetailResponse createAppointment(CreateAppointmentRequest request);

    AppointmentDetailResponse getAppointmentById(Long id);

    PageResponse<AppointmentSummaryResponse> getAllAppointments(int page, int size, String sort, String direction, LocalDate startDate, LocalDate endDate);

    PageResponse<AppointmentSummaryResponse> getAllAppointments(int pageNo, int size, String search[], String sortBy);
}
