package com.vds.wishow.kwebblockchain.api

import com.vds.wishow.kwebblockchain.bootstrap.Utils.hash
import com.vds.wishow.kwebblockchain.logger.LoggerAOP
import com.vds.wishow.kwebblockchain.service.WiuserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.View
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping(value = ["/blockchain"])
class WiuserResource(val service: WiuserService) {

    @LoggerAOP
    @GetMapping(value = ["/", ""])
    fun index(model: MutableMap<String, Any>): ModelAndView {
        model["title"] = "Login - Welcome to Wicoin Blockchain"
        return ModelAndView("login", model)
    }

    @LoggerAOP
    @GetMapping("/home")
    fun home(model: MutableMap<String, Any>): ModelAndView {
        return ModelAndView("home", model)
    }

    @LoggerAOP
    @PostMapping("/login")
    fun login(request: HttpServletRequest, model: MutableMap<String, Any>, redirectAttributes: RedirectAttributes): ModelAndView {
        val hashedUserEmail = hash("SHA-512", request.getParameter("email"))
        val hashedUserPassword = hash("SHA-512", request.getParameter("password"))
        val wiuser = service.findBy(hashedUserEmail, hashedUserPassword)

        return if (wiuser != null) {
            redirectAttributes.addFlashAttribute("title", "Home - Welcome to Wicoin Blockchain")
            redirectAttributes.addFlashAttribute("username", wiuser.username)
            ModelAndView("redirect:home")
        } else {
            model["title"] = "Login - Welcome to Wicoin Blockchain"
            model["errorMessage"] = "User not found (problem with email and/or password)"
            ModelAndView("login", model)
        }
    }
}