package com.erikschouten.usermodule.controller.dto.`in`

import javax.validation.constraints.Email
import com.erikschouten.customclasses.validators.Password
import com.erikschouten.usermodule.model.Authority

data class CreateAppUserDTO(@field:[Email] val email: String,
                            @field:[Password] val password: String,
                            val authorities: List<Authority>,
                            val locked: Boolean)
