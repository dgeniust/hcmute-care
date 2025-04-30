package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.response.CustomerRevenueResponse;
import vn.edu.hcmute.utecare.dto.response.EmployeeRevenueResponse;
import vn.edu.hcmute.utecare.repository.*;
import vn.edu.hcmute.utecare.service.RevenueService;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final CustomerRepository customerRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final StaffRepository staffRepository;

    @Override
    public CustomerRevenueResponse getCustomerRevenue() {
        long totalAcc = customerRepository.count();
        long totalMedicalRecord = medicalRecordRepository.count();
        return CustomerRevenueResponse.builder()
                .totalAccounts(totalAcc)
                .totalMedicalRecords(totalMedicalRecord)
                .build();
    }

    @Override
    public EmployeeRevenueResponse getEmployeeRevenue() {
        long totalDoctor = doctorRepository.count();
        long totalNurse = nurseRepository.count();
        long totalStaff = staffRepository.count();
        return EmployeeRevenueResponse.builder()
                .totalDoctor(totalDoctor)
                .totalNurse(totalNurse)
                .totalStaff(totalStaff)
                .build();
    }
}
