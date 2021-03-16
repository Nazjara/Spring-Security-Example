package com.nazjara.security.repository;

import com.nazjara.security.domain.LoginSuccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginSuccessRepository extends JpaRepository<LoginSuccess, Long> {
}
