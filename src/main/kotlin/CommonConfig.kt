package auth.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.boot.autoconfigure.domain.EntityScan

@Configuration
@EnableJpaRepositories(basePackages = ["auth.repository"])
@EntityScan(basePackages = ["auth.entity"])
@ComponentScan(basePackages = ["auth"])
@EnableConfigurationProperties(AuthProperties::class)
open class CommonConfig
