package vn.edu.hcmute.utecare.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.model.Account;
import vn.edu.hcmute.utecare.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Account account = accountRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                account.getUsername(),
                account.getPassword(),
                account.isEnabled(),
                account.isAccountNonExpired(),
                account.isCredentialsNonExpired(),
                account.isAccountNonLocked(),
                account.getAuthorities()
        );
    }
}
