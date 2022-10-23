package com.vds.wishow.kwebblockchain.bootstrap

import io.jsonwebtoken.Jwts
import java.security.PrivateKey
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

object JwsUtils {

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
}