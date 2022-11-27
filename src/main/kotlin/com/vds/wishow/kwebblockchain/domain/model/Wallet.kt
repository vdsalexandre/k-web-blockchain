package com.vds.wishow.kwebblockchain.domain.model

import com.vds.wishow.kwebblockchain.bootstrap.AuthUtils.keyToString
import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils.generateKeyPair
import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils.hash
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob

@Entity
data class Wallet(
    @Id val walletId: String = "WIC" + hash(),
    @Lob var publicKey: String? = null,
    @Lob private var privateKey: String? = null,
    var wiuserId: Long
) {
    init {
        val keyPair = generateKeyPair()
        publicKey = keyToString(keyPair.public)
        privateKey = keyToString(keyPair.private)
    }

    fun getBalance(): BigDecimal = BigDecimal.ZERO
}
