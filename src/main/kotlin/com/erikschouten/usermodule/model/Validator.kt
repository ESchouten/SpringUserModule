package com.erikschouten.usermodule.model

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class RoleValidator : Validator<Set<SimpleGrantedAuthority>> {
    override fun validate(obj: Set<SimpleGrantedAuthority>) = true
}

interface Validator<T : Any> {
    fun validate(obj: T): Boolean
}
