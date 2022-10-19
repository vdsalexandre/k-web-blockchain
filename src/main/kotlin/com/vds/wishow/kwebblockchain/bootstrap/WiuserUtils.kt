package com.vds.wishow.kwebblockchain.bootstrap

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.security.MessageDigest
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

object WiuserUtils {
    const val ERROR_USER_NOT_FOUND = "User not found - wrong email and/or password"
    const val ERROR_USER_NOT_LOGGED = "Authentication failed, you must log-in to access this area"

    const val TITLE_LOGIN = "Login - Wicoin Blockchain"
    const val TITLE_REGISTER = "Register - Wicoin Blockchain"
    const val TITLE_HOME = "Home - Wicoin Blockchain"
    const val TITLE_WALLET = "Wallet - Wicoin Blockchain"

    private const val URL_AUTH_TOKEN = "http://localhost:9090/auth/token"
    private const val URL_AUTH_USER = "http://localhost:9090/auth/user"

    fun hash(stringToHash: String, algorithm: String = "SHA-512"): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(stringToHash.toByteArray())
            .fold("") { acc, byte -> acc + "%02x".format(byte) }
    }

    fun getUserToken(id: Long) =
        RestTemplate().getForEntity("$URL_AUTH_TOKEN/{id}", Any::class.java, mutableMapOf("id" to id))

    fun createAuthCookie(response: HttpServletResponse, body: String?) {
        val cookie = Cookie("jws", body)
        cookie.isHttpOnly = true // browser can't read the cookie, just for the backend
        response.addCookie(cookie)
    }

    fun deleteAuthCookie(response: HttpServletResponse) {
        val cookie = Cookie("jws", "")
        cookie.maxAge = 0
        response.addCookie(cookie)
    }

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
        return ResponseEntity("${status.value()} - ${status.reasonPhrase}", status)
    }
}
