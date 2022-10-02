package com.vds.wishow.kwebblockchain.api.resource

import com.vds.wishow.kwebblockchain.api.dto.UserLoginDTO
import com.vds.wishow.kwebblockchain.api.dto.UserRegisterDTO
import com.vds.wishow.kwebblockchain.bootstrap.Utils.TITLE_HOME
import com.vds.wishow.kwebblockchain.bootstrap.Utils.TITLE_LOGIN
import com.vds.wishow.kwebblockchain.bootstrap.Utils.TITLE_REGISTER
import com.vds.wishow.kwebblockchain.bootstrap.Utils.TITLE_WALLET
import com.vds.wishow.kwebblockchain.bootstrap.Utils.URL_AUTH_TOKEN
import com.vds.wishow.kwebblockchain.bootstrap.Utils.URL_AUTH_USER
import com.vds.wishow.kwebblockchain.bootstrap.Utils.USER_NOT_FOUND
import com.vds.wishow.kwebblockchain.bootstrap.Utils.WRONG_AUTH_BAD_TOKEN
import com.vds.wishow.kwebblockchain.bootstrap.Utils.WRONG_AUTH_NOT_LOGGED
import com.vds.wishow.kwebblockchain.bootstrap.Utils.hash
import com.vds.wishow.kwebblockchain.domain.model.Wiuser
import com.vds.wishow.kwebblockchain.domain.service.WiuserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.util.WebUtils
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/blockchain")
class WiuserResource(val service: WiuserService) {

    @GetMapping("/")
    fun index(): ModelAndView {
        return ModelAndView("redirect:login")
    }

    @GetMapping("/login")
    fun showLogin(model: MutableMap<String, Any>): ModelAndView {
        model["title"] = TITLE_LOGIN
        return ModelAndView("login", model)
    }

    @PostMapping("/login")
    fun handleLogin(
        userLoginDTO: UserLoginDTO,
        attributes: RedirectAttributes,
        model: MutableMap<String, Any>,
        response: HttpServletResponse
    ): ModelAndView {
        val wiuser = service.findBy(hash(userLoginDTO.email), hash(userLoginDTO.password))

        return if (wiuser != null) {
            val params: MutableMap<String, Long> = mutableMapOf()
            params["id"] = wiuser.id!!
            val result = getUserToken(params)
            val jws = result.body
            val cookie = Cookie("jws", jws)
            cookie.isHttpOnly = true // browser can't read the cookie, just for the backend
            response.addCookie(cookie)
            attributes.addFlashAttribute("username", wiuser.username)
            ModelAndView("redirect:home")
        } else {
            model["errorMessage"] = USER_NOT_FOUND
            model["title"] = TITLE_LOGIN
            ModelAndView("login", model)
        }
    }

    @GetMapping("/register")
    fun showRegister(model: MutableMap<String, Any>): ModelAndView {
        model["title"] = TITLE_REGISTER
        return ModelAndView("register", model)
    }

    @PostMapping("/register")
    fun handleRegister(userRegisterDTO: UserRegisterDTO, @RequestParam username: String): ModelAndView {
        service.save(
            Wiuser(email = hash(userRegisterDTO.email), password = hash(userRegisterDTO.password), username = username)
        )
        return ModelAndView("redirect:login")
    }

    @PostMapping("/logout")
    fun handleLogout(response: HttpServletResponse): ModelAndView {
        val cookie = Cookie("jws", "")
        cookie.maxAge = 0
        response.addCookie(cookie)
        return ModelAndView("redirect:login")
    }

    @GetMapping("/home")
    fun home(request: HttpServletRequest, model: MutableMap<String, Any>, attributes: RedirectAttributes): ModelAndView {
        if (!model.containsKey("username")) {
            val params: MutableMap<String, String> = mutableMapOf()
            val cookie = WebUtils.getCookie(request, "jws")

            if (cookie != null) {
                try {
                    params["jws"] = cookie.value
                    val response = getUserDetails(params)
                    val wiuser = response.body
                    model["username"] = wiuser!!.username
                } catch (e: Exception) {
                    return errorView(WRONG_AUTH_BAD_TOKEN, attributes)
                }
            } else {
                return errorView(WRONG_AUTH_NOT_LOGGED, attributes)
            }
        }
        model["title"] = TITLE_HOME
        return ModelAndView("home", model)
    }

    @GetMapping("/wallet")
    fun wallet(request: HttpServletRequest, model: MutableMap<String, Any>, attributes: RedirectAttributes): ModelAndView {
        val params: MutableMap<String, String> = mutableMapOf()
        val cookie = WebUtils.getCookie(request, "jws")

        if (cookie != null) {
            return try {
                params["jws"] = cookie.value
                val response = getUserDetails(params)
                val wiuser = response.body
                model["title"] = TITLE_WALLET
                model["username"] = wiuser!!.username
                model["id"] = wiuser.id.toString()
                ModelAndView("wallet", model)
            } catch (e: Exception) {
                errorView(WRONG_AUTH_BAD_TOKEN, attributes)
            }
        }
        return errorView(WRONG_AUTH_NOT_LOGGED, attributes)
    }

    private fun getUserToken(params: MutableMap<String, Long>) =
        RestTemplate().getForEntity("$URL_AUTH_TOKEN/{id}", String::class.java, params)

    private fun getUserDetails(params: MutableMap<String, String>) =
        RestTemplate().getForEntity("$URL_AUTH_USER/{jws}", Wiuser::class.java, params)

    private fun errorView(message: String, attributes: RedirectAttributes): ModelAndView {
        attributes.addFlashAttribute("errorMessage", message)
        attributes.addFlashAttribute("title", message)
        return ModelAndView("redirect:error")
    }
}