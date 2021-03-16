package com.nazjara.security.listeners;

import com.nazjara.security.domain.LoginFailed;
import com.nazjara.security.domain.LoginSuccess;
import com.nazjara.security.domain.User;
import com.nazjara.security.repository.LoginFailedRepository;
import com.nazjara.security.repository.LoginSuccessRepository;
import com.nazjara.security.repository.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationEventListener {

    private final LoginSuccessRepository loginSuccessRepository;
    private final LoginFailedRepository loginFailedRepository;
    private final UserRepository userRepository;

    @EventListener
    public void listen(AuthenticationSuccessEvent event) {
        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            var builder = LoginSuccess.builder();

            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();

            if (token.getPrincipal() instanceof User) {
                User user = (User) token.getPrincipal();
                builder.user(user);
                log.debug("User logged in: " + user.getUsername());
            }

            if (token.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
                builder.sourceId(details.getRemoteAddress());
                log.debug("Source IP: " + details.getRemoteAddress());
            }

            log.debug("LoginSuccess saved. Id: " + loginSuccessRepository.save(builder.build()));
        }
    }

    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event) {
        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            var builder = LoginFailed.builder();

            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();

            if (token.getPrincipal() instanceof String) {
                userRepository.getByUsername((String) token.getPrincipal()).ifPresent(builder::user);
                builder.username((String) token.getPrincipal());
                log.debug("User login failed: " + token.getPrincipal());
            }

            if (token.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
                builder.sourceId(details.getRemoteAddress());
                log.debug("Source IP: " + details.getRemoteAddress());
            }

            var loginFailed = loginFailedRepository.save(builder.build());
            log.debug("LoginFailed saved. Id: " + loginFailed);

            if (loginFailed.getUser() != null) {
                lockUserAccount(loginFailed.getUser());
            }
        }
    }

    private void lockUserAccount(User user) {
        var failures = loginFailedRepository.findAllByUserAndCreatedDateIsAfter(user,
                Timestamp.valueOf(LocalDateTime.now().minus(1, ChronoUnit.DAYS)));

        if (failures.size() > 3) {
            log.debug("Locking user account...");
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }
    }
}
