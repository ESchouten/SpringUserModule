package com.erikschouten.usermodule.controller.dto.out

import com.erikschouten.usermodule.model.AppUser
import java.util.*
import javax.validation.constraints.Email

data class AppUserDTO(val id: UUID,
                      @field:[Email] val email: String,
                      val authorities: List<String>,
                      val locked: Boolean) {

    constructor(appUser: AppUser) : this(appUser.id, appUser.email, appUser.authorities.map { it.authority }, appUser.locked)
}
