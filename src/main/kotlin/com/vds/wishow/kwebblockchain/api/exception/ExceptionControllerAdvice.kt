package com.vds.wishow.kwebblockchain.api.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun handleMethodArgumentTypeMismatchException(
        e: MethodArgumentTypeMismatchException,
        attributes: RedirectAttributes
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(HttpStatus.BAD_REQUEST.value(), "${HttpStatus.BAD_REQUEST} - ${e.message}"),
            HttpStatus.BAD_REQUEST
        )
    }
}
