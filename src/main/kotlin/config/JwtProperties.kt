package config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("jwt")
data class JwtProperties(
    var secretKey: String,
    var accessTokenExpiration: Long,
    var refreshTokenExpiration: Long
)
