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
class AppUserService<T: AppUser>(private val appUserRepository: AppUserRepository<T>,
                                 private val appUserUtil: AppUserUtil<T>,
                                 private val encoder: PasswordEncoder) {

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

    private fun doUpdate(appUser: T, email: String, roles: Set<SimpleGrantedAuthority>?, locked: Boolean?): T {
        if (appUser.email != email && appUserUtil.emailInUse(email)) throw AlreadyExistsException("Email already in use")
        appUser.email = email
        if (locked != null) appUser.locked = locked
        if (roles != null) appUser.authorities = roles
        return appUserRepository.save(appUser)
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

    private fun doChangePassword(appUser: T, newPassword: String) {
        appUser.password = encoder.encode(newPassword)
        appUserRepository.save(appUser)
    }

    /**
     * Admin functionality
     * Returns list of all accounts
     *
     * Used by Administrators account management
     */
    fun getAll() = appUserRepository.findAll()
}
