package com.erikschouten.usermodule.controller.dto.`in`

import com.erikschouten.customclasses.validators.Password

data class ChangePasswordDTO(val currentPassword: String,
                             @field:[Password] val newPassword: String)
