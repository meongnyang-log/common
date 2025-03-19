package auth.entity

import auth.dto.AuthProvider
import auth.dto.UserRole
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val userEmail: String,

    @Column(nullable = false)
    val userPassword: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val userRole: UserRole = UserRole.USER,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    val provider: AuthProvider,

    val isAgreed: Boolean,

    @CreatedDate
    @Column(updatable = false)
    val createdAt: LocalDateTime? = null,

    @LastModifiedDate
    val updatedAt: LocalDateTime? = null
) {
    constructor() : this(
        userEmail = "",
        userPassword = "",
        userRole = UserRole.USER,
        provider = AuthProvider.LOCAL,
        isAgreed = false,
    ) {

    }
}
