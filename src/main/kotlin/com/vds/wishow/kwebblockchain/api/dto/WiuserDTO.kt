package com.vds.wishow.kwebblockchain.api.dto

import com.vds.wishow.kwebblockchain.domain.model.Wiuser

data class WiuserDTO(val id: Long, val username: String) {
    companion object {
        fun Wiuser.toWiuserDTO(): WiuserDTO {
            return WiuserDTO(this.id!!, this.username)
        }
    }
}
