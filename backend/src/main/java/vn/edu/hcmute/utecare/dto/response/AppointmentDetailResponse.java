package vn.edu.hcmute.utecare.dto.response;

import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;

@Getter
@Setter
public class AppointmentDetailResponse {
    private Long id;

    private MedicalRecordResponse medicalRecord;

    private DoctorScheduleResponse doctorSchedule;

    private AppointmentStatus status;

    private Integer waitingNumber;
}
