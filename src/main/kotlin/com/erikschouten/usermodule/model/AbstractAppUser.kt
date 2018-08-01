package com.erikschouten.usermodule.model

import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*

abstract class AbstractAppUser(@Id @GeneratedValue @Column(columnDefinition = "BINARY(16)", nullable = false)
                               val id: UUID = UUID.randomUUID(),
                               @Column(unique = true, nullable = false)
                               var email: String,
                               @Column(nullable = false)
                               private var password: String,
                               @ElementCollection(fetch = FetchType.EAGER)
                               var authorities: Set<SimpleGrantedAuthority>,
                               @Column(nullable = false)
                               var locked: Boolean = false) : UserDetails, CredentialsContainer {

    override fun eraseCredentials() {
        password = ""
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities.toMutableList()
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
}
