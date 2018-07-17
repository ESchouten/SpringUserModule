package com.erikschouten.usermodule.controller.dto.`in`

import javax.validation.constraints.Email

data class UpdateAppUserDTO(@field:[Email] val email: String,
                            val authorities: List<String>,
                            val locked: Boolean)
