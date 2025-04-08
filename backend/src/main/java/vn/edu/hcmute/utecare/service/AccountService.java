package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.AccountStatusUpdateRequest;
import vn.edu.hcmute.utecare.dto.response.AccountResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.util.enumeration.AccountStatus;
import vn.edu.hcmute.utecare.util.enumeration.Role;

public interface AccountService {
    AccountResponse getAccountById(Long id);

    PageResponse<AccountResponse> getAllAccounts(int page, int size, String sort, String direction);

    AccountResponse updateAccountStatus(Long id, AccountStatusUpdateRequest request);

    PageResponse<AccountResponse> searchAccounts(String keyword, Role role, AccountStatus status, int page, int size, String sort, String direction);
}
