package com.vds.wishow.kwebblockchain.api.dto

import com.vds.wishow.kwebblockchain.bootstrap.Utils.hash
import com.vds.wishow.kwebblockchain.domain.model.Wiuser

data class UserRegisterDTO(val username: String, val email: String, val password: String) {

    fun toWiuser() = Wiuser(username = username, email = hash(email), password = hash(password))
}
