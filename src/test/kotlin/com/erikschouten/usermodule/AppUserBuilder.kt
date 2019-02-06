package com.erikschouten.usermodule

import com.erikschouten.usermodule.model.AppUser
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

data class AppUserBuilder(val id: UUID = UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"),
                          val email: String = "test@headon.nl",
                          val password: String = "NKCGcxGkllBcFjk",
                          val encoder: PasswordEncoder = BCryptPasswordEncoder(),
                          val roles: Set<SimpleGrantedAuthority> = emptySet(),
                          val locked: Boolean = false) {

    fun build() = AppUser(id, email, password, encoder, roles)
}