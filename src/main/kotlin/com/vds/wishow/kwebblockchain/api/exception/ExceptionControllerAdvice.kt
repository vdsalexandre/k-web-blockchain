package com.vds.wishow.kwebblockchain.api.exception

import com.vds.wishow.kwebblockchain.bootstrap.WiuserUtils.errorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler(value = [NullPointerException::class, MethodArgumentTypeMismatchException::class, NumberFormatException::class])
    fun handleExceptions(e: Exception, attributes: RedirectAttributes): ResponseEntity<Any> {
        return errorResponse(HttpStatus.BAD_REQUEST)
    }
}
