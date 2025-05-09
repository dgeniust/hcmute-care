package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
    public List<DoctorTicketSummaryResponse> getTicketSummaryByScheduleSlot(Long scheduleSlotId,
                                                                            TicketStatus status) {
        log.info("Getting ticket summary for doctor with ID: {}", scheduleSlotId);

        List<Ticket> ticketSummary = ticketRepository.getTicketSummaryByScheduleSlotId(scheduleSlotId, status);
        return ticketSummary.stream().map(ticketMapper::toDoctorTicketSummaryResponse).toList();
    }

    @Override
    public TicketResponse getTicketById(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        return ticketMapper.toResponse(ticket);
    }

    @Override
    public TicketResponse updateTicketStatus(Long ticketId, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        ticket.setStatus(status);
        ticketRepository.save(ticket);
        return ticketMapper.toResponse(ticket);
    }

    @Override
    public PageResponse<TicketResponse> getAllTicketsByMedicalRecordId(Long medicalRecordId,
                                                                       TicketStatus status,
                                                                       int page,
                                                                       int size,
                                                                       String sort,
                                                                       String direction) {
        log.info("Getting all tickets by medical record ID: {}, status: {}, page: {}, size: {}, sort: {}, direction: {}",
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
    public List<TicketResponse> getAllTicketsByDoctorId(Long doctorId,
                                                        LocalDate date,
                                                        TicketStatus status) {
        log.info("Getting all tickets by doctor ID: {}", doctorId);
        List<Ticket> tickets = ticketRepository.findAllByDoctorIdAndDateAndStatus(doctorId, date, status);

        return tickets.stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    @Override
    public PageResponse<TicketResponse> getAllTicket(
            int page, int size, String sort, String direction,
            LocalDate scheduleDate
    ){
        log.info("Getting all tickets");
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Ticket> ticketPage = ticketRepository.findAllTicket(scheduleDate, pageable);

        return PageResponse.<TicketResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(ticketPage.getTotalElements())
                .totalPages(ticketPage.getTotalPages())
                .content(ticketPage.getContent().stream().map(ticketMapper::toResponse).toList())
                .build();

    }
}
