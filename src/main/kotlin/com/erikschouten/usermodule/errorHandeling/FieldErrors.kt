package com.erikschouten.usermodule.errorHandeling

import org.springframework.http.HttpStatus

enum class FieldErrors(val status: HttpStatus = HttpStatus.BAD_REQUEST) {
    NOT_FOUND(HttpStatus.NOT_FOUND), ALREADY_EXISTS(HttpStatus.CONFLICT), CONFLICT, EXPECTATION_FAILED(HttpStatus.EXPECTATION_FAILED)
}
