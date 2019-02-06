package com.erikschouten.usermodule.controller.dto.`in`

import com.erikschouten.customclasses.validators.Email
import com.erikschouten.customclasses.validators.NoHtml

data class UpdateAppUserDTO(@field:[Email NoHtml] val email: String,
                            @field:[NoHtml] val authorities: List<String>,
                            val locked: Boolean)
