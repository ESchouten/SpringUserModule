package com.erikschouten.usermodule.controller.dto

import com.erikschouten.usermodule.controller.dto.`in`.*
import com.erikschouten.usermodule.controller.dto.out.AppUserDTO
import com.erikschouten.usermodule.model.Authority
import org.junit.Before
import org.junit.Test
import java.util.*
import javax.validation.Validation
import javax.validation.Validator

class AppUserDTOConstraintsTests {

    private lateinit var validator: Validator

    @Before
    fun setUp() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    fun registerAppUserDTO() {
        //Valid
        var violations = validator.validate(RegisterAppUserDTO("registerAppUserTest@headon.nl", "PAss11@@"))
        assert(violations.isEmpty())
        //Invalid email
        violations = validator.validate(RegisterAppUserDTO("registerAppUserTest@", "PAss11@@"))
        assert(violations.size == 1)
        //Invalid password
        violations = validator.validate(RegisterAppUserDTO("registerAppUserTest@headon.nl", "PAss11asdas"))
        assert(violations.size == 1)
    }

    @Test
    fun createAppUserDTO() {
        //Valid
        var violations = validator.validate(CreateAppUserDTO("createAppUserTest@headon.nl", "PAss11@@", listOf(Authority.ROLE_USERS), false))
        assert(violations.isEmpty())
        //Invalid email
        violations = validator.validate(CreateAppUserDTO("createAppUserTest@", "PAss11@@", listOf(Authority.ROLE_USERS), false))
        assert(violations.size == 1)
        //Invalid password
        violations = validator.validate(CreateAppUserDTO("createAppUserTest@headon.nl", "PAss11asdfm", listOf(Authority.ROLE_USERS), false))
        assert(violations.size == 1)
    }

    @Test
    fun email() {
        //Valid
        var violations = validator.validate(Email("emailTest@headon.nl"))
        assert(violations.isEmpty())
        //Invalid
        violations = validator.validate(Email("emailTest@"))
        assert(violations.size == 1)
    }

    @Test
    fun password() {
        //Valid
        var violations = validator.validate(Password("PAss11@@"))
        assert(violations.isEmpty())
        //Invalid
        violations = validator.validate(Password("PAss11acmadjkls"))
        assert(violations.size == 1)
    }

    @Test
    fun updateAppUserDTO() {
        //Valid
        var violations = validator.validate(UpdateAppUserDTO("updateAppUserTest@headon.nl", listOf(Authority.ROLE_USERS), false))
        assert(violations.isEmpty())
        //Invalid email
        violations = validator.validate(UpdateAppUserDTO("updateAppUserTest@", listOf(Authority.ROLE_USERS), false))
        assert(violations.size == 1)
    }

    @Test
    fun changePasswordDTO() {
        //Valid
        var violations = validator.validate(ChangePasswordDTO("PAss11@@", "PAss11@@!"))
        assert(violations.isEmpty())
        //Invalid
        violations = validator.validate(ChangePasswordDTO("PAss11@@", "PAss11aaa"))
        assert(violations.size == 1)
        //Valid
        violations = validator.validate(ChangePasswordDTO("PAss11aaa", "PAss11@@"))
        assert(violations.isEmpty())
    }

    @Test
    fun appUserDTO() {
        //Valid
        var violations = validator.validate(AppUserDTO(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"), "appUserDTO@headon.nl", emptyList(), false))
        assert(violations.isEmpty())
        //Invalid Email
        violations = validator.validate(AppUserDTO(UUID.fromString("befa7c20-20ae-42dd-ad1f-b061cce7ad85"), "appUserDTO", emptyList(), false))
        assert(violations.size == 1)
    }
}