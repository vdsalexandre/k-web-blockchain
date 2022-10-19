package com.vds.wishow.kwebblockchain.api.dto

import com.vds.wishow.kwebblockchain.bootstrap.WiuserUtils.hash
import com.vds.wishow.kwebblockchain.domain.model.Wiuser

data class UserRegisterDTO(val username: String, val email: String, val password: String) {

    fun toDomain() = Wiuser(username = username, email = hash(email), password = hash(password))
}
