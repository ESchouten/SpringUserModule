package com.erikschouten.usermodule.errorHandeling

data class FieldErrorExceptionDTO(val field: String, val objectName: String, val message: String) {
    constructor(exception: FieldErrorException): this(exception.field, exception.objectName, if (exception.description != null) exception.description.name else exception.exceptionMessage)
}
