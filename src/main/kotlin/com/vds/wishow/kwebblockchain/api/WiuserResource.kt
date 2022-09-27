package com.vds.wishow.kwebblockchain.api

import com.vds.wishow.kwebblockchain.bootstrap.Utils.hash
import com.vds.wishow.kwebblockchain.logger.LoggerAOP
import com.vds.wishow.kwebblockchain.service.WiuserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
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
    @PostMapping("/login")
    fun login(request: HttpServletRequest, model: MutableMap<String, Any>): ModelAndView {
        val hashedUserEmail = hash("SHA-512", request.getParameter("email"))
        val hashedUserPassword = hash("SHA-512", request.getParameter("password"))
        val wiuser = service.findBy(hashedUserEmail, hashedUserPassword)

        return if (wiuser != null) {
            model["title"] = "Home - Welcome to Wicoin Blockchain"
            model["username"] = wiuser.username
            ModelAndView("home", model)
        } else {
            model["title"] = "Login - Welcome to Wicoin Blockchain"
            model["errorMessage"] = "User not found (problem with email and/or password)"
            ModelAndView("login", model)
        }
    }
}