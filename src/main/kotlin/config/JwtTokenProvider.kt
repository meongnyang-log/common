package config

import error.CustomException
import error.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.Date

class JwtTokenProvider(private val jwtProperties: JwtProperties) {
    private val key = Keys.hmacShaKeyFor(jwtProperties.secretKey.toByteArray())

    /**
     * Access Token
     */
    fun createAccessToken(userId: Long): String {
        return createToken(userId, jwtProperties.accessTokenExpiration)
    }

    /**
     * Refresh Token
     */
    fun createRefreshToken(userId: Long): String {
        return createToken(userId, jwtProperties.refreshTokenExpiration)
    }

    /**
     * 생성 및 서명
     */
    private fun createToken(userId: Long, expiration: Long): String {
        val claims: Claims = Jwts.claims()
        claims["userId"] = userId

        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun validateRefreshToken(refreshToken: String): Long {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .body

            (claims["userId"] as Number).toLong()
        } catch (e: Exception) {
            throw CustomException(ErrorCode.INVALID_TOKEN)
        }
    }

    /**
     * 토큰에서 userId 추출하기
     */
    fun getUserId(token: String): Long {
        return (Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body["userId"] as Number).toLong()
    }

    /**
     * 토큰 유효성 검증하기
     */
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            println("Token validation failed: ${e.message}")
            throw CustomException(ErrorCode.INVALID_TOKEN)
        }
    }

}
