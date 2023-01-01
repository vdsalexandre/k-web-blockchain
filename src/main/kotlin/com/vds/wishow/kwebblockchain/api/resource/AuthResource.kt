package com.vds.wishow.kwebblockchain.api.resource

import com.vds.wishow.kwebblockchain.api.dto.QrCodeDTO
import com.vds.wishow.kwebblockchain.api.dto.WalletDTO.Companion.toDtoOrNull
import com.vds.wishow.kwebblockchain.api.dto.WiuserDTO
import com.vds.wishow.kwebblockchain.api.dto.WiuserLoginDTO
import com.vds.wishow.kwebblockchain.bootstrap.AuthUtils.extractIssuer
import com.vds.wishow.kwebblockchain.bootstrap.AuthUtils.generateJWS
import com.vds.wishow.kwebblockchain.bootstrap.AuthUtils.verifyJWS
import com.vds.wishow.kwebblockchain.bootstrap.ErrorUtils.errorResponse
import com.vds.wishow.kwebblockchain.domain.service.WalletService
import com.vds.wishow.kwebblockchain.domain.service.WiuserService
import com.vds.wishow.kwebblockchain.security.AuthResponse
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthResource(val wiuserService: WiuserService, val walletService: WalletService) {

    private val keyPair = Keys.keyPairFor(SignatureAlgorithm.PS512)

    @GetMapping("/user/{jws}")
    fun getUserDetails(@PathVariable jws: String): ResponseEntity<Any> {
        return if (verifyJWS(keyPair.private, jws)) {
            val issuer = extractIssuer(keyPair.private, jws)
            val wiuser = wiuserService.findById(issuer)

            if (wiuser != null) {
                val wallet = walletService.findByWiuserId(wiuser.id!!)
                ResponseEntity.ok(WiuserDTO(wiuser.id, wiuser.username, wallet.toDtoOrNull()))
            } else
                errorResponse(HttpStatus.UNAUTHORIZED)
        } else
            errorResponse(HttpStatus.UNAUTHORIZED)
    }

    @PostMapping("/token")
    fun getUserToken(@RequestBody wiuserLoginDTO: WiuserLoginDTO): ResponseEntity<Any> {
        val wiuser = wiuserService.findWiuser(wiuserLoginDTO)

        return if (wiuser != null)
            ResponseEntity.ok(AuthResponse(generateJWS(keyPair.private, wiuser.id!!), wiuser.username))
        else
            errorResponse(HttpStatus.NOT_FOUND)
    }

    @GetMapping("/wallet/{walletId}")
    fun getQrCodeFromWallet(@PathVariable walletId: String): ResponseEntity<out Any> {
        val wallet = walletService.findById(walletId)

        return if (wallet != null) {
            ResponseEntity.ok(QrCodeDTO(qrCode = wallet.qrCode))
        } else {
            errorResponse(HttpStatus.NOT_FOUND)
        }
    }
}
