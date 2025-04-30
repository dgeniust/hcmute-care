package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.response.CustomerRevenueResponse;
import vn.edu.hcmute.utecare.dto.response.EmployeeRevenueResponse;

public interface RevenueService {
    CustomerRevenueResponse getCustomerRevenue();
    EmployeeRevenueResponse getEmployeeRevenue();
}
