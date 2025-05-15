package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.response.DoctorTicketSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.TicketResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.TicketMapper;
import vn.edu.hcmute.utecare.model.Ticket;
import vn.edu.hcmute.utecare.repository.TicketRepository;
import vn.edu.hcmute.utecare.service.TicketService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.TicketStatus;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DoctorTicketSummaryResponse> getTicketSummaryByScheduleSlot(Long scheduleSlotId, TicketStatus status) {
        log.info("Truy xuất danh sách vé khám cho khung lịch với ID: {}, trạng thái: {}", scheduleSlotId, status);
        List<Ticket> ticketSummary = ticketRepository.getTicketSummaryByScheduleSlotId(scheduleSlotId, status);
        return ticketSummary.stream().map(ticketMapper::toDoctorTicketSummaryResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getTicketById(Long ticketId) {
        log.info("Truy xuất vé khám với ID: {}", ticketId);
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vé khám với ID: " + ticketId));
        log.info("Truy xuất vé khám thành công với ID: {}", ticketId);
        return ticketMapper.toResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse updateTicketStatus(Long ticketId, TicketStatus status) {
        log.info("Cập nhật trạng thái vé khám với ID: {}, trạng thái: {}", ticketId, status);
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vé khám với ID: " + ticketId));
        ticket.setStatus(status);
        Ticket updatedTicket = ticketRepository.save(ticket);
        log.info("Cập nhật trạng thái vé khám thành công với ID: {}", ticketId);
        return ticketMapper.toResponse(updatedTicket);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TicketResponse> getAllTicketsByMedicalRecordId(
            Long medicalRecordId, TicketStatus status, int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách vé khám theo hồ sơ y tế với ID: {}, trạng thái: {}, trang: {}, kích thước: {}, sắp xếp: {}, hướng: {}",
                medicalRecordId, status, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Ticket> ticketPage = ticketRepository.findAllByMedicalRecordIdAndStatus(medicalRecordId, status, pageable);
        return PageResponse.<TicketResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(ticketPage.getTotalElements())
                .totalPages(ticketPage.getTotalPages())
                .content(ticketPage.getContent().stream()
                        .map(ticketMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> getAllTicketsByDoctorId(Long doctorId, LocalDate date, TicketStatus status) {
        log.info("Truy xuất danh sách vé khám theo bác sĩ với ID: {}, ngày: {}, trạng thái: {}", doctorId, date, status);
        List<Ticket> tickets = ticketRepository.findAllByDoctorIdAndDateAndStatus(doctorId, date, status);
        return tickets.stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TicketResponse> getAllTicket(int page, int size, String sort, String direction,
                                                     LocalDate scheduleDate,
                                                     TicketStatus status,
                                                     Long doctorId,
                                                     Long patientId) {
        log.info("Truy xuất danh sách vé khám: trang={}, kích thước={}, sắp xếp={}, hướng={}, ngày lịch khám={}, trạng thái={}, bác sĩ={}, bệnh nhân={}",
                page, size, sort, direction, scheduleDate, status, doctorId, patientId);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Ticket> ticketPage = ticketRepository.findAllTicket(status, doctorId, patientId, scheduleDate, pageable);
        return PageResponse.<TicketResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(ticketPage.getTotalElements())
                .totalPages(ticketPage.getTotalPages())
                .content(ticketPage.getContent().stream()
                        .map(ticketMapper::toResponse)
                        .toList())
                .build();
    }
}