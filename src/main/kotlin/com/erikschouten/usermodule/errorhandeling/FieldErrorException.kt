package com.erikschouten.usermodule.errorhandeling

import com.erikschouten.customclasses.exceptions.handling.FieldErrorException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun FieldErrorException.toResponse() =
    ResponseEntity(FieldErrorExceptionDTO(this), status?.let { HttpStatus.valueOf(it) } ?: HttpStatus.BAD_REQUEST)

class FieldErrorExceptionDTO private constructor(
    val objectName: String? = null,
    val field: String? = null,
    val status: Int? = null,
    val description: String? = null
) {
    constructor(exception: FieldErrorException) : this(exception.objectName, exception.field, exception.status, exception.description)
}
