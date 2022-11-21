package com.vds.wishow.kwebblockchain.domain.model

import com.vds.wishow.kwebblockchain.bootstrap.HashUtils.hash
import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils.generateKeyPair
import java.math.BigDecimal
import java.security.PrivateKey
import java.security.PublicKey
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Wallet(
    @Id val walletId: String = "WICOIN" + hash(),
    var publicKey: PublicKey? = null,
    private var privateKey: PrivateKey? = null,
    var wiuserId: Long? = null
) {
    init {
        val keyPair = generateKeyPair()
        publicKey = keyPair.public
        privateKey = keyPair.private
    }

    fun getBalance(): BigDecimal = BigDecimal.ZERO
}
