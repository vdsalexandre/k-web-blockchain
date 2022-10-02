package com.vds.wishow.kwebblockchain.bootstrap

import io.jsonwebtoken.Jwts
import java.security.MessageDigest
import java.security.PrivateKey
import java.util.Date


object Utils {
    const val USER_NOT_FOUND = "User not found - wrong email and/or password"
    const val WRONG_AUTH_TOKEN = "Authentication token invalid"

    const val TITLE_LOGIN = "Login - Wicoin Blockchain"
    const val TITLE_REGISTER = "Register - Wicoin Blockchain"
    const val TITLE_HOME = "Home - Wicoin Blockchain"
    const val TITLE_WALLET = "Wallet - Wicoin Blockchain"

    const val URL_AUTH_TOKEN = "http://localhost:9090/auth/token"
    const val URL_AUTH_USER = "http://localhost:9090/auth/user"

    fun hash(algorithm: String, stringToHash: String): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(stringToHash.toByteArray())
            .fold("") { acc, byte -> acc + "%02x".format(byte) }
    }

    fun generateJWS(privateKey: PrivateKey, issuer: String): String {
        return Jwts
            .builder()
            .setIssuer(issuer)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .signWith(privateKey)
            .compact()
    }

    fun extractJWSBody(jws: String, privateKey: PrivateKey): String {
        return Jwts
                .parserBuilder()
                .setSigningKey(privateKey)
                .build()
                .parse(jws)
                .body
                .toString()
    }

    fun extractIssuerValueFromBody(body: String) = body.substringAfter("iss=").substringBefore(",").toLong()
}
