package com.erikschouten.usermodule.controller.dto.`in`

import com.erikschouten.customclasses.validators.Email
import com.erikschouten.customclasses.validators.NoHtml
import com.erikschouten.customclasses.validators.Password

data class CreateAppUserDTO(@field:[Email NoHtml] val email: String,
                            @field:[Password NoHtml] val password: String,
                            @field:[NoHtml] val authorities: List<String>,
                            val locked: Boolean)
