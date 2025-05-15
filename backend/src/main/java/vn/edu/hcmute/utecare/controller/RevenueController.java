package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "REVENUE", description = "API cung cấp thông tin thống kê doanh thu liên quan đến khách hàng và nhân viên")
@Slf4j(topic = "REVENUE_CONTROLLER")
public class RevenueController {
    private final RevenueService revenueService;

    @GetMapping("/customers")
    @Operation(
            summary = "Lấy thống kê doanh thu khách hàng",
            description = "Truy xuất thông tin thống kê doanh thu liên quan đến khách hàng, bao gồm tổng số tài khoản và hồ sơ y tế."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thống kê doanh thu khách hàng thành công")
    })
    public ResponseData<CustomerRevenueResponse> revenueCustomer() {
        log.info("Yêu cầu lấy thống kê doanh thu khách hàng");
        return ResponseData.<CustomerRevenueResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thống kê doanh thu khách hàng thành công")
                .data(revenueService.getCustomerRevenue())
                .build();
    }

    @GetMapping("/employees")
    @Operation(
            summary = "Lấy thống kê doanh thu nhân viên",
            description = "Truy xuất thông tin thống kê doanh thu liên quan đến nhân viên, bao gồm tổng số bác sĩ, y tá và nhân viên khác."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thống kê doanh thu nhân viên thành công")
    })
    public ResponseData<EmployeeRevenueResponse> revenueEmployee() {
        log.info("Yêu cầu lấy thống kê doanh thu nhân viên");
        return ResponseData.<EmployeeRevenueResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thống kê doanh thu nhân viên thành công")
                .data(revenueService.getEmployeeRevenue())
                .build();
    }
}