package com.vds.wishow.kwebblockchain.bootstrap

import io.jsonwebtoken.Jwts
import java.security.MessageDigest
import java.security.PrivateKey
import java.util.Date


object Utils {
    const val USER_NOT_FOUND = "User not found - wrong email and/or password"
    const val WRONG_AUTH_BAD_TOKEN = "Authentication token invalid"
    const val WRONG_AUTH_NOT_LOGGED = "Authentication failed, you must log-in to access this area"

    const val TITLE_LOGIN = "Login - Wicoin Blockchain"
    const val TITLE_REGISTER = "Register - Wicoin Blockchain"
    const val TITLE_HOME = "Home - Wicoin Blockchain"
    const val TITLE_WALLET = "Wallet - Wicoin Blockchain"

    const val URL_AUTH_TOKEN = "http://localhost:9090/auth/token"
    const val URL_AUTH_USER = "http://localhost:9090/auth/user"

    private const val ONE_DAY: Long = 1000 * 60 * 24

    fun hash(stringToHash: String, algorithm: String = "SHA-512"): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(stringToHash.toByteArray())
            .fold("") { acc, byte -> acc + "%02x".format(byte) }
    }

    fun generateJWS(privateKey: PrivateKey, issuer: String): String {
        return Jwts
            .builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + ONE_DAY))
            .signWith(privateKey)
            .compact()
    }

    fun verifyJWSAndExtractIssuer(jws: String, privateKey: PrivateKey): Long? {
        return try {
            Jwts
                .parserBuilder()
                .setSigningKey(privateKey)
                .build()
                .parse(jws)
                .body
                .toString()
                .extractIssuerFromBody()
        } catch (e: Exception) {
            null
        }
    }

    private fun String.extractIssuerFromBody(): Long {
        return this
            .substringAfter("iss=")
            .substringBefore(",")
            .toLong()
    }
}
