package com.vds.wishow.kwebblockchain.api.dto

import com.vds.wishow.kwebblockchain.domain.model.Wallet
import java.math.BigDecimal

data class WalletDTO(val walletId: String, val balance: BigDecimal, val wiuserId: Long) {

    companion object {
        fun Wallet?.toDtoOrNull(): WalletDTO? {
            return if (this != null) {
                WalletDTO(this.walletId, this.getBalance(), this.wiuserId!!)
            } else {
                null
            }
        }

        fun Wallet.toDto() = WalletDTO(this.walletId, this.getBalance(), this.wiuserId!!)
    }
}
