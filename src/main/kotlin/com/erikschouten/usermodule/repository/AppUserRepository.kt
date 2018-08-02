package com.erikschouten.usermodule.repository

import com.erikschouten.usermodule.model.AppUser
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AppUserRepository<T : AppUser> : CrudRepository<T, UUID> {

    fun findByEmail(email: String): Optional<T>
}
