package com.erikschouten.usermodule.controller.dto.`in`

import com.erikschouten.customclasses.validators.NoHtml
import javax.validation.constraints.Email

class Email(@field:[Email NoHtml] val value: String)
