package config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "auth.jwt")
data class JwtProperties (
    val secretKey:String,
    val accessTokenExpiration:Long,
    val refreshTokenExpiration:Long,
)




