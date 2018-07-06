package com.erikschouten.usermodule.controller.dto.`in`

import javax.validation.constraints.Email
import com.erikschouten.customclasses.validators.Password

data class RegisterAppUserDTO(@field:[Email] val email: String,
                              @field:[Password] val password: String)
