package com.erikschouten.usermodule.service

import com.erikschouten.customclasses.exceptions.InvalidParameterException
import com.erikschouten.customclasses.exceptions.handling.FieldErrorException
import com.erikschouten.usermodule.errorhandeling.CustomFieldErrors
import com.erikschouten.usermodule.model.AppUser
import com.erikschouten.usermodule.validator.RoleValidator
import com.erikschouten.usermodule.repository.AppUserRepository
import com.erikschouten.usermodule.service.util.AppUserUtil
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppUserService(private val appUserRepository: AppUserRepository,
                     private val appUserUtil: AppUserUtil,
                     private val encoder: PasswordEncoder,
                     private val roleValidator: RoleValidator) {

    /**
     * User functionality
     * Creates AppUser with default User Authorities
     *
     * Used in registration
     */
    @Throws(FieldErrorException::class)
    fun create(email: String, password: String) = this.create(email, password, emptySet(), false)

    /**
     * Admin functionality
     * Creates AppUser with specified Authorities
     *
     * Used by Administrators account creation
     */
    @Throws(FieldErrorException::class)
    fun create(email: String, password: String, roles: Set<SimpleGrantedAuthority>, locked: Boolean = false) =
            this.create(AppUser(email = email, password = password, encoder = encoder, authorities = roles, locked = locked))

    @Throws(FieldErrorException::class)
    fun create(appUser: AppUser): AppUser {
        appUserUtil.emailInUse(appUser.email)

        // todo: Not actually used. Check method
        if (!roleValidator.validate(appUser.authorities.toSet())) throw InvalidParameterException("Role not allowed")

        return appUserRepository.save(appUser)
    }

    /**
     * User functionality
     * Updates AppUser Email
     *
     * Used in account settings
     */
    @Throws(FieldErrorException::class)
    fun update(email: String) = doUpdate(appUserUtil.findCurrent(), email, null, null)

    /**
     * Admin functionality
     * Updates AppUser with email and specified Authorities
     *
     * Used by Administrators account management
     */
    @Throws(FieldErrorException::class)
    fun update(id: UUID, email: String?, roles: Set<SimpleGrantedAuthority>?, locked: Boolean?) = doUpdate(appUserUtil.get(id), email, roles, locked)

    @Throws(FieldErrorException::class)
    private fun doUpdate(appUser: AppUser, email: String?, roles: Set<SimpleGrantedAuthority>?, locked: Boolean?): AppUser {
        if (!email.isNullOrBlank() && appUser.email != email) appUserUtil.emailInUse(email)

        if (roles != null && !roleValidator.validate(roles)) throw InvalidParameterException("Role not allowed")

        return appUserRepository.save(
                appUser.apply {
                    if (!email.isNullOrBlank()) this.email = email
                    if (locked != null) this.isLocked = locked
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
    @Throws(FieldErrorException::class)
    fun changePassword(currentPassword: String, newPassword: String) {
        if (currentPassword == newPassword) throw FieldErrorException("user", "password", description = CustomFieldErrors.SAME_PASSWORD.name)
        val appUser = appUserUtil.findCurrent()
        if (!encoder.matches(currentPassword, appUser.password)) throw FieldErrorException("user", "password", description = "Wrong current password")
        doChangePassword(appUser, newPassword)
    }

    /**
     * Admin functionality
     * Updates AppUser password
     *
     * Used by Administrators account management
     */
    @Throws(FieldErrorException::class)
    fun changePassword(id: UUID, newPassword: String) {
        val appUser = appUserUtil.get(id)
        this.doChangePassword(appUser, newPassword)
    }

    private fun doChangePassword(appUser: AppUser, newPassword: String) {
        appUserRepository.save(
                appUser.apply { setPassword(newPassword, encoder) }
        )
    }

    fun delete(id: UUID) {
        appUserRepository.deleteById(id)
    }

    /**
     * Admin functionality
     * Returns list of all accounts
     *
     * Used by Administrators account management
     */
    fun getAll(all: Boolean) = if (all) appUserRepository.findAll() else appUserRepository.findAllByIsLockedFalse()
}
