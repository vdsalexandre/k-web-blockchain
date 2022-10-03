package com.vds.wishow.kwebblockchain.api.resource

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
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthResource(val service: WiuserService) {

    private val keyPair = Keys.keyPairFor(SignatureAlgorithm.PS512)

    @GetMapping("/user/{jws}")
    fun userDetails(@PathVariable jws: String?): ResponseEntity<Any> {
        if (jws != null && verifyJWS(keyPair.private, jws)) {
            val issuer = extractIssuer(keyPair.private, jws)
            return ResponseEntity.ok(service.findById(issuer))
        }
        return ResponseEntity<Any>("JWT signature does not match locally computed signature", HttpStatus.UNAUTHORIZED)
    }

    @GetMapping("/token/{id}")
    fun userToken(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<String> {
        val wiuser = service.findById(id)

        return if (wiuser != null)
            ResponseEntity.ok(generateJWS(keyPair.private, wiuser))
        else
            ResponseEntity("User not found", HttpStatus.NOT_FOUND)
    }
}