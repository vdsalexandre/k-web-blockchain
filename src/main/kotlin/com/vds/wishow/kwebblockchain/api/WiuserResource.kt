package com.vds.wishow.kwebblockchain.api

import com.vds.wishow.kwebblockchain.bootstrap.Utils.HOME_TITLE
import com.vds.wishow.kwebblockchain.bootstrap.Utils.LOGIN_TITLE
import com.vds.wishow.kwebblockchain.bootstrap.Utils.REGISTER_TITLE
import com.vds.wishow.kwebblockchain.bootstrap.Utils.USER_NOT_FOUND
import com.vds.wishow.kwebblockchain.bootstrap.Utils.hash
import com.vds.wishow.kwebblockchain.logger.LoggerAOP
import com.vds.wishow.kwebblockchain.model.Wiuser
import com.vds.wishow.kwebblockchain.service.WiuserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/blockchain")
class WiuserResource(val service: WiuserService) {

    @LoggerAOP
    @GetMapping("/")
    fun index(model: MutableMap<String, Any>): ModelAndView {
        return ModelAndView("redirect:login")
    }

    @LoggerAOP
    @GetMapping("/login")
    fun showLogin(model: MutableMap<String, Any>): ModelAndView {
        model["title"] = LOGIN_TITLE
        return ModelAndView("login", model)
    }

    @LoggerAOP
    @PostMapping("/login")
    fun handleLogin(@RequestParam email: String, @RequestParam password: String, attributes: RedirectAttributes, model: MutableMap<String, Any>): ModelAndView {
        val hashedUserEmail = hash("SHA-512", email)
        val hashedUserPassword = hash("SHA-512", password)
        val wiuser = service.findBy(hashedUserEmail, hashedUserPassword)

        return if (wiuser != null) {
            attributes.addFlashAttribute("title", HOME_TITLE)
            attributes.addFlashAttribute("username", wiuser.username)
            ModelAndView("redirect:home")
        } else {
            model["errorMessage"] = USER_NOT_FOUND
            model["title"] = LOGIN_TITLE
            return ModelAndView("login", model)
        }
    }

    @LoggerAOP
    @GetMapping("/register")
    fun showRegister(model: MutableMap<String, Any>): ModelAndView {
        model["title"] = REGISTER_TITLE
        return ModelAndView("register", model)
    }

    @LoggerAOP
    @PostMapping("/register")
    fun handleRegister(@RequestParam email: String, @RequestParam password: String, @RequestParam username: String): ModelAndView {
        service.save(
            Wiuser(
                email = hash("SHA-512", email),
                password = hash("SHA-512", password),
                username = username
            )
        )
        return ModelAndView("redirect:login")
    }

    @LoggerAOP
    @GetMapping("/home")
    fun home(model: MutableMap<String, Any>): ModelAndView {
        return ModelAndView("home", model)
    }
}