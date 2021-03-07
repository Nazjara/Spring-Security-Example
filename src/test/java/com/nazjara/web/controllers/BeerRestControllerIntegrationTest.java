package com.nazjara.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BeerRestControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void deleteBeer() throws Exception{
        mockMvc.perform(delete("/api/v1/beer/91f3101e-fe3d-4795-93fd-1ac4b6c76b95")
            .header("Api-Key", "user1")
            .header("Api-Secret", "password1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerBadCredentials() throws Exception{
        mockMvc.perform(delete("/api/v1/beer/91f3101e-fe3d-4795-93fd-1ac4b6c76b95")
                .header("Api-Key", "user10")
                .header("Api-Secret", "password10"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeers() throws Exception{
        mockMvc.perform(get("/api/v1/beer"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeersByUpc() throws Exception{
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isOk());
    }
}
