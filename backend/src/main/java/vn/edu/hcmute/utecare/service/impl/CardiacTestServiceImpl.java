package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.CardiacTestRequest;
import vn.edu.hcmute.utecare.dto.response.CardiacTestResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.CardiacTestMapper;
import vn.edu.hcmute.utecare.model.CardiacTest;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.repository.CardiacTestRepository;
import vn.edu.hcmute.utecare.repository.EncounterRepository;
import vn.edu.hcmute.utecare.service.CardiacTestService;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.EMedicalTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardiacTestServiceImpl implements CardiacTestService {

    private final CardiacTestRepository cardiacTestRepository;
    private final EncounterRepository encounterRepository;
    private final CardiacTestMapper cardiacTestMapper;

    @Override
    @Transactional
    public CardiacTestResponse createCardiacTest(CardiacTestRequest request) {
        log.info("Tạo xét nghiệm tim mạch mới: {}", request);
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        CardiacTest cardiacTest = cardiacTestMapper.toEntity(request);
        cardiacTest.setEncounter(encounter);
        cardiacTest.setStatus(EMedicalTest.PENDING);
        CardiacTest savedCardiacTest = cardiacTestRepository.save(cardiacTest);
        log.info("Tạo xét nghiệm tim mạch thành công với ID: {}", savedCardiacTest.getId());
        return cardiacTestMapper.toResponse(savedCardiacTest);
    }

    @Override
    @Transactional(readOnly = true)
    public CardiacTestResponse getCardiacTestById(Long id) {
        log.info("Truy xuất xét nghiệm tim mạch với ID: {}", id);
        CardiacTest cardiacTest = cardiacTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm tim mạch với ID: " + id));
        log.info("Truy xuất xét nghiệm tim mạch thành công với ID: {}", id);
        return cardiacTestMapper.toResponse(cardiacTest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardiacTestResponse> getAll() {
        log.info("Truy xuất danh sách tất cả xét nghiệm tim mạch");
        return cardiacTestRepository.findAll().stream()
                .map(cardiacTestMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CardiacTestResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách xét nghiệm tim mạch: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<CardiacTest> cardiacTestPage = cardiacTestRepository.findAll(pageable);
        return PageResponse.<CardiacTestResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(cardiacTestPage.getTotalPages())
                .totalElements(cardiacTestPage.getTotalElements())
                .content(cardiacTestPage.getContent().stream()
                        .map(cardiacTestMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public CardiacTestResponse updateCardiacTest(Long id, CardiacTestRequest request) {
        log.info("Cập nhật xét nghiệm tim mạch với ID: {} và thông tin: {}", id, request);
        CardiacTest cardiacTest = cardiacTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm tim mạch với ID: " + id));
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc gặp với ID: " + request.getEncounterId()));
        cardiacTestMapper.updateEntity(cardiacTest, request);
        cardiacTest.setEncounter(encounter);
        CardiacTest updatedCardiacTest = cardiacTestRepository.save(cardiacTest);
        log.info("Cập nhật xét nghiệm tim mạch thành công với ID: {}", id);
        return cardiacTestMapper.toResponse(updatedCardiacTest);
    }

    @Override
    @Transactional
    public void deleteCardiacTest(Long id) {
        log.info("Xóa xét nghiệm tim mạch với ID: {}", id);
        CardiacTest cardiacTest = cardiacTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xét nghiệm tim mạch với ID: " + id));
        cardiacTestRepository.delete(cardiacTest);
        log.info("Xóa xét nghiệm tim mạch thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardiacTestResponse> getAllLabTestByDateAndStatus(LocalDate date, String status) {
        log.info("Truy xuất danh sách xét nghiệm tim mạch theo ngày: {} và trạng thái: {}", date, status);
        try {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            EMedicalTest statusEnum = EMedicalTest.valueOf(status);
            return cardiacTestRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                    .stream()
                    .map(cardiacTestMapper::toResponse)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Trạng thái xét nghiệm không hợp lệ: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardiacTestResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Truy xuất danh sách xét nghiệm tim mạch theo cuộc gặp ID: {} và ngày: {}", encounterId, date);
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59);
        return cardiacTestRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(cardiacTestMapper::toResponse)
                .toList();
    }
}