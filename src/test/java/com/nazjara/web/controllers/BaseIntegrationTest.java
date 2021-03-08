package com.nazjara.web.controllers;

import com.nazjara.service.repository.BeerRepository;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected BeerRepository beerRepository;

    static Stream<Arguments> getStreamAdminCustomer() {
        return Stream.of(Arguments.of("user1" , "password1"));
    }

    static Stream<Arguments> getStreamAllUsers() {
        return Stream.of(Arguments.of("user1" , "password1"),
                Arguments.of("user2", "password2"),
                Arguments.of("user3", "password3"));
    }

    static Stream<Arguments> getStreamNotAdmin() {
        return Stream.of(Arguments.of("user2", "password2"),
                Arguments.of("user3", "password3"));
    }
}
