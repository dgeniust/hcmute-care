package vn.edu.hcmute.utecare.service;

import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.utecare.dto.request.CustomerCreationRequest;
import vn.edu.hcmute.utecare.dto.request.CustomerRequest;
import vn.edu.hcmute.utecare.dto.response.CustomerResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.util.enumeration.Membership;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerCreationRequest request);

    CustomerResponse getCustomerById(Long id);

    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    void deleteCustomer(Long id);

    PageResponse<CustomerResponse> getAllCustomers(int page, int size, String sort, String direction);

    PageResponse<CustomerResponse> searchCustomers(String keyword, Membership membership, int page, int size, String sort, String direction);
}