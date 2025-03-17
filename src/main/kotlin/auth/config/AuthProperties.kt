package auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "auth")
data class AuthProperties(
    val jwt: JwtConfig = JwtConfig(),
    val oauth2: OAuth2Config = OAuth2Config()
)

data class JwtConfig(
    val secretKey: String = "",
    val accessTokenExpiration: Long = 3600000L,  // 1시간
    val refreshTokenExpiration: Long = 86400000L // 24시간
)

data class OAuth2Config(
    val kakao: ProviderConfig = ProviderConfig(),
    val google: ProviderConfig = ProviderConfig(),
    val naver: ProviderConfig = ProviderConfig()
)

data class ProviderConfig(
    val clientId: String = "",
    val clientSecret: String = "",
    val redirectUri: String = ""
) 