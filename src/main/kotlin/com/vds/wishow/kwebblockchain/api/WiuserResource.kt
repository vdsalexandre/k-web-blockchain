package com.vds.wishow.kwebblockchain.api

import com.vds.wishow.kwebblockchain.logger.LoggerAOP
import com.vds.wishow.kwebblockchain.model.Wiuser
import com.vds.wishow.kwebblockchain.service.WiuserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

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
    fun login(@ModelAttribute wiUser: Wiuser, model: MutableMap<String, Any>): ModelAndView {
        return if (service.findBy(wiUser.email, wiUser.password) != null) {
            model["title"] = "Home - Welcome to Wicoin Blockchain"
            model["email"] = wiUser.email
            ModelAndView("home", model)
        } else {
            model["title"] = "Login - Welcome to Wicoin Blockchain"
            model["errorMessage"] = "User not found (problem with email and/or password)"
            ModelAndView("login", model)
        }
    }
}