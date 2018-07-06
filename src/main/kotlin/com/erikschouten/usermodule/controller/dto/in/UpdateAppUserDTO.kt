package com.erikschouten.usermodule.controller.dto.`in`

import javax.validation.constraints.Email
import com.erikschouten.usermodule.model.Authority

data class UpdateAppUserDTO(@field:[Email] val email: String,
                            val authorities: List<Authority>,
                            val locked: Boolean)
