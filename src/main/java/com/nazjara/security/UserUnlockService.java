package com.nazjara.security;

import com.nazjara.security.repository.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserUnlockService {

    private final UserRepository userRepository;

    @Scheduled(fixedRate = 10000)
    public void unlockAccounts() {
        log.debug("Running unlock accounts");

        var lockedUsers = userRepository.findAllByAccountNonLockedAndLastModifiedDateIsBefore(false,
                        Timestamp.valueOf(LocalDateTime.now().minusSeconds(30)));

        if (!lockedUsers.isEmpty()) {
            log.debug("Locked accounts found");
            lockedUsers.forEach( user -> user.setAccountNonLocked(true));
            userRepository.saveAll(lockedUsers);
        }
    }
}
