package com.vds.wishow.kwebblockchain.api.dto

import com.vds.wishow.kwebblockchain.domain.model.Wiuser
import com.vds.wishow.kwebblockchain.security.DataEncoder.encode

data class WiuserRegisterDTO(val username: String, val email: String, val password: String) {

    fun toDomain() = Wiuser(username = username, email = encode(email), password = encode(password))
}
