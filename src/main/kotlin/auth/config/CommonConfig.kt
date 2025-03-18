package auth.config

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import config.CustomUserDetailsService
import auth.repository.UserRepository

@AutoConfiguration
@EnableJpaRepositories(basePackages = ["auth"])
@EntityScan(basePackages = ["auth"])
@ComponentScan(basePackages = ["auth", "config"])
@EnableConfigurationProperties(AuthProperties::class)
class CommonConfig {

    @Bean
    fun customUserDetailsService(userRepository: UserRepository): CustomUserDetailsService {
        return CustomUserDetailsService(userRepository)
    }

//    @Bean
//    fun jwtTokenProvider(authProperties: AuthProperties): JwtTokenProvider {
//        return JwtTokenProvider(JwtProperties(
//            secretKey = authProperties.jwt.secretKey,
//            accessTokenExpiration = authProperties.jwt.accessTokenExpiration,
//            refreshTokenExpiration = authProperties.jwt.refreshTokenExpiration
//        ))
//    }
}
