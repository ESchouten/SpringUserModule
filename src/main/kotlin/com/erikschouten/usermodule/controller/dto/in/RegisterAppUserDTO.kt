package com.erikschouten.usermodule.controller.dto.`in`

import com.erikschouten.customclasses.validators.NoHtml
import com.erikschouten.customclasses.validators.Password
import com.erikschouten.customclasses.validators.Email

data class RegisterAppUserDTO(@field:[Email NoHtml] val email: String,
                              @field:[Password NoHtml] val password: String)
