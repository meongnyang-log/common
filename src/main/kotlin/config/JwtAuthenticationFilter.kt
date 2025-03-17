package config

import error.CustomException
import error.ErrorCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = extractToken(request)
            if (!token.isNullOrBlank()) {
                processToken(token)
            }
        } catch (e: CustomException) {
            log.error("Authentication error: ${e.message}", e)
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
            return
        } catch (e: Exception) {
            log.error("Unexpected authentication error: ${e.message}", e)
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "인증 처리 중 오류가 발생했습니다")
            return
        }
        
        filterChain.doFilter(request, response)
    }

    private fun processToken(token: String) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw CustomException(ErrorCode.INVALID_TOKEN)
        }

        val userId = jwtTokenProvider.getUserId(token)
        val userDetails = userDetailsService.loadUserByUsername(userId.toString()) as CustomUserDetails
        
        val authentication = UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.authorities
        )
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return when {
            bearerToken.isNullOrBlank() -> null
            !bearerToken.startsWith("Bearer ") -> throw CustomException(ErrorCode.INVALID_TOKEN)
            else -> bearerToken.substring(7)
        }
    }
}
