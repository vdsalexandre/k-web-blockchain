package com.vds.wishow.kwebblockchain.api.resource

import com.google.gson.Gson
import com.vds.wishow.kwebblockchain.api.dto.UserLoginDTO
import com.vds.wishow.kwebblockchain.api.dto.UserRegisterDTO
import com.vds.wishow.kwebblockchain.api.dto.WiuserDTO
import com.vds.wishow.kwebblockchain.bootstrap.Utils.ERROR_USER_NOT_FOUND
import com.vds.wishow.kwebblockchain.bootstrap.Utils.ERROR_USER_NOT_LOGGED
import com.vds.wishow.kwebblockchain.bootstrap.Utils.TITLE_HOME
import com.vds.wishow.kwebblockchain.bootstrap.Utils.TITLE_LOGIN
import com.vds.wishow.kwebblockchain.bootstrap.Utils.TITLE_REGISTER
import com.vds.wishow.kwebblockchain.bootstrap.Utils.TITLE_WALLET
import com.vds.wishow.kwebblockchain.bootstrap.Utils.URL_AUTH_TOKEN
import com.vds.wishow.kwebblockchain.bootstrap.Utils.URL_AUTH_USER
import com.vds.wishow.kwebblockchain.bootstrap.Utils.hash
import com.vds.wishow.kwebblockchain.domain.model.Wiuser
import com.vds.wishow.kwebblockchain.domain.service.WiuserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
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
            model["errorMessage"] = ERROR_USER_NOT_FOUND
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
    fun handleRegister(userRegisterDTO: UserRegisterDTO): ModelAndView {
        service.save(
            Wiuser(
                email = hash(userRegisterDTO.email),
                password = hash(userRegisterDTO.password),
                username = userRegisterDTO.username
            )
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
    fun home(
        request: HttpServletRequest,
        model: MutableMap<String, Any>,
        attributes: RedirectAttributes
    ): ModelAndView {
        return handleGetRequest(request, model, attributes, "home", TITLE_HOME)
    }

    @GetMapping("/wallet")
    fun wallet(
        request: HttpServletRequest,
        model: MutableMap<String, Any>,
        attributes: RedirectAttributes
    ): ModelAndView {
        return handleGetRequest(request, model, attributes, "wallet", TITLE_WALLET)
    }

    private fun handleGetRequest(
        request: HttpServletRequest,
        model: MutableMap<String, Any>,
        attributes: RedirectAttributes,
        viewName: String,
        viewTitle: String,
    ): ModelAndView {
        val cookie = WebUtils.getCookie(request, "jws")
        val response: ResponseEntity<Any>

        if (cookie != null) {
            return try {
                val params: MutableMap<String, String> = mutableMapOf()
                params["jws"] = cookie.value
                response = getUserDetails(params)

                if (response.statusCodeValue == 200) {
                    val wiuserDTO = Gson().fromJson("${response.body}", WiuserDTO::class.java)
                    model["title"] = viewTitle
                    model["username"] = wiuserDTO.username
                    model["id"] = wiuserDTO.id
                    ModelAndView(viewName, model)
                } else {
                    errorView("${response.statusCodeValue}: ${response.statusCode}", attributes)
                }
            } catch (e: Exception) {
                errorView(e.message!!, attributes)
            }
        }
        return errorView(ERROR_USER_NOT_LOGGED, attributes)
    }

    private fun getUserToken(params: MutableMap<String, Long>): ResponseEntity<String> =
        RestTemplate().getForEntity("$URL_AUTH_TOKEN/{id}", String::class.java, params)

    private fun getUserDetails(params: MutableMap<String, String>): ResponseEntity<Any> =
        RestTemplate().getForEntity("$URL_AUTH_USER/{jws}", Any::class.java, params)

    private fun errorView(message: String, attributes: RedirectAttributes): ModelAndView {
        attributes.addFlashAttribute("errorMessage", message)
        attributes.addFlashAttribute("title", message)
        return ModelAndView("redirect:error")
    }
}