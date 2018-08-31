package com.erikschouten.usermodule.controller.dto.`in`

import com.erikschouten.customclasses.validators.Email
import com.erikschouten.customclasses.validators.NoHtml

class EmailDTO(@field:[Email NoHtml] val email: String)
