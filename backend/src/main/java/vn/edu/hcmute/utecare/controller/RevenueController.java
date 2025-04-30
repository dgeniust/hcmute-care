package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmute.utecare.dto.response.CustomerRevenueResponse;
import vn.edu.hcmute.utecare.dto.response.EmployeeRevenueResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.RevenueService;

@RestController
@RequestMapping("/api/v1/revenue")
@RequiredArgsConstructor
@Tag(name = "Revenue", description = "Revenue API")
@Slf4j(topic = "REVENUE_CONTROLLER")
public class RevenueController {
    private final RevenueService revenueService;

    @GetMapping("/customers")
    @Operation(summary = "Get revenue customer", description = "Retrieve revenue customer")
    public ResponseData<CustomerRevenueResponse> revenueCustomer(){
        log.info("Get revenue customer request");
        CustomerRevenueResponse customerRevenueResponse = revenueService.getCustomerRevenue();
        return ResponseData.<CustomerRevenueResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Revenue customer retrieved successfully")
                .data(customerRevenueResponse)
                .build();
    }

    @GetMapping("/employees")
    @Operation(summary = "Get revenue employee", description = "Retrieve revenue employee")
    public ResponseData<EmployeeRevenueResponse> revenueEmployee(){
        log.info("Get revenue customer request");
        EmployeeRevenueResponse employeeRevenueResponse = revenueService.getEmployeeRevenue();
        return ResponseData.<EmployeeRevenueResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Revenue employee retrieved successfully")
                .data(employeeRevenueResponse)
                .build();
    }
}
