package com.erikschouten.usermodule.controller

import com.erikschouten.usermodule.AppUserBuilder
import com.erikschouten.usermodule.service.AppUserService
import com.erikschouten.usermodule.service.util.AppUserUtil
import com.nhaarman.mockitokotlin2.*
import org.junit.Test

class AppUserControllerTest {

    private val appUserService = mock<AppUserService>()
    private val appUserUtil = mock<AppUserUtil>()
    private val appUserController = AppUserController(appUserService, appUserUtil)

    @Test
    fun findAllTest() {
        whenever(appUserService.getAll()).thenReturn(emptyList())
        assert(appUserController.getAll().isEmpty())
        whenever(appUserService.getAll()).thenReturn(listOf(AppUserBuilder().build(), AppUserBuilder().build()))
        assert(appUserController.getAll().size == 2)
    }
}
