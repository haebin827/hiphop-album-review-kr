package io.github.haebin827.hiphopreview.kr.config;

import io.github.haebin827.hiphopreview.kr.util.AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    // 유저가 로그인할 시 바로 직전의 링크로 리다이렉트하는 핸들러
    private final AuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        // 관리자(admin) 권한 필요
                        .requestMatchers("/admin/**", "/announce/new", "/announce/edit/**", "/announce/delete/**").hasRole("ADMIN")

                        // 사용자(user) & 관리자(admin) 권한 필요
                        .requestMatchers("/support/album-request", "/support/artist-request", "/support/feedback", "/profile/**", "/album/review/new").hasAnyRole("ADMIN", "USER")

                        // 인증된 사용자만 접근 가능
                        .requestMatchers("/auth/findInfo").authenticated()

                        // 로그인하지 않은 사용자도 접근 가능한 URL
                        .requestMatchers("/auth/login", "/").permitAll()

                        // 그 외 모든 요청
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                (request, response, authException) -> {
                                    // /auth/findInfo에 대한 403 Forbidden
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.getWriter().write("Access Denied: You are not allowed to access this resource.");
                                },
                                new OrRequestMatcher(
                                        new AntPathRequestMatcher("/auth/findInfo"),
                                        new AntPathRequestMatcher("/auth/emailConfirm"),
                                        new AntPathRequestMatcher("/auth/registerConfirmed")
                                )
                        )
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/auth/login"), // 로그인 페이지로 리다이렉트
                                new OrRequestMatcher(
                                        new AntPathRequestMatcher("/admin/**"),
                                        new AntPathRequestMatcher("/support/album-request"),
                                        new AntPathRequestMatcher("/support/artist-request"),
                                        new AntPathRequestMatcher("/support/feedback"),
                                        new AntPathRequestMatcher("/profile/**")
                                )
                        )
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .successHandler(successHandler) // 커스텀 핸들러 사용
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .anonymous(anonymous -> anonymous
                        .principal(new org.springframework.security.core.userdetails.User(
                                "guestUser", "", Collections.emptyList())) // UserDetails로 설정, 익명 사용자 이름 변경
                        .authorities("ROLE_GUEST") // 익명 사용자 권한
                )
                .sessionManagement(session -> session
                        .sessionFixation(sessionFixation -> sessionFixation.migrateSession()) // 세션 고정 공격 방지 설정
                        .maximumSessions(1) // 단일 세션 허용
                        .maxSessionsPreventsLogin(false) // 기존 세션 만료 없이 새로운 로그인 차단 => 로그아웃 후 세션이 올바르게 해제되지 않으면, 동일한 세션 충돌로 인해 로그인 오류가 발생
                )
                /*.rememberMe(remember -> remember
                        .key("uniqueAndSecret")
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(7 * 24 * 60 * 60)
                )*/;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}