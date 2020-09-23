package com.erikschouten.usermodule.model

import com.fasterxml.jackson.annotation.JsonIgnoreType
import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreType
open class AppUser(
        @Id @GeneratedValue @Column(columnDefinition = "BINARY(16)", nullable = false)
        open val id: UUID = UUID.randomUUID(),
        @Column(unique = true, nullable = false)
        open var email: String,
        @Column(nullable = false)
        private var userPassword: String,
        @ElementCollection(fetch = FetchType.EAGER)
        open var roles: Set<String>,
        @Column(nullable = false)
        open var isLocked: Boolean = false) : UserDetails, CredentialsContainer {

    constructor(id: UUID = UUID.randomUUID(), email: String, password: String, encoder: PasswordEncoder, authorities: Set<SimpleGrantedAuthority>, locked: Boolean = false) : this(UUID.randomUUID(), email, encoder.encode(password), authorities.map { it.authority }.toSet(), locked)
    constructor(email: String, password: String, encoder: PasswordEncoder, authorities: Set<SimpleGrantedAuthority>, locked: Boolean = false) : this(UUID.randomUUID(), email, password, encoder, authorities, locked)

    override fun eraseCredentials() {
        userPassword = ""
    }

    override fun getPassword() = userPassword

    fun setPassword(password: String, encoder: PasswordEncoder) {
        this.userPassword = encoder.encode(password)
    }

    override fun getAuthorities() = roles.map { SimpleGrantedAuthority(it) }.toMutableList()

    fun setAuthorities(authorities: Set<SimpleGrantedAuthority>) {
        this.roles = authorities.map { it.authority }.toSet()
    }

    override fun getUsername() = email

    override fun isAccountNonLocked() = !isLocked

    override fun isEnabled() = true

    override fun isCredentialsNonExpired() = true

    override fun isAccountNonExpired() = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppUser

        if (id != other.id) return false
        if (email != other.email) return false
        if (userPassword != other.userPassword) return false
        if (isLocked != other.isLocked) return false
        if (roles != other.roles) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + userPassword.hashCode()
        result = 31 * result + isLocked.hashCode()
        result = 31 * result + roles.hashCode()
        return result
    }
}
