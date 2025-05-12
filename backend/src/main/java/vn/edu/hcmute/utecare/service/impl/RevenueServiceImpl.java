package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.response.CustomerRevenueResponse;
import vn.edu.hcmute.utecare.dto.response.EmployeeRevenueResponse;
import vn.edu.hcmute.utecare.repository.*;
import vn.edu.hcmute.utecare.service.RevenueService;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevenueServiceImpl implements RevenueService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final CustomerRepository customerRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final StaffRepository staffRepository;

    @Override
    public CustomerRevenueResponse getCustomerRevenue() {
        log.info("Truy xuất thống kê doanh thu khách hàng");
        long totalAccounts = customerRepository.count();
        long totalMedicalRecords = medicalRecordRepository.count();
        log.info("Tổng số tài khoản khách hàng: {}, tổng số hồ sơ y tế: {}", totalAccounts, totalMedicalRecords);
        return CustomerRevenueResponse.builder()
                .totalAccounts(totalAccounts)
                .totalMedicalRecords(totalMedicalRecords)
                .build();
    }

    @Override
    public EmployeeRevenueResponse getEmployeeRevenue() {
        log.info("Truy xuất thống kê doanh thu nhân viên");
        long totalDoctors = doctorRepository.count();
        long totalNurses = nurseRepository.count();
        long totalStaff = staffRepository.count();
        log.info("Tổng số bác sĩ: {}, tổng số y tá: {}, tổng số nhân viên: {}", totalDoctors, totalNurses, totalStaff);
        return EmployeeRevenueResponse.builder()
                .totalDoctor(totalDoctors)
                .totalNurse(totalNurses)
                .totalStaff(totalStaff)
                .build();
    }
}