package backend.tangsquad.chat.config;

import backend.tangsquad.auth.jwt.JwtTokenProvider;
import backend.tangsquad.auth.jwt.UserDetailsServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public WebSocketConfig(JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat").setAllowedOrigins("https://jiangxy.github.io")
                .withSockJS()
                .setSessionCookieNeeded(false);
        registry.addEndpoint("/ws/chat").setAllowedOrigins("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new WebSocketJwtInterceptor(jwtTokenProvider, userDetailsServiceImpl));
    }
}
