package com.erikschouten.usermodule.service.util

import com.erikschouten.customclasses.exceptions.NotFoundException
import com.erikschouten.usermodule.model.AppUser
import com.erikschouten.usermodule.repository.AppUserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
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
    fun findCurrent() = get(SecurityContextHolder.getContext().authentication.name)

    fun emailInUse(email: String) = appUserRepository.findByEmail(email).isPresent

    fun get(email: String): AppUser {
        val optionalAppUser = appUserRepository.findByEmail(email)
        if (!optionalAppUser.isPresent) throw NotFoundException("User not found")
        return optionalAppUser.get()
    }

    fun get(id: UUID): AppUser {
        val optionalAppUser = appUserRepository.findById(id)
        if (!optionalAppUser.isPresent) throw NotFoundException("User not found")
        return optionalAppUser.get()
    }

    override fun loadUserByUsername(email: String): UserDetails {
        try {
            return get(email)
        } catch (ex: NotFoundException) {
            throw UsernameNotFoundException("User not found")
        }
    }
}
