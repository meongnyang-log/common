package auth

import auth.config.AuthProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@AutoConfiguration
@EnableJpaRepositories(basePackages = ["auth"])
@EntityScan(basePackages = ["auth"])
@ComponentScan(basePackages = ["auth"])
@EnableConfigurationProperties(AuthProperties::class)
class CommonConfig
