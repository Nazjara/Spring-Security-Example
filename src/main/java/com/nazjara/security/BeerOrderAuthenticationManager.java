package com.nazjara.security;

import com.nazjara.security.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class BeerOrderAuthenticationManager {

    public boolean customerIdMatches(Authentication authentication, UUID id) {
        User authenticatedUser = (User) authentication.getPrincipal();
        log.debug("Auth User Customer Id: " + authenticatedUser.getCustomer().getId() + " Customer Id: " + id);

        return authenticatedUser.getCustomer().getId().equals(id);
    }
}
