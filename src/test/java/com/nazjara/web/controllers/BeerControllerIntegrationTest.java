package com.nazjara.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BeerControllerIntegrationTest extends BaseIntegrationTest {

    @WithMockUser
    @Test
    void findBeers() throws Exception{
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeersWithHttpBasic() throws Exception{
        mockMvc.perform(get("/beers/find").with(httpBasic("user1", "password1")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void getRootWithoutAuthentication() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void getFindBeersWithoutAuthentication() throws Exception{
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isUnauthorized());
    }
}
