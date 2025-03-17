package auth.config

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.boot.autoconfigure.domain.EntityScan

@AutoConfiguration
@EnableJpaRepositories(basePackages = ["auth.repository"])
@EntityScan(basePackages = ["auth.entity"])
@ComponentScan(basePackages = ["auth"])
@EnableConfigurationProperties(AuthProperties::class)
@ConditionalOnMissingBean(CommonConfig::class)
open class CommonConfig
