package com.erikschouten.usermodule.model

import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*
import javax.persistence.*

@Entity
class AppUser(id: UUID = UUID.randomUUID(),
              email: String,
              password: String,
              authorities: Set<SimpleGrantedAuthority>,
              locked: Boolean) : AbstractAppUser(id, email, password, authorities, locked)
