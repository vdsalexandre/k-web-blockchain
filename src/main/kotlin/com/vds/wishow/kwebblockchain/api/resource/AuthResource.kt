package com.vds.wishow.kwebblockchain.api.resource

import com.vds.wishow.kwebblockchain.bootstrap.Utils.generateJWS
import com.vds.wishow.kwebblockchain.bootstrap.Utils.verifyJWSAndExtractIssuer
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
        if (jws != null) {
            val issuer = verifyJWSAndExtractIssuer(jws, keyPair.private)
            if (issuer != null) {
                return ResponseEntity.ok(service.findById(issuer))
            }
            return ResponseEntity<Any>("User not found", HttpStatus.NOT_FOUND)
        }
        return ResponseEntity<Any>("JWT signature does not match locally computed signature", HttpStatus.UNAUTHORIZED)
    }

    @GetMapping("/token/{id}")
    fun userToken(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<String> {
        return ResponseEntity.ok(generateJWS(keyPair.private, "$id"))
    }
}