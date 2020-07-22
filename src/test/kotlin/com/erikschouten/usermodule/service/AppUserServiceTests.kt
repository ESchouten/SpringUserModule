package com.erikschouten.usermodule.service

import com.erikschouten.customclasses.exceptions.handling.FieldErrorException
import com.erikschouten.usermodule.AppUserBuilder
import com.erikschouten.usermodule.model.AppUser
import com.erikschouten.usermodule.validator.RoleValidator
import com.erikschouten.usermodule.repository.AppUserRepository
import com.erikschouten.usermodule.service.util.AppUserUtil
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class AppUserServiceTests {

    private val appUserRepository = mock<AppUserRepository>()
    private val appUserUtil = mock<AppUserUtil>()
    private val passwordEncoder = mock<PasswordEncoder>()
    private val appUserService = AppUserService(appUserRepository, appUserUtil, passwordEncoder, RoleValidator())

    @Test
    fun validAppUserCreation() {
        whenever(appUserUtil.get(any<String>())).thenReturn(AppUserBuilder(id = UUID.randomUUID(), email = "TransactionUserTestInvalid@headon.nl").build())
        whenever(passwordEncoder.encode("p")).thenReturn("vErYsEcUrEpAsSwOrD")
        whenever(appUserRepository.save(any<AppUser>()))
                .thenReturn(AppUserBuilder(email = "validAppUserCreation@headon.nl").build())

        appUserService.create("validAppUserCreation@headon.nl", "p")
    }

    @Test
    fun appUserUpdate() {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken("appUserUpdate@headon.nl", "p")
        whenever(appUserUtil.findCurrent()).thenReturn(AppUserBuilder(email = "appUserUpdate@headon.nl").build())
        whenever(appUserRepository.save(any<AppUser>())).thenReturn(AppUserBuilder(email = "appUserUpdate@headon.nl").build())

        appUserService.update("appUserUpdate@gmail.com")
    }

    @Test
    fun appUserUpdateAdmin() {
        whenever(appUserUtil.get(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85")))
                .thenReturn(AppUserBuilder(email = "appUserUpdateAdmin@headon.nl").build())
        whenever(appUserRepository.save(any<AppUser>()))
                .thenReturn(AppUserBuilder(email = "appUserUpdateAdmin@headon.nl").build())

        appUserService.update(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"), "appUserUpdateAdmin@gmail.com", setOf(SimpleGrantedAuthority("ROLE_USERS")), false)
    }

    @Test
    fun appUserUpdateAdminEmailNoChange() {
        whenever(appUserUtil.get(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85")))
                .thenReturn(AppUserBuilder(email = "appUserUpdateAdmin@headon.nl").build())
        whenever(appUserRepository.save(any<AppUser>())).thenReturn(AppUserBuilder(email = "appUserUpdateAdmin@headon.nl").build())

        appUserService.update(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"), "appUserUpdateAdmin@headon.nl", emptySet(), true)
    }

    @Test
    fun changePasswordUser() {
        whenever(passwordEncoder.encode("p")).thenReturn("vErYsEcUrEpAsSwOrD")
        whenever(passwordEncoder.encode("q")).thenReturn("DiFfErEnTvErYsEcUrEpAsSwOrD")
        whenever(passwordEncoder.matches("p", "vErYsEcUrEpAsSwOrD")).thenReturn(true)

        val appUser = AppUserBuilder(email = "changePasswordUser@headon.nl", password = "p", encoder = passwordEncoder).build()

        whenever(appUserUtil.findCurrent()).thenReturn(appUser)

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken("changePasswordUser@headon.nl", "p")
        appUserService.changePassword("p", "q")
    }

    @Test(expected = FieldErrorException::class)
    fun wrongPasswordChangePasswordUser() {
        whenever(passwordEncoder.encode("p")).thenReturn("vErYsEcUrEpAsSwOrD")
        whenever(passwordEncoder.matches("p", "vErYsEcUrEpAsSwOrD")).thenReturn(true)

        val password = passwordEncoder.encode("p")

        whenever(appUserUtil.findCurrent())
                .thenReturn(AppUserBuilder(email = "wrongPasswordChangePasswordUser@headon.nl", password = password).build())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken("wrongPasswordChangePasswordUser@headon.nl", "p")
        appUserService.changePassword("asd", "q")
    }

    @Test(expected = FieldErrorException::class)
    fun samePasswordChangePasswordUser() {
        appUserService.changePassword("p", "p")
    }

    @Test
    fun changePasswordAdmin() {
        whenever(passwordEncoder.encode("q")).thenReturn("DiFfErEnTvErYsEcUrEpAsSwOrD")
        whenever(appUserUtil.get(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85")))
                .thenReturn(AppUserBuilder(email = "changePasswordAdmin@headon.nl").build())

        appUserService.changePassword(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"), "q")
    }

    @Test
    fun listAppUserDTO() {
        whenever(appUserRepository.findAll())
                .thenReturn(listOf(AppUserBuilder().build(), AppUserBuilder(email = "test2@headon.nl").build()))
        appUserService.getAll(true)
    }

    @Test
    fun getCurrentUser() {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken("getCurrentUser@headon.nl", "p")
        whenever(appUserUtil.findCurrent())
                .thenReturn(AppUserBuilder(email = "getCurrentUser@headon.nl").build())

        val appUser = appUserUtil.findCurrent()
        assert(appUser.email == "getCurrentUser@headon.nl")
    }
}
