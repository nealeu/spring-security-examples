package com.example.springsecurityexamples;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        SpringSecurityExamplesApplication.ControllerRequestMappedToDefault.class,
        SpringSecurityExamplesApplication.ControllerWithNoOverrides.class
})
@Import(ControllerTest.TestSecurityConfig.class)
class ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void defaultShouldGive403ForUser() throws Exception {
        var results = mockMvc.perform(
                get("/default")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userWithAuthority("ROLE_USER"))
                    .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void defaultShouldGive200ForAdmin() throws Exception {
        var results = mockMvc.perform(
                get("/default")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userWithAuthority("ROLE_ADMIN"))
                    .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void adminSlashShouldGive403ForUser() throws Exception {
        var results = mockMvc.perform(
                get("/admin/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userWithAuthority("ROLE_USER"))
                    .with(csrf()))
                .andExpect(status().isForbidden());
    }

    static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userWithAuthority(String role) {
        return user("user").authorities(new SimpleGrantedAuthority(role));
    }


    @EnableWebSecurity
    @EnableMethodSecurity(proxyTargetClass = true)
    @TestConfiguration
    public static class TestSecurityConfig {}

}