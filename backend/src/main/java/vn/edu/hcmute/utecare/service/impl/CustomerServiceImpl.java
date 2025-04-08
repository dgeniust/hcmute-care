package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.CustomerCreationRequest;
import vn.edu.hcmute.utecare.dto.request.CustomerRequest;
import vn.edu.hcmute.utecare.dto.response.CustomerResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.AccountMapper;
import vn.edu.hcmute.utecare.mapper.CustomerMapper;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.model.Customer;
import vn.edu.hcmute.utecare.repository.AccountRepository;
import vn.edu.hcmute.utecare.repository.CustomerRepository;
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

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CustomerResponse createCustomer(CustomerCreationRequest request) {
        log.info("Creating customer with request: {}", request);

        if (customerRepository.existsByPhone(request.getCustomerRequest().getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        Customer customer = CustomerMapper.INSTANCE.toEntity(request.getCustomerRequest());
        Customer savedCustomer = customerRepository.saveAndFlush(customer);

        Account account = AccountMapper.INSTANCE.toEntity(request.getAccountRequest());
        account.setPassword(passwordEncoder.encode(request.getAccountRequest().getPassword()));
        account.setUser(savedCustomer);
        account.setRole(Role.CUSTOMER);
        account.setStatus(AccountStatus.ACTIVE);

        accountRepository.save(account);

        log.info("Customer created successfully with ID: {}", savedCustomer.getId());
        return CustomerMapper.INSTANCE.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        log.info("Getting customer by id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        log.info("Successfully retrieved customer with id: {}", id);
        return CustomerMapper.INSTANCE.toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        log.info("Updating customer with id: {} and request: {}", id, request);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        if (!customer.getPhone().equals(request.getPhone()) && customerRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        CustomerMapper.INSTANCE.updateEntity(request, customer);
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Successfully updated customer with id: {}", id);
        return CustomerMapper.INSTANCE.toResponse(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        Account account = accountRepository.findByUser_Id(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for customer with ID: " + id));
        accountRepository.delete(account);
        customerRepository.delete(customer);
        log.info("Successfully deleted customer with id: {}", id);
    }

    @Override
    public PageResponse<CustomerResponse> getAllCustomers(int page, int size, String sort, String direction) {
        log.info("Fetching all customers with pagination: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);

        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return PageResponse.<CustomerResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(customerPage.getTotalPages())
                .totalElements(customerPage.getTotalElements())
                .content(customerPage.getContent().stream().map(CustomerMapper.INSTANCE::toResponse).toList())
                .build();
    }

    @Override
    public PageResponse<CustomerResponse> searchCustomers(String keyword, Membership membership, int page, int size, String sort, String direction) {
        log.info("Searching customers with keyword: {}, page={}, size={}, sort={}, direction={}", keyword, page, size, sort, direction);
        Pageable pageable = PaginationUtil.createPageable(page, size, sort, direction);
        Page<Customer> customerPage = customerRepository.searchCustomers(keyword, membership, pageable);
        return PageResponse.<CustomerResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(customerPage.getTotalPages())
                .totalElements(customerPage.getTotalElements())
                .content(customerPage.getContent().stream().map(CustomerMapper.INSTANCE::toResponse).toList())
                .build();
    }
}