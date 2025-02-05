package com.Initiative.app.config.websocket;

import com.Initiative.app.config.core.JwtService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class WebSocketTokenFilter implements ChannelInterceptor {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    public WebSocketTokenFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT == accessor.getCommand()) {

            String jwt = jwtService.parseJwt(accessor);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (jwt != null && auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    String username = jwtService.extractUsername(jwt);
                    UserDetails loadedUserDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    loadedUserDetails, null, loadedUserDetails.getAuthorities());
                    accessor.setUser (authentication);
                }
            }
        }
        return message;
    }
}
