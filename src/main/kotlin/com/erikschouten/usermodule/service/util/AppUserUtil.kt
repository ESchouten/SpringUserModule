package com.erikschouten.usermodule.service.util

import com.erikschouten.customclasses.exceptions.handling.FieldErrorException
import com.erikschouten.usermodule.model.AppUser
import com.erikschouten.usermodule.repository.AppUserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppUserUtil(private val appUserRepository: AppUserRepository) : UserDetailsService {

    /**
     * User functionality
     * Returns current logged in AppUsers info
     *
     * Used in account settings
     */
    @Throws(FieldErrorException::class)
    fun findCurrent() = get(SecurityContextHolder.getContext().authentication.name)

    @Throws(FieldErrorException::class)
    fun emailInUse(email: String) {
        if (appUserRepository.findByEmail(email).isPresent) throw FieldErrorException("user", "email", HttpStatus.CONFLICT.value())
    }

    @Throws(FieldErrorException::class)
    fun get(email: String): AppUser = appUserRepository.findByEmail(email).orElseThrow { FieldErrorException("user", status = HttpStatus.NOT_FOUND.value()) }

    @Throws(FieldErrorException::class)
    fun get(id: UUID): AppUser = appUserRepository.findById(id).orElseThrow { FieldErrorException("user", status = HttpStatus.NOT_FOUND.value()) }

    @Throws(FieldErrorException::class)
    override fun loadUserByUsername(email: String): UserDetails = get(email)
}
