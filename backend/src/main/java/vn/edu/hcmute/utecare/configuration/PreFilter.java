package vn.edu.hcmute.utecare.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.access.AccessDeniedException;
import vn.edu.hcmute.utecare.exception.ErrorResponse;
import vn.edu.hcmute.utecare.service.JwtService;
import vn.edu.hcmute.utecare.service.RedisService;
import vn.edu.hcmute.utecare.util.enumeration.TokenType;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class PreFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RedisService redisService;
    private static final String AUTHORIZATION = "Authorization";
    private static final String AT_BLACKLIST_PREFIX = "bl_at:";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("{} {}", request.getMethod(), request.getRequestURI());

        final String authHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasLength(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("token: {}...", token.substring(0, 15));
            String username = "";
            try {
                username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
                log.info("username: {}", username);

                // Kiểm tra token hết hạn
                if (jwtService.isTokenExpired(token, TokenType.ACCESS_TOKEN)) {
                    throw new AccessDeniedException("Token has expired");
                }

                // Kiểm tra token trong blacklist
                String blacklistedToken = (String) redisService.get(AT_BLACKLIST_PREFIX + username);
                if (blacklistedToken != null && blacklistedToken.equals(token)) {
                    throw new AccessDeniedException("Token has been revoked");
                }

            } catch (MalformedJwtException e) {
                log.error("Malformed JWT token: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(errorResponse(request.getRequestURI(), "Invalid or malformed JWT token"));
                return;
            } catch (AccessDeniedException e) {
                log.info(e.getMessage());
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(errorResponse(request.getRequestURI(), e.getMessage()));
                return;
            }

            UserDetails user = userDetailsService.loadUserByUsername(username);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);

            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private String errorResponse(String url, String message) {
        try {
            ErrorResponse error = new ErrorResponse();
            error.setTimestamp(new Date());
            error.setStatus(HttpServletResponse.SC_FORBIDDEN);
            error.setPath(url);
            error.setError("Forbidden");
            error.setMessage(message);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(error);
        } catch (Exception e) {
            return "";
        }
    }
}