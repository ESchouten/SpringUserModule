package com.erikschouten.usermodule.errorHandeling

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.lang.Exception

fun getMessage(objectName: String, field: String, exceptionMessage: String, description: FieldErrors?): String {
    val builder = StringBuilder()
    builder.append("Error on field.")

    if (objectName != "") builder.append(" object: $objectName,")
    if (field != "") builder.append(" field: $field,")
    if (exceptionMessage != "") builder.append(" exceptionMessage: $exceptionMessage,")
    if (description != null) builder.append(" error: ${description.name},")

    return builder.toString()
}

class FieldErrorException(
        val objectName: String = "",
        val field: String = "",
        val description: FieldErrors? = null,
        val exceptionMessage: String = ""
) : Exception(getMessage(objectName, field, exceptionMessage, description)) {

    constructor(message: String, objectName: String = "", field: String = "") : this(objectName = objectName, field = field, exceptionMessage = message)

    constructor(description: FieldErrors, objectName: String = "", field: String = "") : this(objectName = objectName, field = field, description = description, exceptionMessage = "")

    fun toResponse() = if (description != null) {
        ResponseEntity(FieldErrorExceptionDTO(this), description.status)
    } else {
        ResponseEntity(FieldErrorExceptionDTO(this), HttpStatus.BAD_REQUEST)
    }

}
