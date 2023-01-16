package com.vds.wishow.kwebblockchain.domain.model

import com.vds.wishow.kwebblockchain.bootstrap.AuthUtils.keyToString
import com.vds.wishow.kwebblockchain.bootstrap.QrCodeUtils.generateQrCodeFromData
import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils.generateKeyPair
import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils.hash
import org.hibernate.annotations.Type
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob

@Entity
data class Wallet(
    @Id val walletId: String = "WIC" + hash(),
    @Lob var publicKey: String? = null,
    @Lob private var privateKey: String? = null,
    @Lob @Type(type = "org.hibernate.type.ImageType") var qrCode: ByteArray = byteArrayOf(),
    var wiuserId: Long
) {
    init {
        val keyPair = generateKeyPair()
        publicKey = keyToString(keyPair.public)
        privateKey = keyToString(keyPair.private)
        qrCode = generateQrCodeFromData(walletId)
    }

    fun getBalance(): BigDecimal = BigDecimal.ZERO

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Wallet

        if (walletId != other.walletId) return false
        if (publicKey != other.publicKey) return false
        if (privateKey != other.privateKey) return false
        if (!qrCode.contentEquals(other.qrCode)) return false
        if (wiuserId != other.wiuserId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = walletId.hashCode()
        result = 31 * result + (publicKey?.hashCode() ?: 0)
        result = 31 * result + (privateKey?.hashCode() ?: 0)
        result = 31 * result + qrCode.contentHashCode()
        result = 31 * result + wiuserId.hashCode()
        return result
    }
}
