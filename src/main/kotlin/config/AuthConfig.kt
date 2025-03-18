package config

import auth.repository.UserRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class AuthConfig {
    @Bean
    fun jwtTokenProvider(jwtProperties: JwtProperties): JwtTokenProvider {
        return JwtTokenProvider(jwtProperties)
    }

    @Bean
    fun customUserDetailsService(userRepository: UserRepository): CustomUserDetailsService {
        return CustomUserDetailsService(userRepository)
    }
}
