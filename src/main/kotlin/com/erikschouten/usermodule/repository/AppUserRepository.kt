package com.erikschouten.usermodule.repository

import com.erikschouten.usermodule.model.AppUser
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository
import java.util.*

@NoRepositoryBean
interface IAppUserRepository<T : AppUser> : CrudRepository<T, UUID> {
    fun findByEmail(email: String): Optional<T>
    fun findAllByIsLockedFalse(): List<AppUser>
}

@Repository
interface AppUserRepository : IAppUserRepository<AppUser> {}
