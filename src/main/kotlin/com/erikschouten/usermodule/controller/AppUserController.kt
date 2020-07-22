package com.erikschouten.usermodule.controller

import com.erikschouten.customclasses.exceptions.handling.FieldErrorException
import com.erikschouten.usermodule.controller.dto.`in`.*
import com.erikschouten.usermodule.controller.dto.out.AppUserDTO
import com.erikschouten.usermodule.errorhandeling.toResponse
import com.erikschouten.usermodule.service.AppUserService
import com.erikschouten.usermodule.service.util.AppUserUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class AppUserController(private val appUserService: AppUserService,
                        private val appUserUtil: AppUserUtil) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ResponseEntity<out Any> =
            try {
                ResponseEntity.status(HttpStatus.OK).body(AppUserDTO(appUserUtil.get(id)))
            } catch (ex: FieldErrorException) {
                ex.toResponse()
            }

    @PostMapping
    fun create(@RequestBody @Valid createAppUserDTO: CreateAppUserDTO): ResponseEntity<out Any> =
            try {
                appUserService.create(createAppUserDTO.email, createAppUserDTO.password,
                        createAppUserDTO.authorities.map { SimpleGrantedAuthority(it) }.toSet(), createAppUserDTO.locked)
                ResponseEntity.status(HttpStatus.CREATED).build()
            } catch (ex: FieldErrorException) {
                ex.toResponse()
            }

    @PutMapping
    fun update(@RequestBody @Valid emailDTO: EmailDTO): ResponseEntity<out Any> =
            try {
                appUserService.update(emailDTO.email)
                ResponseEntity.status(HttpStatus.ACCEPTED).build()
            } catch (ex: FieldErrorException) {
                ex.toResponse()
            }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody @Valid updateAppUserDTO: UpdateAppUserDTO): ResponseEntity<out Any> =
            try {
                appUserService.update(id, updateAppUserDTO.email, updateAppUserDTO.authorities.map { SimpleGrantedAuthority(it) }.toSet(), updateAppUserDTO.locked)
                ResponseEntity.status(HttpStatus.ACCEPTED).build()
            } catch (ex: FieldErrorException) {
                ex.toResponse()
            }

    @PutMapping("/password")
    fun changePassword(@RequestBody @Valid changePasswordDTO: ChangePasswordDTO): ResponseEntity<out Any> =
            try {
                appUserService.changePassword(changePasswordDTO.currentPassword, changePasswordDTO.newPassword)
                ResponseEntity.status(HttpStatus.ACCEPTED).build()
            } catch (ex: FieldErrorException) {
                ex.toResponse()
            }

    @PutMapping("/{id}/password")
    fun changePassword(@PathVariable id: UUID, @RequestBody @Valid passwordDTO: PasswordDTO): ResponseEntity<out Any> =
            try {
                appUserService.changePassword(id, passwordDTO.password)
                ResponseEntity.status(HttpStatus.ACCEPTED).build()
            } catch (ex: FieldErrorException) {
                ex.toResponse()
            }

    @GetMapping
    fun getAll(@RequestParam all: Boolean = false): List<AppUserDTO> = appUserService.getAll(all).map { AppUserDTO(it) }

}
