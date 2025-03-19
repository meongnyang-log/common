package config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
// 보안 설정을 위한 추상 클래스 정의
abstract class AbstractSecurityConfig {

    // SecurityFilterChain Bean을 설정하는 메서드
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // JWT 인증 필터 생성
        val jwtFilter = JwtAuthenticationFilter(jwtTokenProvider(), customUserDetailsService())

        http
            // CSRF 보호를 비활성화 (REST API에서는 CSRF를 사용하지 않음)
            .csrf { csrf -> csrf.disable() }
            // 세션 관리 정책을 Stateless로 설정 (서버가 세션을 저장하지 않음)
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            // HTTP 요청에 대한 접근 제어를 설정
            .authorizeHttpRequests { auth ->
                configure(auth) // 자식 클래스에서 설정한 접근 제어 규칙을 적용
            }
            // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가하여 JWT 토큰을 처리
            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )

        // 설정한 HTTP 보안 규칙을 기반으로 SecurityFilterChain을 반환
        return http.build()
    }

    // 자식 클래스에서 구현해야 할 추상 메서드: HTTP 요청에 대한 접근 제어 규칙을 설정하는 메서드
    protected abstract fun configure(auth: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry)

    // 자식 클래스에서 구현해야 할 추상 메서드: JWT 토큰을 처리하는 JwtTokenProvider를 반환하는 메서드
    protected abstract fun jwtTokenProvider(): JwtTokenProvider

    // 자식 클래스에서 구현해야 할 추상 메서드: 사용자 정보를 처리하는 CustomUserDetailsService를 반환하는 메서드
    protected abstract fun customUserDetailsService(): CustomUserDetailsService
}
