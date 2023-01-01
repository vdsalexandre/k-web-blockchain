package com.vds.wishow.kwebblockchain.api.dto

data class QrCodeFromWallet(val qrCode: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QrCodeFromWallet

        if (!qrCode.contentEquals(other.qrCode)) return false

        return true
    }

    override fun hashCode(): Int {
        return qrCode.contentHashCode()
    }
}
