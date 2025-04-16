package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.CreateAppointmentRequest;
import vn.edu.hcmute.utecare.dto.response.AppointmentDetailResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.AppointmentMapper;
import vn.edu.hcmute.utecare.model.Appointment;
import vn.edu.hcmute.utecare.model.DoctorSchedule;
import vn.edu.hcmute.utecare.model.MedicalRecord;
import vn.edu.hcmute.utecare.repository.AppointmentRepository;
import vn.edu.hcmute.utecare.repository.DoctorScheduleRepository;
import vn.edu.hcmute.utecare.repository.MedicalRecordRepository;
import vn.edu.hcmute.utecare.service.AppointmentService;
import vn.edu.hcmute.utecare.util.enumeration.AppointmentStatus;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

    @Override
    @Transactional
    public AppointmentDetailResponse createAppointment(CreateAppointmentRequest request){
        log.info("Creating appointment with request: {}", request);

        MedicalRecord medicalRecord = medicalRecordRepository.findById(request.getMedicalRecordId())
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findById(request.getDoctorScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor schedule not found"));

        if (appointmentRepository.existsByDoctorScheduleAndMedicalRecord(doctorSchedule, medicalRecord)) {
            throw new IllegalArgumentException("Appointment already exists for this medical record and doctor schedule");
        }

        if (doctorSchedule.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past");
        }

        if (doctorSchedule.getBookedSlots() >= doctorSchedule.getMaxSlots()) {
            throw new IllegalArgumentException("No available slots for this doctor schedule");
        }

        Appointment appointment = Appointment.builder()
                .medicalRecord(medicalRecord)
                .doctorSchedule(doctorSchedule)
                .status(AppointmentStatus.PENDING)
                .build();

        appointmentRepository.save(appointment);

        doctorSchedule.setBookedSlots(doctorSchedule.getBookedSlots() + 1);

        doctorScheduleRepository.save(doctorSchedule);

        return AppointmentMapper.INSTANCE.toDetailResponse(appointment);
    }

}
