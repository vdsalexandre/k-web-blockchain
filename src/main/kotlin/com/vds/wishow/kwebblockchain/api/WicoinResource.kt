package com.vds.wishow.kwebblockchain.api

import com.vds.wishow.kwebblockchain.logger.LoggerAOP
import com.vds.wishow.kwebblockchain.model.User
import com.vds.wishow.kwebblockchain.service.WicoinService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping(value = ["/blockchain"])
class WicoinResource(val service: WicoinService) {

    @LoggerAOP
    @GetMapping(value = ["/", ""])
    fun index(model: MutableMap<String, Any>): ModelAndView {
        model["title"] = "Login - Welcome to Wicoin Blockchain"
        return ModelAndView("login", model)
    }

    @LoggerAOP
    @PostMapping("/login")
    fun login(@ModelAttribute user: User, model: MutableMap<String, Any>): ModelAndView {
        model["title"] = "Home - Welcome to Wicoin Blockchain"
        model["email"] = user.email!!
        return ModelAndView("home", model)
    }
}