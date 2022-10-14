package com.vds.wishow.kwebblockchain.api.resource

import com.vds.wishow.kwebblockchain.api.dto.WiuserDTO.Companion.toWiuserDTO
import com.vds.wishow.kwebblockchain.bootstrap.JwsUtils.extractIssuer
import com.vds.wishow.kwebblockchain.bootstrap.JwsUtils.generateJWS
import com.vds.wishow.kwebblockchain.bootstrap.JwsUtils.verifyJWS
import com.vds.wishow.kwebblockchain.domain.service.WiuserService
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthResource(val service: WiuserService) {

    private val keyPair = Keys.keyPairFor(SignatureAlgorithm.PS512)

    @GetMapping("/user/{jws}")
    fun getUserDetails(@PathVariable jws: String): ResponseEntity<Any> {
        if (verifyJWS(keyPair.private, jws)) {
            val issuer = extractIssuer(keyPair.private, jws)
            val wiuser = service.findById(issuer)

            return if (wiuser != null)
                ResponseEntity.ok(wiuser.toWiuserDTO())
            else
                ResponseEntity(HttpStatus.UNAUTHORIZED.reasonPhrase, HttpStatus.UNAUTHORIZED)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED.reasonPhrase, HttpStatus.UNAUTHORIZED)
    }

    @GetMapping("/token/{id}")
    fun generateUserToken(@PathVariable id: Long): ResponseEntity<String> {
        val wiuser = service.findById(id)

        return if (wiuser != null)
            ResponseEntity.ok(generateJWS(keyPair.private, wiuser.toWiuserDTO()))
        else
            ResponseEntity(HttpStatus.NOT_FOUND.reasonPhrase, HttpStatus.NOT_FOUND)
    }
}
