package config


data class JwtProperties (
    val secretKey:String,
    val accessTokenExpiration:Long,
    val refreshTokenExpiration:Long,
)




