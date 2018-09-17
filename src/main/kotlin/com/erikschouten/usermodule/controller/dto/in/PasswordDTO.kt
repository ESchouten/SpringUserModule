package com.erikschouten.usermodule.controller.dto.`in`

import com.erikschouten.customclasses.validators.NoHtml
import com.erikschouten.customclasses.validators.Password

class PasswordDTO(@field:[Password NoHtml] val password: String)
