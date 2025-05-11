package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.DoctorRequest;
import vn.edu.hcmute.utecare.dto.response.*;
import vn.edu.hcmute.utecare.service.DoctorService;
import vn.edu.hcmute.utecare.service.ScheduleService;
import vn.edu.hcmute.utecare.service.ScheduleSlotService;
import vn.edu.hcmute.utecare.service.TicketService;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@Tag(name = "DOCTOR", description = "API quản lý thông tin bác sĩ, lịch làm việc, vé khám và xét nghiệm y tế")
@Slf4j(topic = "DOCTOR_CONTROLLER")
public class DoctorController {
    private final DoctorService doctorService;
    private final ScheduleService scheduleService;
    private final TicketService ticketService;
    private final ScheduleSlotService scheduleSlotService;

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'CUSTOMER')")
    @Operation(
            summary = "Lấy thông tin bác sĩ theo ID",
            description = "Truy xuất thông tin chi tiết của một bác sĩ dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin bác sĩ thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bác sĩ với ID được cung cấp")
    })
    public ResponseData<DoctorResponse> getDoctorById(
            @Parameter(description = "ID của bác sĩ cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin bác sĩ với ID: {}", id);
        return ResponseData.<DoctorResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin bác sĩ thành công")
                .data(doctorService.getDoctorById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tạo bác sĩ mới",
            description = "Tạo một bác sĩ mới với thông tin chi tiết và tài khoản liên kết."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo bác sĩ thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "409", description = "Bác sĩ hoặc tài khoản liên kết đã tồn tại")
    })
    public ResponseData<DoctorResponse> createDoctor(@RequestBody @Valid DoctorRequest request) {
        log.info("Yêu cầu tạo bác sĩ mới: {}", request);
        return ResponseData.<DoctorResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo bác sĩ thành công")
                .data(doctorService.createDoctor(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Cập nhật thông tin bác sĩ",
            description = "Cập nhật thông tin của một bác sĩ hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật bác sĩ thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bác sĩ với ID được cung cấp")
    })
    public ResponseData<DoctorResponse> updateDoctor(
            @Parameter(description = "ID của bác sĩ cần cập nhật") @PathVariable("id") Long id,
            @RequestBody @Valid DoctorRequest request) {
        log.info("Yêu cầu cập nhật bác sĩ với ID: {}", id);
        return ResponseData.<DoctorResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật bác sĩ thành công")
                .data(doctorService.updateDoctor(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Xóa bác sĩ",
            description = "Xóa một bác sĩ và tài khoản liên kết dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa bác sĩ thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bác sĩ với ID được cung cấp")
    })
    public ResponseData<Void> deleteDoctor(
            @Parameter(description = "ID của bác sĩ cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa bác sĩ với ID: {}", id);
        doctorService.deleteDoctor(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa bác sĩ thành công")
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách tất cả bác sĩ",
            description = "Truy xuất danh sách bác sĩ phân trang, hỗ trợ sắp xếp theo các tiêu chí và thứ tự."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách bác sĩ thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<DoctorResponse>> getAllDoctors(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bác sĩ mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createdAt)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách bác sĩ: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<DoctorResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách bác sĩ thành công")
                .data(doctorService.getAllDoctors(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'CUSTOMER')")
    @Operation(
            summary = "Tìm kiếm bác sĩ",
            description = "Tìm kiếm bác sĩ theo từ khóa (ví dụ: tên, chuyên khoa) với phân trang."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm bác sĩ thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<DoctorResponse>> searchDoctors(
            @Parameter(description = "Từ khóa tìm kiếm (ví dụ: tên, chuyên khoa)") @RequestParam(required = false) String keyword,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bác sĩ mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, createdAt)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu tìm kiếm bác sĩ với từ khóa: {}", keyword);
        return ResponseData.<PageResponse<DoctorResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm bác sĩ thành công")
                .data(doctorService.searchDoctors(keyword, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/schedule")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'CUSTOMER')")
    @Operation(
            summary = "Lấy lịch làm việc của bác sĩ theo ngày",
            description = "Truy xuất thông tin lịch làm việc của một bác sĩ dựa trên ID và ngày cụ thể."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy lịch làm việc thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy lịch làm việc cho bác sĩ vào ngày được cung cấp")
    })
    public ResponseData<ScheduleResponse> getDoctorSchedule(
            @Parameter(description = "ID của bác sĩ") @PathVariable("id") Long id,
            @Parameter(description = "Ngày cần lấy lịch (định dạng yyyy-MM-dd)") @RequestParam LocalDate date) {
        log.info("Yêu cầu lấy lịch làm việc của bác sĩ với ID: {} vào ngày: {}", id, date);
        return ResponseData.<ScheduleResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy lịch làm việc thành công")
                .data(scheduleService.getDoctorSchedule(id, date))
                .build();
    }

    @GetMapping("/{id}/schedule-slots")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách khung giờ làm việc",
            description = "Truy xuất danh sách các khung giờ làm việc của một bác sĩ dựa trên ID và ngày cụ thể."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách khung giờ thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khung giờ cho bác sĩ vào ngày được cung cấp")
    })
    public ResponseData<List<ScheduleSlotResponse>> getDoctorScheduleSlots(
            @Parameter(description = "ID của bác sĩ") @PathVariable("id") Long id,
            @Parameter(description = "Ngày cần lấy khung giờ (định dạng yyyy-MM-dd)") @RequestParam LocalDate date) {
        log.info("Yêu cầu lấy danh sách khung giờ của bác sĩ với ID: {} vào ngày: {}", id, date);
        return ResponseData.<List<ScheduleSlotResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách khung giờ thành công")
                .data(scheduleSlotService.getAllScheduleSlotsByDoctorIdAndDate(id, date))
                .build();
    }

    @GetMapping("/{id}/schedules")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR', 'CUSTOMER')")
    @Operation(
            summary = "Lấy danh sách lịch làm việc",
            description = "Truy xuất danh sách lịch làm việc phân trang của một bác sĩ trong khoảng thời gian cụ thể."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách lịch làm việc thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    public ResponseData<PageResponse<ScheduleResponse>> getDoctorSchedules(
            @Parameter(description = "ID của bác sĩ") @PathVariable("id") Long id,
            @Parameter(description = "Ngày bắt đầu (định dạng yyyy-MM-dd, tùy chọn)") @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "Ngày kết thúc (định dạng yyyy-MM-dd, tùy chọn)") @RequestParam(required = false) LocalDate endDate,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng lịch mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: id, date)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "asc") String direction) {
        log.info("Yêu cầu lấy danh sách lịch làm việc của bác sĩ với ID: {} từ {} đến {}", id, startDate, endDate);
        return ResponseData.<PageResponse<ScheduleResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách lịch làm việc thành công")
                .data(scheduleService.getDoctorSchedules(id, startDate, endDate, page, size, sort, direction))
                .build();
    }

    @GetMapping("/{id}/tickets")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR')")
    @Operation(
            summary = "Lấy danh sách vé khám",
            description = "Truy xuất danh sách vé khám của một bác sĩ dựa trên ID, ngày và trạng thái (tùy chọn)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách vé khám thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy vé khám cho bác sĩ vào ngày được cung cấp")
    })
    public ResponseData<List<TicketResponse>> getDoctorTickets(
            @Parameter(description = "ID của bác sĩ") @PathVariable("id") Long id,
            @Parameter(description = "Ngày của vé khám (định dạng yyyy-MM-dd)") @RequestParam LocalDate date,
            @Parameter(description = "Trạng thái vé khám (ví dụ: PENDING, CONFIRMED)") @RequestParam(required = false) TicketStatus status) {
        log.info("Yêu cầu lấy danh sách vé khám của bác sĩ với ID: {} vào ngày: {}", id, date);
        return ResponseData.<List<TicketResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách vé khám thành công")
                .data(ticketService.getAllTicketsByDoctorId(id, date, status))
                .build();
    }

