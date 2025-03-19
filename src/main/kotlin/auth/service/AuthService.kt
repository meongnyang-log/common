package auth.service

import auth.dto.AuthProvider
import auth.dto.TokenResponse
import auth.entity.User
import auth.repository.UserRepository
import config.JwtTokenProvider
import error.CustomException
import error.ErrorCode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.ResourceAccessException

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val restTemplate = RestTemplate()

    fun authenticateWithOAuth2(accessToken: String, provider: String): TokenResponse {
        if (accessToken.isBlank()) {
            throw CustomException(ErrorCode.INVALID_TOKEN)
        }

        val userInfo = try {
            when (provider.uppercase()) {
                "KAKAO" -> getKakaoUserInfo(accessToken)
                "GOOGLE" -> getGoogleUserInfo(accessToken)
                "NAVER" -> getNaverUserInfo(accessToken)
                else -> throw CustomException(ErrorCode.INVALID_PROVIDER)
            }
        } catch (e: Exception) {
            log.error("OAuth2 authentication failed for provider $provider: ${e.message}", e)
            throw when (e) {
                is HttpClientErrorException.Unauthorized -> CustomException(ErrorCode.OAUTH2_TOKEN_EXPIRED)
                is HttpClientErrorException.Forbidden -> CustomException(ErrorCode.OAUTH2_INVALID_TOKEN)
                is ResourceAccessException -> CustomException(ErrorCode.OAUTH2_NETWORK_ERROR)
                is CustomException -> e
                else -> CustomException(ErrorCode.OAUTH2_PROCESSING_ERROR)
            }
        }

        val user = userRepository.findByUserEmail(userInfo.email)
            ?: saveUser(userInfo.email, AuthProvider.valueOf(provider.uppercase()))

        return TokenResponse(
            accessToken = jwtTokenProvider.createAccessToken(user.id),
            refreshToken = jwtTokenProvider.createRefreshToken(user.id)
        )
    }

    private fun getKakaoUserInfo(accessToken: String): OAuthUserInfo {
        try {
            val headers = HttpHeaders()
            headers.set("Authorization", "Bearer $accessToken")
            val entity = HttpEntity<String>(headers)

            val response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                Map::class.java
            )

            val body = response.body ?: throw CustomException(ErrorCode.OAUTH2_USER_INFO_MISSING)
            val kakaoAccount = body["kakao_account"] as? Map<*, *>
                ?: throw CustomException(ErrorCode.OAUTH2_USER_INFO_MISSING)
            val email = kakaoAccount["email"] as? String
                ?: throw CustomException(ErrorCode.OAUTH2_USER_INFO_MISSING)

            return OAuthUserInfo(email)
        } catch (e: Exception) {
            log.error("Failed to get Kakao user info: ${e.message}", e)
            throw when (e) {
                is CustomException -> e
                is HttpClientErrorException -> handleOAuthError(e)
                else -> CustomException(ErrorCode.OAUTH2_PROCESSING_ERROR)
            }
        }
    }

    private fun getGoogleUserInfo(accessToken: String): OAuthUserInfo {
        try {
            val headers = HttpHeaders()
            headers.set("Authorization", "Bearer $accessToken")
            val entity = HttpEntity<String>(headers)

            val response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                entity,
                Map::class.java
            )

            val body = response.body ?: throw CustomException(ErrorCode.OAUTH2_USER_INFO_MISSING)
            val email = body["email"] as? String
                ?: throw CustomException(ErrorCode.OAUTH2_USER_INFO_MISSING)

            return OAuthUserInfo(email)
        } catch (e: Exception) {
            log.error("Failed to get Google user info: ${e.message}", e)
            throw when (e) {
                is CustomException -> e
                is HttpClientErrorException -> handleOAuthError(e)
                else -> CustomException(ErrorCode.OAUTH2_PROCESSING_ERROR)
            }
        }
    }

    private fun getNaverUserInfo(accessToken: String): OAuthUserInfo {
        try {
            val headers = HttpHeaders()
            headers.set("Authorization", "Bearer $accessToken")
            val entity = HttpEntity<String>(headers)

            val response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                entity,
                Map::class.java
            )

            val body = response.body ?: throw CustomException(ErrorCode.OAUTH2_USER_INFO_MISSING)
            val responseData = body["response"] as? Map<*, *>
                ?: throw CustomException(ErrorCode.OAUTH2_USER_INFO_MISSING)
            val email = responseData["email"] as? String
                ?: throw CustomException(ErrorCode.OAUTH2_USER_INFO_MISSING)

            return OAuthUserInfo(email)
        } catch (e: Exception) {
            log.error("Failed to get Naver user info: ${e.message}", e)
            throw when (e) {
                is CustomException -> e
                is HttpClientErrorException -> handleOAuthError(e)
                else -> CustomException(ErrorCode.OAUTH2_PROCESSING_ERROR)
            }
        }
    }

    private fun handleOAuthError(e: HttpClientErrorException): CustomException {
        return when (e) {
            is HttpClientErrorException.Unauthorized -> CustomException(ErrorCode.OAUTH2_TOKEN_EXPIRED)
            is HttpClientErrorException.Forbidden -> CustomException(ErrorCode.OAUTH2_INVALID_TOKEN)
            else -> CustomException(ErrorCode.OAUTH2_PROCESSING_ERROR)
        }
    }

    fun refreshToken(refreshToken: String): TokenResponse {
        if (refreshToken.isBlank()) {
            throw CustomException(ErrorCode.INVALID_REFRESH_TOKEN)
        }

        val userId = try {
            jwtTokenProvider.validateRefreshToken(refreshToken)
        } catch (e: Exception) {
            log.error("Failed to validate refresh token: ${e.message}", e)
            throw CustomException(ErrorCode.INVALID_REFRESH_TOKEN)
        }

        return TokenResponse(
            accessToken = jwtTokenProvider.createAccessToken(userId),
            refreshToken = jwtTokenProvider.createRefreshToken(userId)
        )
    }

    private fun saveUser(email: String, provider: AuthProvider): User {
        val user = User(
            userEmail = email,
            userPassword = "",
            provider = provider,
        )

        return userRepository.save(user)
    }
}

data class OAuthUserInfo(
    val email: String
)
