package com.erikschouten.usermodule.service

import com.erikschouten.customclasses.exceptions.AlreadyExistsException
import com.erikschouten.customclasses.exceptions.InvalidParameterException
import com.erikschouten.usermodule.model.AppUser
import com.erikschouten.usermodule.repository.AppUserRepository
import com.erikschouten.usermodule.service.util.AppUserUtil
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppUserService(private val appUserRepository: AppUserRepository,
                     private val appUserUtil: AppUserUtil,
                     private val encoder: PasswordEncoder) {

    /**
     * User functionality
     * Creates AppUser with default User Authorities
     *
     * Used in registration
     */
    fun create(email: String, password: String) = this.doCreate(email, password,
            kotlin.collections.emptySet(), false)

    /**
     * Admin functionality
     * Creates AppUser with specified Authorities
     *
     * Used by Administrators account creation
     */
    fun create(email: String, password: String, roles: Set<SimpleGrantedAuthority>, locked: Boolean = false) =
            this.doCreate(email, password, roles, locked)

    private fun doCreate(email: String, password: String, roles: Set<SimpleGrantedAuthority>, locked: Boolean): AppUser {
        if (appUserUtil.emailInUse(email)) throw AlreadyExistsException("Email already in use")

        return appUserRepository.save(
                AppUser(email = email, password = password, encoder = encoder, authorities = roles, locked = locked)
        )
    }

    /**
     * User functionality
     * Updates AppUser Email
     *
     * Used in account settings
     */
    fun update(email: String) = doUpdate(appUserUtil.findCurrent(), email, null, null)

    /**
     * Admin functionality
     * Updates AppUser with email and specified Authorities
     *
     * Used by Administrators account management
     */
    fun update(id: UUID, email: String, roles: Set<SimpleGrantedAuthority>?, locked: Boolean) = doUpdate(appUserUtil.get(id), email, roles, locked)

    private fun doUpdate(appUser: AppUser, email: String?, roles: Set<SimpleGrantedAuthority>?, locked: Boolean?): AppUser {
        if (!email.isNullOrBlank() && appUser.email != email && appUserUtil.emailInUse(email)) throw AlreadyExistsException("Email already in use")

        return appUserRepository.save(
                appUser.apply {
                    if (!email.isNullOrBlank()) this.email = email
                    if (locked != null) this.locked = locked
                    if (roles != null) this.setAuthorities(roles)
                }
        )
    }

    /**
     * User functionality
     * Changes AppUser password if entered current password is correct
     *
     * Used in account settings
     */
    fun changePassword(currentPassword: String, newPassword: String) {
        if (currentPassword == newPassword) throw InvalidParameterException("Passwords are the same")
        val appUser = appUserUtil.findCurrent()
        if (!encoder.matches(currentPassword, appUser.password)) throw InvalidParameterException("Invalid password entered")
        doChangePassword(appUser, newPassword)
    }

    /**
     * Admin functionality
     * Updates AppUser password
     *
     * Used by Administrators account management
     */
    fun changePassword(id: UUID, newPassword: String) {
        val appUser = appUserUtil.get(id)
        this.doChangePassword(appUser, newPassword)
    }

    private fun doChangePassword(appUser: AppUser, newPassword: String) {
        appUserRepository.save(
                appUser.apply { setPassword(newPassword, encoder) }
        )
    }

    /**
     * Admin functionality
     * Returns list of all accounts
     *
     * Used by Administrators account management
     */
    fun getAll() = appUserRepository.findAll()
}
