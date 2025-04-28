package vn.edu.hcmute.utecare.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.utecare.service.JwtService;
import vn.edu.hcmute.utecare.util.enumeration.TokenType;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authToken = accessor.getFirstNativeHeader("Authorization");
            if (authToken == null || authToken.trim().isEmpty() || !authToken.startsWith("Bearer ")) {
                log.error("Missing or invalid Authorization header");
                return null;
            }
            String token = authToken.substring(7);
            try {
                String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
                UserDetails user = userDetailsService.loadUserByUsername(username);
                if (!jwtService.isValid(token, TokenType.ACCESS_TOKEN, user)) {
                    log.error("Token not valid");
                    return null;
                }
                accessor.setUser(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
            } catch (Exception e) {
                log.error("Authentication failed: {}", e.getMessage());
                return null;
            }
        }
        return message;
    }
}
