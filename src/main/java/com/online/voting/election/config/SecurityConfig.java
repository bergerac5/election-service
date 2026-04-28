package com.online.voting.election.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.online.voting.election.security.JwtFilter;

@Configuration
public class SecurityConfig {

        private final JwtFilter jwtFilter;

        public SecurityConfig(JwtFilter jwtFilter) {
                this.jwtFilter = jwtFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                // public endpoints
                                                .requestMatchers("/actuator/**", "/elections/health",
                                                                "/elections/{electionId}", "/elections/bulk",
                                                                "/positions/{positionId}", "/positions/bulk")
                                                .permitAll()

                                                // ADMIN only on position management endpoints
                                                .requestMatchers(
                                                                "positions/createPosition",
                                                                "/updatePosition/{positionId}",
                                                                "/deletePosition/{positionId}")
                                                .hasRole("ADMIN")

                                                // ADMIN only on election management endpoints
                                                .requestMatchers(
                                                                "/elections/createPosition",
                                                                "/elections/updateElection/{electionId}",
                                                                "/elections/deleteElection/{electionId}",
                                                                "/elections/{electionId}/status")
                                                .hasRole("ADMIN")

                                                // VOTER and ADMIN
                                                .requestMatchers("/position-candidates/**")
                                                .hasAnyRole("VOTER", "ADMIN", "CANDIDATE")

                                                // fallback
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