//    @GetMapping("/patient/{patientId}/medical-tests-date")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR')")
//    @Operation(
//            summary = "Lấy danh sách xét nghiệm y tế theo ngày",
//            description = "Truy xuất danh sách các xét nghiệm y tế của một bệnh nhân dựa trên ID và ngày cụ thể, bao gồm các loại như xét nghiệm máu, xét nghiệm tim mạch, chụp hình ảnh, xét nghiệm tiêu hóa, EEG, EMG, đo hô hấp, phân tích khí máu, dẫn truyền thần kinh."
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Lấy danh sách xét nghiệm y tế thành công"),
//            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
//            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm y tế cho bệnh nhân vào ngày được cung cấp")
//    })
//    public ResponseData<List<MedicalTestDetailResponse>> getMedicalTestsByPatientId(
//            @Parameter(description = "ID của bệnh nhân") @PathVariable("patientId") Long patientId,
//            @Parameter(description = "Ngày cần lấy xét nghiệm (định dạng yyyy-MM-dd, tùy chọn)") @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date) {
//        log.info("Yêu cầu lấy danh sách xét nghiệm y tế cho bệnh nhân với ID: {} và ngày: {}", patientId, date);
//        return ResponseData.<List<MedicalTestDetailResponse>>builder()
//                .status(HttpStatus.OK.value())
//                .message("Lấy danh sách xét nghiệm y tế thành công")
//                .data(doctorService.getMedicalTestsByPatientId(patientId, date))
//                .build();
//    }
//
//    @GetMapping("/patient/{patientId}/medical-tests/all")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR')")
//    @Operation(
//            summary = "Lấy tất cả xét nghiệm y tế",
//            description = "Truy xuất toàn bộ danh sách xét nghiệm y tế của một bệnh nhân dựa trên ID, bao gồm các loại như xét nghiệm máu, xét nghiệm tim mạch, chụp hình ảnh, xét nghiệm tiêu hóa, EEG, EMG, đo hô hấp, phân tích khí máu, dẫn truyền thần kinh."
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Lấy tất cả xét nghiệm y tế thành công"),
//            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
//            @ApiResponse(responseCode = "404", description = "Không tìm thấy xét nghiệm y tế cho bệnh nhân")
//    })
//    public ResponseData<List<MedicalTestDetailResponse>> getAllMedicalTestsByPatientId(
//            @Parameter(description = "ID của bệnh nhân") @PathVariable("patientId") Long patientId) {
//        log.info("Yêu cầu lấy tất cả xét nghiệm y tế cho bệnh nhân với ID: {}", patientId);
//        return ResponseData.<List<MedicalTestDetailResponse>>builder()
//                .status(HttpStatus.OK.value())
//                .message("Lấy tất cả xét nghiệm y tế thành công")
//                .data(doctorService.getMedicalTestsByPatientId(patientId))
//                .build();
//    }
}