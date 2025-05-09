package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.CardiacTestRequest;
import vn.edu.hcmute.utecare.dto.response.CardiacTestResponse;
import vn.edu.hcmute.utecare.exception.NotFoundException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardiacTestServiceImpl implements CardiacTestService {

    private final CardiacTestRepository cardiacTestRepository;
    private final EncounterRepository encounterRepository;


    @Override
    public CardiacTestResponse createCardiacTest(CardiacTestRequest request) {
        log.info("Tạo CardiacTest mới: {}", request);
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Encounter với id: " + request.getEncounterId()));
        CardiacTest cardiacTest = CardiacTestMapper.INSTANCE.toEntity(request);
        cardiacTest.setEncounter(encounter);
        cardiacTest.setStatus(EMedicalTest.PENDING);
        CardiacTest savedCardiacTest = cardiacTestRepository.save(cardiacTest);
        return CardiacTestMapper.INSTANCE.toResponse(savedCardiacTest);
    }

    @Override
    public CardiacTestResponse getCardiacTestById(Long id) {
        log.info("Lấy thông tin CardiacTest với id: {}", id);
        CardiacTest cardiacTest = cardiacTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy CardiacTest với id: " + id));
        return CardiacTestMapper.INSTANCE.toResponse(cardiacTest);
    }

    @Override
    public List<CardiacTestResponse> getAll() {
        log.info("Lấy danh sách tất cả CardiacTest");
        return cardiacTestRepository.findAll().stream()
                .map(CardiacTestMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<CardiacTestResponse> getAll(int page, int size, String sort, String direction) {
        log.info("Lấy danh sách CardiacTest với phân trang: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<CardiacTest> cardiacTestPage = cardiacTestRepository.findAll(pageable);

        return PageResponse.<CardiacTestResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(cardiacTestPage.getTotalPages())
                .totalElements(cardiacTestPage.getTotalElements())
                .content(cardiacTestPage.getContent().stream()
                        .map(CardiacTestMapper.INSTANCE::toResponse)
                        .toList())
                .build();
    }

    @Override
    public CardiacTestResponse updateCardiacTest(Long id, CardiacTestRequest request) {
        log.info("Cập nhật CardiacTest với id: {}", id);
        CardiacTest cardiacTest = cardiacTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy CardiacTest với id: " + id));
        Encounter encounter = encounterRepository.findById(request.getEncounterId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Encounter với id: " + request.getEncounterId()));
        CardiacTestMapper.INSTANCE.updateEntity(cardiacTest, request);
        cardiacTest.setEncounter(encounter);
        cardiacTestRepository.save(cardiacTest);
        return CardiacTestMapper.INSTANCE.toResponse(cardiacTest);
    }

    @Override
    public void deleteCardiacTest(Long id) {
        log.info("Xóa CardiacTest với id: {}", id);
        CardiacTest cardiacTest = cardiacTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy CardiacTest với id: " + id));
        cardiacTestRepository.delete(cardiacTest);
    }

    @Override
    public List<CardiacTestResponse> getAllLabTestByDateAndStatus(LocalDate date, String status) {
        log.info("Lấy danh sách LaboratoryTests theo ngày {} và trạng thái PENDING", date);
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59); // 23:59:59
        EMedicalTest statusEnum = EMedicalTest.valueOf(String.valueOf(status)); // statusString là "PENDING", "COMPLETED",...
        return cardiacTestRepository.findByCreateDateBetweenAndStatus(startOfDay, endOfDay, statusEnum)
                .stream()
                .map(CardiacTestMapper.INSTANCE::toResponse).toList();
    }

    @Override
    public List<CardiacTestResponse> getEncounterIdAndDate(Long encounterId, LocalDate date) {
        log.info("Lấy danh sách CardiacTest theo encounterId {} và ngày {}", encounterId, date);
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfDay = queryDate.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = queryDate.atTime(23, 59, 59); // 23:59:59
        return cardiacTestRepository.findByEncounterIdAndCreateDateBetween(encounterId, startOfDay, endOfDay)
                .stream()
                .map(CardiacTestMapper.INSTANCE::toResponse)
                .toList();
    }

}