package com.erikschouten.usermodule.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.erikschouten.customclasses.exceptions.NotFoundException
import com.erikschouten.usermodule.AppUserBuilder
import com.erikschouten.usermodule.repository.AppUserRepository
import com.erikschouten.usermodule.service.util.AppUserUtil
import org.junit.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*

class AppUserUtilTests {

    private val appUserRepository = mock<AppUserRepository>()
    private val appUserUtil = AppUserUtil(appUserRepository)

    @Test
    fun findCurrentUser() {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken("findCurrentUser@headon.nl", "p")
        whenever(appUserRepository.findByEmail("findCurrentUser@headon.nl")).thenReturn(Optional.of(AppUserBuilder(email = "findCurrentUser@headon.nl").build()))
        val appUser = appUserUtil.findCurrent()

        assert(appUser.email == "findCurrentUser@headon.nl")
    }

    @Test
    fun emailInUse() {
        whenever(appUserRepository.findByEmail("emailInUse@headon.nl")).thenReturn(Optional.of(AppUserBuilder(email = "emailInUse@headon.nl").build()))
        assert(appUserUtil.emailInUse("emailInUse@headon.nl"))
    }

    @Test
    fun emailNotInUse() {
        whenever(appUserRepository.findByEmail("emailNotInUse@headon.nl")).thenReturn(Optional.empty())
        assert(!appUserUtil.emailInUse("emailNotInUse@headon.nl"))
    }

    @Test
    fun findByEmail() {
        whenever(appUserRepository.findByEmail("findByEmail@headon.nl")).thenReturn(Optional.of(AppUserBuilder(email = "findByEmail@headon.nl").build()))
        assert(appUserUtil.get("findByEmail@headon.nl").email == "findByEmail@headon.nl")
    }

    @Test(expected = NotFoundException::class)
    fun findByEmailNotFound() {
        whenever(appUserRepository.findByEmail("findByEmailNotFound@headon.nl")).thenReturn(Optional.empty())
        appUserUtil.get("findByEmailNotFound@headon.nl")
    }

    @Test
    fun findById() {
        whenever(appUserRepository.findById(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"))).thenReturn(Optional.of(AppUserBuilder(id = UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"), email = "findById@headon.nl").build()))
        assert(appUserUtil.get(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85")).id == UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"))
    }

    @Test(expected = NotFoundException::class)
    fun findByIdNotFound() {
        whenever(appUserRepository.findById(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"))).thenReturn(Optional.empty())
        appUserUtil.get(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"))
    }

    @Test
    fun loadByUsername() {
        whenever(appUserRepository.findByEmail("loadByUsername@headon.nl")).thenReturn(Optional.of(AppUserBuilder(email = "loadByUsername@headon.nl").build()))
        assert(appUserUtil.loadUserByUsername("loadByUsername@headon.nl").username == "loadByUsername@headon.nl")
    }

    @Test(expected = UsernameNotFoundException::class)
    fun loadByUsernameNotFound() {
        whenever(appUserRepository.findByEmail("findByEmailNotFound@headon.nl")).thenReturn(Optional.empty())
        appUserUtil.loadUserByUsername("findByEmailNotFound@headon.nl")
    }

}