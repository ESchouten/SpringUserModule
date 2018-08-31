package com.erikschouten.usermodule.controller.dto.`in`

import com.erikschouten.customclasses.validators.NoHtml
import com.erikschouten.customclasses.validators.Email

class Email(@field:[Email NoHtml] val value: String)
