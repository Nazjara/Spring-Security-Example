package com.nazjara.security.bootstrap;

import com.nazjara.security.domain.Authority;
import com.nazjara.security.domain.User;
import com.nazjara.security.repository.AuthorityRepository;
import com.nazjara.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (authorityRepository.count() == 0) {
            loadAuthorityData();
        }

        if (userRepository.count() == 0) {
            loadUserData();
        }
    }

    private void loadAuthorityData() {
        var authority1 = Authority.builder()
                .role("ROLE_ADMIN")
                .build();

        var authority2 = Authority.builder()
                .role("ROLE_USER")
                .build();

        var authority3 = Authority.builder()
                .role("ROLE_CUSTOMER")
                .build();

        authorityRepository.save(authority1);
        authorityRepository.save(authority2);
        authorityRepository.save(authority3);
    }

    private void loadUserData() {
        var user1 = User.builder()
                .username("user1")
                .password(passwordEncoder.encode("password1"))
                .authority(authorityRepository.getOne(1L))
                .build();

        var user2 = User.builder()
                .username("user2")
                .password(passwordEncoder.encode("password2"))
                .authority(authorityRepository.getOne(2L))
                .build();

        var user3 = User.builder()
                .username("user3")
                .password(passwordEncoder.encode("password3"))
                .authority(authorityRepository.getOne(3L))
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
}
