package com.vds.wishow.kwebblockchain.bootstrap

import io.jsonwebtoken.Jwts
import org.springframework.http.HttpStatus
import java.security.Key
import java.security.PrivateKey
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Base64
import java.util.Date
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

object AuthUtils {

    fun generateJWS(privateKey: PrivateKey, wiuserId: Long): String {
        return Jwts
            .builder()
            .setIssuer(wiuserId.toString())
            .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
            .signWith(privateKey)
            .compact()
    }

    fun extractIssuer(privateKey: PrivateKey, jws: String): Long {
        return Jwts
            .parserBuilder()
            .setSigningKey(privateKey)
            .build()
            .parseClaimsJws(jws)
            .body
            .issuer
            .toLong()
    }

    fun verifyJWS(privateKey: PrivateKey, jws: String): Boolean {
        return try {
            Jwts
                .parserBuilder()
                .setSigningKey(privateKey)
                .build()
                .parseClaimsJws(jws)
                .body
            true
        } catch (e: Exception) {
            false
        }
    }

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

    fun isWiuserConnected(cookie: Cookie): Boolean {
        return try {
            val response = WiuserUtils.getUserDetails(cookie.value)
            response.statusCode == HttpStatus.OK
        } catch (e: Exception) {
            false
        }
    }

    fun keyToString(key: Key): String = Base64.getEncoder().encodeToString(key.encoded)
}