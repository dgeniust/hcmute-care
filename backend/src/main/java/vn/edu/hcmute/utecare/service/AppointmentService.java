package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.CreateAppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentDetailResponse;

public interface AppointmentService {
    AppointmentDetailResponse createAppointment(CreateAppointmentRequest request);
}
