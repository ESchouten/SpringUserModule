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
@RequestMapping("/users")
class AppUserController(private val appUserService: AppUserService,
                        private val appUserUtil: AppUserUtil) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ResponseEntity<AppUserDTO> =
            try {
                ResponseEntity.status(HttpStatus.OK).body(AppUserDTO(appUserUtil.get(id)))
            } catch (e: NotFoundException) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }

    @PostMapping
    fun create(@RequestBody @Valid createAppUserDTO: CreateAppUserDTO): ResponseEntity<Void> =
            try {
                appUserService.create(createAppUserDTO.email, createAppUserDTO.password,
                        createAppUserDTO.authorities.map { SimpleGrantedAuthority(it) }.toSet(), createAppUserDTO.locked)
                ResponseEntity.status(HttpStatus.CREATED).build()
            } catch (ex: AlreadyExistsException) {
                ResponseEntity.status(HttpStatus.CONFLICT).build()
            } catch (ex: InvalidParameterException) {
                ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).build()
            }

    @PutMapping
    fun update(@RequestBody @Valid emailDTO: EmailDTO): ResponseEntity<Void> =
            try {
                appUserService.update(emailDTO.email)
                ResponseEntity.status(HttpStatus.ACCEPTED).build()
            } catch (ex: AlreadyExistsException) {
                ResponseEntity.status(HttpStatus.CONFLICT).build()
            } catch (ex: NotFoundException) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody @Valid updateAppUserDTO: UpdateAppUserDTO): ResponseEntity<Void> =
            try {
                appUserService.update(id, updateAppUserDTO.email, updateAppUserDTO.authorities.map { SimpleGrantedAuthority(it) }.toSet(), updateAppUserDTO.locked)
                ResponseEntity.status(HttpStatus.ACCEPTED).build()
            } catch (ex: AlreadyExistsException) {
                ResponseEntity.status(HttpStatus.CONFLICT).build()
            } catch (ex: NotFoundException) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            } catch (ex: InvalidParameterException) {
                ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).build()
            }

    @PutMapping("/password")
    fun changePassword(@RequestBody @Valid changePasswordDTO: ChangePasswordDTO): ResponseEntity<Void> =
            try {
                appUserService.changePassword(changePasswordDTO.currentPassword, changePasswordDTO.newPassword)
                ResponseEntity.status(HttpStatus.ACCEPTED).build()
            } catch (ex: NotFoundException) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            } catch (ex: InvalidParameterException) {
                ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build()
            }

    @PutMapping("/{id}/password")
    fun changePassword(@PathVariable id: UUID, @RequestBody @Valid passwordDTO: PasswordDTO): ResponseEntity<Void> =
            try {
                appUserService.changePassword(id, passwordDTO.password)
                ResponseEntity.status(HttpStatus.ACCEPTED).build()
            } catch (ex: NotFoundException) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }

    @GetMapping
    fun getAll(@RequestParam locked: Boolean = false): List<AppUserDTO> = appUserService.getAll(locked).map { AppUserDTO(it) }

}
