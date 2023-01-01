package com.vds.wishow.kwebblockchain.bootstrap

import com.vds.wishow.kwebblockchain.api.dto.WiuserLoginDTO
import com.vds.wishow.kwebblockchain.bootstrap.Variables.URL_AUTH_TOKEN
import com.vds.wishow.kwebblockchain.bootstrap.Variables.URL_AUTH_USER
import com.vds.wishow.kwebblockchain.bootstrap.Variables.URL_AUTH_WALLET
import org.springframework.web.client.RestTemplate

object WiuserUtils {

    fun getUserToken(wiuserLoginDTO: WiuserLoginDTO) =
        RestTemplate().postForEntity(URL_AUTH_TOKEN, wiuserLoginDTO, Any::class.java)

    fun getUserDetails(jws: String) =
        RestTemplate().getForEntity("$URL_AUTH_USER/{jws}", Any::class.java, mutableMapOf("jws" to jws))

    fun getQrCodeFromWallet(walletId: String) =
        RestTemplate().getForEntity("$URL_AUTH_WALLET/{walletId}", Any::class.java, mutableMapOf("walletId" to walletId))

}
