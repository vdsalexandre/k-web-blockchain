package com.vds.wishow.kwebblockchain.bootstrap

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes

object ErrorUtils {

    fun errorView(message: String, attributes: RedirectAttributes): ModelAndView {
        attributes.addFlashAttribute("errorMessage", message)
        attributes.addFlashAttribute("title", message)
        return ModelAndView("redirect:error")
    }

    fun errorResponse(status: HttpStatus, errorMessage: String? = null): ResponseEntity<Any> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        return ResponseEntity("${status.value()} - ${status.reasonPhrase} $errorMessage", headers, status)
    }
}