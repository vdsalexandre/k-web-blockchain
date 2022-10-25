package com.vds.wishow.kwebblockchain.bootstrap

import com.vds.wishow.kwebblockchain.api.dto.WiuserLoginDTO
import com.vds.wishow.kwebblockchain.bootstrap.Variables.URL_AUTH_TOKEN
import com.vds.wishow.kwebblockchain.bootstrap.Variables.URL_AUTH_USER
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

object WiuserUtils {

    fun createAuthCookie(response: HttpServletResponse, token: String) {
        val cookie = Cookie("jws", token)
        cookie.isHttpOnly = true
        response.addCookie(cookie)
    }

    fun deleteAuthCookie(response: HttpServletResponse) {
        val cookie = Cookie("jws", "")
        cookie.maxAge = 0
        response.addCookie(cookie)
    }

    fun getUserToken(wiuserLoginDTO: WiuserLoginDTO) =
        RestTemplate().postForEntity(URL_AUTH_TOKEN, wiuserLoginDTO, Any::class.java)

    fun getUserDetails(jws: String) =
        RestTemplate().getForEntity("$URL_AUTH_USER/{jws}", Any::class.java, mutableMapOf("jws" to jws))

    fun isWiuserConnected(cookie: Cookie): Boolean {
        return try {
            val response = getUserDetails(cookie.value)
            response.statusCode == HttpStatus.OK
        } catch (e: Exception) {
            false
        }
    }

    fun errorResponse(status: HttpStatus): ResponseEntity<Any> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        return ResponseEntity("${status.value()} - ${status.reasonPhrase}", headers, status)
    }
}
