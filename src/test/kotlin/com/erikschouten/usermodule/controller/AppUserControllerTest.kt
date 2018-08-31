package com.erikschouten.usermodule.controller

import com.erikschouten.customclasses.exceptions.AlreadyExistsException
import com.erikschouten.customclasses.exceptions.InvalidParameterException
import com.erikschouten.customclasses.exceptions.NotFoundException
import com.erikschouten.usermodule.AppUserBuilder
import com.erikschouten.usermodule.controller.dto.`in`.*
import com.erikschouten.usermodule.service.AppUserService
import com.erikschouten.usermodule.service.util.AppUserUtil
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.springframework.http.HttpStatus
import java.util.*

class AppUserControllerTest {

    private val appUserService = mock<AppUserService>()
    private val appUserUtil = mock<AppUserUtil>()
    private val appUserController = AppUserController(appUserService, appUserUtil)

    @Test
    fun createTest() {
        assert(appUserController.create(CreateAppUserDTO("createTest@headon.nl", "PAss22!!", emptyList(), false)).statusCode == HttpStatus.CREATED)
        whenever(appUserService.create(any(), any(), any(), any())).doThrow(AlreadyExistsException::class)
        assert(appUserController.create(CreateAppUserDTO("createTest@headon.nl", "PAss22!!", emptyList(), false)).statusCode == HttpStatus.CONFLICT)
    }

    @Test
    fun updateTest() {
        assert(appUserController.update(EmailDTO("updateTest@headon.nl")).statusCode == HttpStatus.ACCEPTED)
        whenever(appUserService.update(any())).doThrow(AlreadyExistsException::class)
        assert(appUserController.update(EmailDTO("updateTest@headon.nl")).statusCode == HttpStatus.CONFLICT)
        whenever(appUserService.update(any())).doThrow(NotFoundException::class)
        assert(appUserController.update(EmailDTO("updateTest@headon.nl")).statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    fun updateAdminTest() {
        assert(appUserController.update(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"), UpdateAppUserDTO("updateTest@headon.nl", emptyList(), false)).statusCode == HttpStatus.ACCEPTED)
        whenever(appUserService.update(any(), any(), any(), any())).doThrow(AlreadyExistsException::class)
        assert(appUserController.update(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"), UpdateAppUserDTO("updateTest@headon.nl", emptyList(), false)).statusCode == HttpStatus.CONFLICT)
        whenever(appUserService.update(any(), any(), any(), any())).doThrow(NotFoundException::class)
        assert(appUserController.update(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"), UpdateAppUserDTO("updateTest@headon.nl", emptyList(), false)).statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    fun changePasswordTest() {
        assert(appUserController.changePassword(ChangePasswordDTO("PAss11@@", "PAss11@@")).statusCode == HttpStatus.ACCEPTED)
        whenever(appUserService.changePassword(any<String>(), any())).doThrow(InvalidParameterException::class)
        assert(appUserController.changePassword(ChangePasswordDTO("PAss11@@", "PAss11@@")).statusCode == HttpStatus.EXPECTATION_FAILED)
        whenever(appUserService.changePassword(any<String>(), any())).doThrow(NotFoundException::class)
        assert(appUserController.changePassword(ChangePasswordDTO("PAss11@@", "PAss11@@")).statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    fun changePasswordAdminTest() {
        assert(appUserController.changePassword(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"), PasswordDTO("PAss11@@")).statusCode == HttpStatus.ACCEPTED)
        whenever(appUserService.changePassword(any<UUID>(), any())).doThrow(NotFoundException::class)
        assert(appUserController.changePassword(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"), PasswordDTO("PAss11@@")).statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    fun findAllTest() {
        whenever(appUserService.getAll()).thenReturn(emptyList())
        assert(appUserController.getAll().isEmpty())
        whenever(appUserService.getAll()).thenReturn(listOf(AppUserBuilder().build(), AppUserBuilder().build()))
        assert(appUserController.getAll().size == 2)
    }
}