package com.nazjara.security.google;

import com.nazjara.security.domain.User;
import com.nazjara.security.repository.UserRepository;
import com.warrenstrange.googleauth.ICredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleCredentialsRepository implements ICredentialRepository {

    private final UserRepository userRepository;

    @Override
    public String getSecretKey(String username) {
        User user = userRepository.getByUsername(username).orElseThrow();

        return user.getGoogle2faSecret();
    }

    @Override
    public void saveUserCredentials(String username, String secretKey, int validationCode, List<Integer> scratchCodes) {
        User user = userRepository.getByUsername(username).orElseThrow();
        user.setGoogle2faSecret(secretKey);
        user.setUseGoogle2fa(true);
        userRepository.save(user);
    }
}
