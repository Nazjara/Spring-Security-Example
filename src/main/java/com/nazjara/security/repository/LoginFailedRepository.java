package com.nazjara.security.repository;

import com.nazjara.security.domain.LoginFailed;
import com.nazjara.security.domain.User;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginFailedRepository extends JpaRepository<LoginFailed, Long> {
    List<LoginFailed> findAllByUserAndCreatedDateIsAfter(User user, Timestamp timestamp);
}
