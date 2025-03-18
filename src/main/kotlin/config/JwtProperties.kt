package config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auth.jwt")
data class JwtProperties(
    val secretKey: String = "xVskKxwYJMHg86EA12MeFnJEv2N8d23Q4c5SeufBbk=SxVskKxwYJMHg86EA12MeFnJEv2N8d23Q4c5SeufBbk=",
    val accessTokenExpiration: Long = 36000000L,
    val refreshTokenExpiration: Long = 864000000L
)
