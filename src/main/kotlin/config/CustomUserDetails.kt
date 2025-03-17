package config

import auth.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class CustomUserDetails(
    private val user: User
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${user.userRole.name}"))
    }

    override fun getPassword(): String = user.userPassword

    override fun getUsername(): String = user.id.toString()

    // 계정 만료 여부
    override fun isAccountNonExpired(): Boolean = true

    // 계정 잠금 여부
    override fun isAccountNonLocked(): Boolean = true

    // 비밀번호 만료 여부
    override fun isCredentialsNonExpired(): Boolean = true

    // 계정 활성화 여부
    override fun isEnabled(): Boolean = true

    // 사용자 정보 접근 메서드
    fun getId(): Long = user.id

    fun getEmail(): String = user.userEmail

    fun getUser(): User = user


}
