package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.CustomerRequest;
import vn.edu.hcmute.utecare.dto.response.CustomerResponse;
import vn.edu.hcmute.utecare.dto.response.MedicalRecordResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.CustomerMapper;
import vn.edu.hcmute.utecare.mapper.MedicalRecordMapper;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.model.Customer;
import vn.edu.hcmute.utecare.model.MedicalRecord;
import vn.edu.hcmute.utecare.repository.AccountRepository;
import vn.edu.hcmute.utecare.repository.CustomerRepository;
import vn.edu.hcmute.utecare.repository.MedicalRecordRepository;
import vn.edu.hcmute.utecare.service.CustomerService;
import vn.edu.hcmute.utecare.util.enumeration.AccountStatus;
import vn.edu.hcmute.utecare.util.enumeration.Membership;
import vn.edu.hcmute.utecare.util.PaginationUtil;
import vn.edu.hcmute.utecare.util.enumeration.Role;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CustomerResponse createCustomer(CustomerRequest request) {
        log.info("Tạo khách hàng mới với thông tin: {}", request);

        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        Customer customer = customerMapper.toEntity(request);

        Account account = Account.builder()
                .password(passwordEncoder.encode(request.getPhone()))
                .user(customer)
                .role(Role.CUSTOMER)
                .status(AccountStatus.ACTIVE)
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        account.setUser(savedCustomer);
        accountRepository.save(account);

        log.info("Tạo khách hàng thành công với ID: {}", savedCustomer.getId());
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        log.info("Truy xuất khách hàng với ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id));
        log.info("Truy xuất khách hàng thành công với ID: {}", id);
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        log.info("Cập nhật khách hàng với ID: {} và thông tin: {}", id, request);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id));

        if (!customer.getPhone().equals(request.getPhone()) && customerRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        customerMapper.updateEntity(request, customer);
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Cập nhật khách hàng thành công với ID: {}", id);
        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        log.info("Xóa khách hàng với ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id));
        Account account = accountRepository.findByUser_Id(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản cho khách hàng với ID: " + id));
        accountRepository.delete(account);
        customerRepository.delete(customer);
        log.info("Xóa khách hàng thành công với ID: {}", id);
    }

    @Override
    public PageResponse<CustomerResponse> getAllCustomers(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách khách hàng: trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return PageResponse.<CustomerResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(customerPage.getTotalPages())
                .totalElements(customerPage.getTotalElements())
                .content(customerPage.getContent().stream().map(customerMapper::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<CustomerResponse> searchCustomers(String keyword, Membership membership, int page, int size, String sort, String direction) {
        log.info("Tìm kiếm khách hàng với từ khóa: {}, cấp thành viên: {}, trang={}, kích thước={}, sắp xếp={}, hướng={}",
                keyword, membership, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Customer> customerPage = customerRepository.searchCustomers(keyword, membership, pageable);
        return PageResponse.<CustomerResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(customerPage.getTotalPages())
                .totalElements(customerPage.getTotalElements())
                .content(customerPage.getContent().stream().map(customerMapper::toResponse).toList())
                .build();
    }


}