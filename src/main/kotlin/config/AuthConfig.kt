package config

import auth.config.AuthProperties
import auth.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfig {
    @Bean
    fun jwtTokenProvider(authProperties: AuthProperties): JwtTokenProvider {
        return JwtTokenProvider(
            JwtProperties(
                secretKey = authProperties.jwt.secretKey,
                accessTokenExpiration = authProperties.jwt.accessTokenExpiration,
                refreshTokenExpiration = authProperties.jwt.refreshTokenExpiration
            )
        )
    }

    @Bean
    fun customUserDetailsService(userRepository: UserRepository): CustomUserDetailsService {
        return CustomUserDetailsService(userRepository)
    }
}
