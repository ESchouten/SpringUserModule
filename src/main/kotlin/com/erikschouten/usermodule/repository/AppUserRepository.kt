package com.erikschouten.usermodule.repository

import com.erikschouten.usermodule.model.AppUser
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository : AbstractAppUserRepository<AppUser>