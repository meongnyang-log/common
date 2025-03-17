package config


import auth.repository.UserRepository
import error.CustomException
import error.ErrorCode
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findById(username.toLong())
            .orElseThrow { CustomException(ErrorCode.USER_NOT_FOUND) }

        return CustomUserDetails(user)
    }
}
