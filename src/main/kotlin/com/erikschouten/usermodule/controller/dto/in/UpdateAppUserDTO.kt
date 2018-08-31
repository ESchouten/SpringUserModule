package com.erikschouten.usermodule.controller.dto.`in`

import com.erikschouten.customclasses.validators.NoHtml
import com.erikschouten.customclasses.validators.NoHtmlList
import com.erikschouten.customclasses.validators.Email

data class UpdateAppUserDTO(@field:[Email NoHtml] val email: String,
                            @field:[NoHtmlList] val authorities: List<String>,
                            val locked: Boolean)
