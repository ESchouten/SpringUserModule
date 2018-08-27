package com.erikschouten.usermodule.controller.dto.`in`

import com.erikschouten.customclasses.validators.NoHtml
import com.erikschouten.customclasses.validators.NoHtmlList
import com.erikschouten.customclasses.validators.Password
import javax.validation.constraints.Email

data class CreateAppUserDTO(@field:[Email NoHtml] val email: String,
                            @field:[Password NoHtml] val password: String,
                            @field:[NoHtmlList] val authorities: List<String>,
                            val locked: Boolean)
