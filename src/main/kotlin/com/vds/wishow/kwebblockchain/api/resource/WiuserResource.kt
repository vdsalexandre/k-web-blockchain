package com.vds.wishow.kwebblockchain.api.resource

import com.google.gson.Gson
import com.vds.wishow.kwebblockchain.api.dto.WalletDTO.Companion.toDto
import com.vds.wishow.kwebblockchain.api.dto.WiuserDTO
import com.vds.wishow.kwebblockchain.api.dto.WiuserLoginDTO
import com.vds.wishow.kwebblockchain.api.dto.WiuserRegisterDTO
import com.vds.wishow.kwebblockchain.bootstrap.AuthUtils.createAuthCookie
import com.vds.wishow.kwebblockchain.bootstrap.AuthUtils.deleteAuthCookie
import com.vds.wishow.kwebblockchain.bootstrap.AuthUtils.isWiuserConnected
import com.vds.wishow.kwebblockchain.bootstrap.ErrorUtils.errorView
import com.vds.wishow.kwebblockchain.bootstrap.ErrorUtils.viewWithErrorMessage
import com.vds.wishow.kwebblockchain.bootstrap.Variables.ERROR_USER_NOT_EXISTS
import com.vds.wishow.kwebblockchain.bootstrap.Variables.ERROR_USER_NOT_FOUND
import com.vds.wishow.kwebblockchain.bootstrap.Variables.ERROR_USER_NOT_LOGGED
import com.vds.wishow.kwebblockchain.bootstrap.Variables.TITLE_HOME
import com.vds.wishow.kwebblockchain.bootstrap.Variables.TITLE_LOGIN
import com.vds.wishow.kwebblockchain.bootstrap.Variables.TITLE_REGISTER
import com.vds.wishow.kwebblockchain.bootstrap.Variables.TITLE_WALLET
import com.vds.wishow.kwebblockchain.bootstrap.WiuserUtils.getUserDetails
import com.vds.wishow.kwebblockchain.bootstrap.WiuserUtils.getUserToken
import com.vds.wishow.kwebblockchain.domain.model.Wallet
import com.vds.wishow.kwebblockchain.domain.service.WalletService
import com.vds.wishow.kwebblockchain.domain.service.WiuserService
import com.vds.wishow.kwebblockchain.security.AuthResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import org.springframework.web.util.WebUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/blockchain")
class WiuserResource(val wiuserService: WiuserService, val walletService: WalletService) {

    @GetMapping("/**")
    fun redirect(request: HttpServletRequest): RedirectView {
        val cookie = getJwsCookie(request)

        return if (cookie != null) {
            if (isWiuserConnected(cookie))
                RedirectView("home")
            else
                RedirectView("login")
        } else
            RedirectView("login")
    }

    @GetMapping("/login")
    fun showLogin(): ModelAndView {
        return ModelAndView("login", mutableMapOf("title" to TITLE_LOGIN))
    }

    @PostMapping("/login")
    fun handleLogin(wiuserLoginDTO: WiuserLoginDTO, attributes: RedirectAttributes, response: HttpServletResponse): Any {
        return try {
            val responseEntity = getUserToken(wiuserLoginDTO)

            if (responseEntity.statusCode == HttpStatus.OK) {
                val body = extractBody(responseEntity, AuthResponse::class.java)
                createAuthCookie(response, body.jws)
                attributes.addFlashAttribute("username", body.username)
                RedirectView("home")
            } else {
                viewWithErrorMessage("login", TITLE_LOGIN, ERROR_USER_NOT_FOUND)
            }
        } catch (e: HttpClientErrorException) {
            viewWithErrorMessage("login", TITLE_LOGIN, ERROR_USER_NOT_EXISTS)
        }
    }

    @GetMapping("/register")
    fun showRegister(): ModelAndView {
        return ModelAndView("register", mutableMapOf("title" to TITLE_REGISTER))
    }

    @PostMapping("/register")
    fun handleRegister(wiuserRegisterDTO: WiuserRegisterDTO): RedirectView {
        wiuserService.save(wiuserRegisterDTO.toDomain())
        return RedirectView("login")
    }

    @PostMapping("/logout")
    fun handleLogout(response: HttpServletResponse): RedirectView {
        deleteAuthCookie(response)
        return RedirectView("login")
    }

    @GetMapping("/home")
    fun home(request: HttpServletRequest, attributes: RedirectAttributes): ModelAndView {
        return handleRequest(request, attributes, "home", TITLE_HOME)
    }

    @GetMapping("/wallet")
    fun showWallet(request: HttpServletRequest, attributes: RedirectAttributes): ModelAndView {
        return handleRequest(request, attributes, "wallet", TITLE_WALLET)
    }

    @PostMapping("/wallet")
    fun createWallet(request: HttpServletRequest, attributes: RedirectAttributes): ModelAndView {
        return handleRequest(request, attributes, "wallet", TITLE_WALLET, "new-wallet")
    }

    private fun handleRequest(request: HttpServletRequest, attributes: RedirectAttributes, view: String, title: String, action: String = ""): ModelAndView {
        val model: MutableMap<String, Any> = mutableMapOf()
        val cookie = getJwsCookie(request)

        if (cookie != null) {
            return try {
                val response = getUserDetails(cookie.value)

                if (response.statusCode == HttpStatus.OK) {
                    val wiuserDTO = extractBody(response, WiuserDTO::class.java)
                    model["title"] = title
                    model["username"] = wiuserDTO.username
                    model["id"] = wiuserDTO.id
                    if (wiuserDTO.wallet != null) {
                        model["walletid"] = wiuserDTO.wallet.walletId
                        model["walletbalance"] = wiuserDTO.wallet.balance
                    } else {
                        if (action == "new-wallet") {
                            val newWallet = Wallet(wiuserId = wiuserDTO.id)
                            walletService.save(newWallet)
                            val walletDto = newWallet.toDto()
                            model["walletid"] = walletDto.walletId
                            model["walletbalance"] = walletDto.balance
                        }
                    }
                    ModelAndView(view, model)
                } else {
                    errorView("${response.statusCodeValue}: ${response.statusCode}", attributes)
                }
            } catch (e: Exception) {
                errorView(e.message!!, attributes)
            }
        }
        return errorView(ERROR_USER_NOT_LOGGED, attributes)
    }

    private fun <T> extractBody(responseEntity: ResponseEntity<Any>, clazz: Class<T>): T {
        return Gson().fromJson(responseEntity.body.toString(), clazz)
    }

    private fun getJwsCookie(request: HttpServletRequest) = WebUtils.getCookie(request, "jws")
}
