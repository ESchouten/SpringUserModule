package com.erikschouten.usermodule.repository

import com.erikschouten.usermodule.model.AbstractAppUser
import org.springframework.data.repository.CrudRepository
import java.util.*

interface AbstractAppUserRepository<T : AbstractAppUser> : CrudRepository<T, UUID> {

    fun findByEmail(email: String): Optional<T>
}
