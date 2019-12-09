package com.erikschouten.usermodule.errorHandeling

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.lang.Exception

fun getMessage(objectName: String?, field: String?, exceptionMessage: String?, description: FieldErrors?): String {
    val builder = StringBuilder()
    builder.append("Error on field.")

    if (objectName != null) builder.append(" object: $objectName,")
    if (field != null) builder.append(" field: $field,")
    if (exceptionMessage != null) builder.append(" exceptionMessage: $exceptionMessage,")
    if (description != null) builder.append(" error: ${description.name},")

    return builder.toString()
}

class FieldErrorException(
        val objectName: String? = null,
        val field: String? = null,
        val fieldError: FieldErrors? = null,
        val exceptionMessage: String? = null
) : Exception(getMessage(objectName, field, exceptionMessage, fieldError)) {

    constructor(message: String, objectName: String? = null, field: String? = null) : this(objectName = objectName, field = field, exceptionMessage = message)

    constructor(description: FieldErrors, objectName: String? = null, field: String? = null, message: String? = null) : this(objectName = objectName, field = field, fieldError = description, exceptionMessage = message)

    fun toResponse() = if (fieldError != null) {
        ResponseEntity(FieldErrorExceptionDTO(this), fieldError.status)
    } else {
        ResponseEntity(FieldErrorExceptionDTO(this), HttpStatus.BAD_REQUEST)
    }

}
