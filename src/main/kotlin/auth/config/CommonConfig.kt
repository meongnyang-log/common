package auth.config

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@AutoConfiguration
@EnableJpaRepositories(basePackages = ["auth"])
@EntityScan(basePackages = ["auth"])
@ComponentScan(basePackages = ["auth", "config"])
@EnableConfigurationProperties(AuthProperties::class)
class CommonConfig
