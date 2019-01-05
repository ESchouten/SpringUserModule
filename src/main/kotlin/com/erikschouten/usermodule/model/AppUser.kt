package com.erikschouten.usermodule.model

import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*

@Entity
class AppUser private constructor(@Id @GeneratedValue @Column(columnDefinition = "BINARY(16)", nullable = false)
                                  val id: UUID = UUID.randomUUID(),
                                  @Column(unique = true, nullable = false)
                                  var email: String,
                                  @Column(nullable = false)
                                  private var password: String,
                                  @ElementCollection(fetch = FetchType.EAGER)
                                  private var roles: Set<String>,
                                  @Column(nullable = false)
                                  var locked: Boolean = false) : UserDetails, CredentialsContainer {

    constructor(id: UUID = UUID.randomUUID(), email: String, password: String, authorities: Set<SimpleGrantedAuthority>) : this(UUID.randomUUID(), email, password, authorities.map { it.authority }.toSet())

    override fun eraseCredentials() {
        password = ""
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it) }.toMutableList()
    }

    fun setAuthorities(authorities: Set<SimpleGrantedAuthority>) {
        this.roles = authorities.map { it.authority }.toSet()
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getUsername(): String {
        return email
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getPassword(): String {
        return password
    }

    fun setPassword(password: String) {
        this.password = password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return !locked
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppUser

        if (id != other.id) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (locked != other.locked) return false
        if (roles != other.roles) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + locked.hashCode()
        result = 31 * result + roles.hashCode()
        return result
    }
}
