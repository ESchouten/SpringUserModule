package com.erikschouten.usermodule.controller

import com.erikschouten.customclasses.exceptions.AlreadyExistsException
import com.erikschouten.customclasses.exceptions.InvalidParameterException
import com.erikschouten.customclasses.exceptions.NotFoundException
import com.erikschouten.usermodule.controller.dto.`in`.*
import com.erikschouten.usermodule.controller.dto.out.AppUserDTO
import com.erikschouten.usermodule.service.AppUserService
import com.erikschouten.usermodule.service.util.AppUserUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class AppUserController(private val appUserService: AppUserService,
                        private val appUserUtil: AppUserUtil) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ResponseEntity<AppUserDTO> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(AppUserDTO(appUserUtil.get(id)))
        } catch (e: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PostMapping
    fun create(@RequestBody @Valid createAppUserDTO: CreateAppUserDTO): ResponseEntity<Void> {
        return try {
            appUserService.create(createAppUserDTO.email, createAppUserDTO.password,
                    createAppUserDTO.authorities.map { SimpleGrantedAuthority(it.toString()) }.toSet(), createAppUserDTO.locked)
            ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (ex: AlreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @PutMapping
    fun update(@RequestBody @Valid email: Email): ResponseEntity<Void> {
        return try {
            appUserService.update(email.value)
            ResponseEntity.status(HttpStatus.ACCEPTED).build()
        } catch (ex: AlreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        } catch (ex: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody @Valid updateAppUserDTO: UpdateAppUserDTO): ResponseEntity<Void> {
        return try {
            appUserService.update(id, updateAppUserDTO.email, updateAppUserDTO.authorities.map { SimpleGrantedAuthority(it.toString()) }.toSet(), updateAppUserDTO.locked)
            ResponseEntity.status(HttpStatus.ACCEPTED).build()
        } catch (ex: AlreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        } catch (ex: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PutMapping("/password")
    fun changePassword(@RequestBody @Valid changePasswordDTO: ChangePasswordDTO): ResponseEntity<Void> {
        return try {
            appUserService.changePassword(changePasswordDTO.currentPassword, changePasswordDTO.newPassword)
            ResponseEntity.status(HttpStatus.ACCEPTED).build()
        } catch (ex: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (ex: InvalidParameterException) {
            ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build()
        }
    }

    @PutMapping("/{id}/password")
    fun changePassword(@PathVariable id: UUID, @RequestBody @Valid password: Password): ResponseEntity<Void> {
        return try {
            appUserService.changePassword(id, password.value)
            ResponseEntity.status(HttpStatus.ACCEPTED).build()
        } catch (ex: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/all")
    fun getAll(): List<AppUserDTO> = appUserService.getAll().map { AppUserDTO(it) }

}
