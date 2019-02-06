package com.erikschouten.usermodule.controller.dto.out

import com.erikschouten.customclasses.validators.Email
import com.erikschouten.customclasses.validators.NoHtml
import com.erikschouten.usermodule.model.AppUser
import java.util.*

data class AppUserDTO(val id: UUID,
                      @field:[Email NoHtml] val email: String,
                      @field:[NoHtml] val authorities: List<String>,
                      val locked: Boolean) {

    constructor(appUser: AppUser) : this(appUser.id, appUser.email, appUser.authorities.map { it.authority }, appUser.locked)
}
