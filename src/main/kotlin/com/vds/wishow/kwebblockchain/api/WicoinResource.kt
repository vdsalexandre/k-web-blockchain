package com.vds.wishow.kwebblockchain.api

import com.vds.wishow.kwebblockchain.model.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping(value = ["/blockchain"])
class WicoinResource {
    private val logger = LoggerFactory.getLogger(WicoinResource::class.java)

    @GetMapping
    fun index(model: MutableMap<String, Any>): ModelAndView {
        logger.info("[GET]/ -> login")
        model["title"] = "Login - Welcome to Wicoin Blockchain"
        return ModelAndView("login", model)
    }

    @PostMapping("/login")
    fun login(@ModelAttribute user: User, model: MutableMap<String, Any>): ModelAndView {
        logger.info("[POST]/login -> ${user.email}")
        model["title"] = "Home - Welcome to Wicoin Blockchain"
        model["email"] = user.email
        return ModelAndView("home", model)
    }
}