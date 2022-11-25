package com.vds.wishow.kwebblockchain.api.dto

import com.vds.wishow.kwebblockchain.domain.model.Wallet
import java.math.BigDecimal

data class WalletDTO(val walletId: String, val balance: BigDecimal, val wiuserId: Long) {

    companion object {
        fun Wallet.toDto() = WalletDTO(this.walletId, this.getBalance(), this.wiuserId)

        fun Wallet?.toDtoOrNull() = this?.toDto()
    }
}
