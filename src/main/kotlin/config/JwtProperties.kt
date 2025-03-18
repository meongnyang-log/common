package config


data class JwtProperties(
    val secretKey: String = "xVskKxwYJMHg86EA12MeFnJEv2N8d23Q4c5SeufBbk=SxVskKxwYJMHg86EA12MeFnJEv2N8d23Q4c5SeufBbk=",
    val accessTokenExpiration: Long = 3600000L,
    val refreshTokenExpiration: Long = 86400000L
)
