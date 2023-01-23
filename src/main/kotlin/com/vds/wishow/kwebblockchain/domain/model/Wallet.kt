package com.vds.wishow.kwebblockchain.domain.model

import com.vds.wishow.kwebblockchain.bootstrap.QrCodeUtils.generateQrCodeFromData
import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils.generatePrivateKey
import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils.hash
import org.hibernate.annotations.Type
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob

@Entity
data class Wallet(
    @Id val walletId: String = "WIC" + hash(),
    private var privateKey: String = "",
    @Lob @Type(type = "org.hibernate.type.ImageType") var qrCode: ByteArray = byteArrayOf(),
    var wiuserId: Long
) {
    init {
        qrCode = generateQrCodeFromData(walletId)
    }

    fun setPrivateKey(privateWords: Set<String>) {
        privateKey = generatePrivateKey(privateWords, walletId)
    }

    fun getBalance(): BigDecimal = BigDecimal.ZERO

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Wallet

        if (walletId != other.walletId) return false
        if (privateKey != other.privateKey) return false
        if (!qrCode.contentEquals(other.qrCode)) return false
        if (wiuserId != other.wiuserId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = walletId.hashCode()
        result = 31 * result + privateKey.hashCode()
        result = 31 * result + qrCode.contentHashCode()
        result = 31 * result + wiuserId.hashCode()
        return result
    }
}
