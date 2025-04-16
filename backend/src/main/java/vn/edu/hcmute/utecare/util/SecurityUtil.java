package vn.edu.hcmute.utecare.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.hcmute.utecare.model.Account;

public class SecurityUtil {
    private SecurityUtil() {
    }
    public static Account getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken || authentication == null) {
            throw new AccessDeniedException("You are not an anonymous user");
        }

        return (Account) authentication.getPrincipal();
    }
}
