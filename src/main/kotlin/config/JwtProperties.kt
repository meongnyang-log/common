package config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auth.jwt")
data class JwtProperties(
    val secretKey: String = "",
    val accessTokenExpiration: Long = 3600000L,
    val refreshTokenExpiration: Long = 86400000L
)
