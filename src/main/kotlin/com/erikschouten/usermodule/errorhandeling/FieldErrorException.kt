package com.erikschouten.usermodule.errorhandeling

import com.erikschouten.customclasses.exceptions.handling.FieldErrorException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun FieldErrorException.toResponse() =
    ResponseEntity(this, status?.let { HttpStatus.valueOf(it) } ?: HttpStatus.BAD_REQUEST)
