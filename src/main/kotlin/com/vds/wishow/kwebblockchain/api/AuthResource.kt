package com.vds.wishow.kwebblockchain.api

import com.vds.wishow.kwebblockchain.bootstrap.Utils
import com.vds.wishow.kwebblockchain.bootstrap.Utils.extractIssuerValueFromBody
import com.vds.wishow.kwebblockchain.model.Wiuser
import com.vds.wishow.kwebblockchain.service.WiuserService
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthResource(val service: WiuserService) {

    private val keyPair = Keys.keyPairFor(SignatureAlgorithm.PS512)

    @GetMapping("/user/{jws}")
    fun userDetails(@PathVariable jws: String?): ResponseEntity<Wiuser?> {
        if (jws != null) {
            val body = Utils.extractJWSBody(jws, keyPair.private)
            var id = -1L
            if (body.isNotBlank()) {
                id = extractIssuerValueFromBody(body)
            }
            return ResponseEntity.ok(service.findById(id))
        }
        return ResponseEntity.badRequest().body(null)
    }

    @GetMapping("/token/{id}")
    fun userToken(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<String> {
        val jws = Utils.generateJWS(keyPair.private, "$id")
        return ResponseEntity.ok(jws)
    }
}