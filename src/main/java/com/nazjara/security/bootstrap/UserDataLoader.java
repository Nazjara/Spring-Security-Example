package com.nazjara.security.bootstrap;

import com.nazjara.security.domain.Authority;
import com.nazjara.security.domain.Role;
import com.nazjara.security.domain.User;
import com.nazjara.security.repository.AuthorityRepository;
import com.nazjara.security.repository.RoleRepository;
import com.nazjara.security.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (authorityRepository.count() == 0) {
            loadData();
        }
    }

    private void loadData() {
        var authority1 = authorityRepository.save(Authority.builder()
                .permission("beer.create")
                .build());
        var authority2 = authorityRepository.save(Authority.builder()
                .permission("beer.delete")
                .build());
        var authority3 = authorityRepository.save(Authority.builder()
                .permission("beer.read")
                .build());
        var authority4 = authorityRepository.save(Authority.builder()
                .permission("beer.update")
                .build());
        var authority5 = authorityRepository.save(Authority.builder()
                .permission("customer.create")
                .build());
        var authority6 = authorityRepository.save(Authority.builder()
                .permission("customer.delete")
                .build());
        var authority7 = authorityRepository.save(Authority.builder()
                .permission("customer.read")
                .build());
        var authority8 = authorityRepository.save(Authority.builder()
                .permission("customer.update")
                .build());
        var authority9 = authorityRepository.save(Authority.builder()
                .permission("brewery.create")
                .build());
        var authority10 = authorityRepository.save(Authority.builder()
                .permission("brewery.delete")
                .build());
        var authority11 = authorityRepository.save(Authority.builder()
                .permission("brewery.read")
                .build());
        var authority12 = authorityRepository.save(Authority.builder()
                .permission("brewery.update")
                .build());

        var role1 = roleRepository.save(Role.builder()
                .name("ADMIN")
                .build());
        var role2 = roleRepository.save(Role.builder()
                .name("USER")
                .build());
        var role3 = roleRepository.save(Role.builder()
                .name("CUSTOMER")
                .build());
        
        role1.setAuthorities(Set.of(authority1, authority2, authority3, authority4, authority5, authority6, authority7,
                        authority8, authority9, authority10, authority11, authority12));
        role2.setAuthorities(Set.of(authority3));
        role3.setAuthorities(Set.of(authority3, authority7, authority11));

        roleRepository.saveAll(Set.of(role1, role2, role3));

        var user1 = userRepository.save(User.builder()
                .username("user1")
                .password(passwordEncoder.encode("password1"))
                .build());

        var user2 = userRepository.save(User.builder()
                .username("user2")
                .password(passwordEncoder.encode("password2"))
                .build());

        var user3 = userRepository.save(User.builder()
                .username("user3")
                .password(passwordEncoder.encode("password3"))
                .build());

        user1.setRoles(Set.of(role1));
        user2.setRoles(Set.of(role2));
        user3.setRoles(Set.of(role3));

        userRepository.saveAll(Set.of(user1, user2, user3));
    }
}
