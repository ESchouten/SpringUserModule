package com.erikschouten.usermodule.controller.dto.`in`

import com.erikschouten.customclasses.validators.NoHtml
import com.erikschouten.customclasses.validators.Password

data class ChangePasswordDTO(@field:[NoHtml] val currentPassword: String,
                             @field:[Password NoHtml] val newPassword: String)
