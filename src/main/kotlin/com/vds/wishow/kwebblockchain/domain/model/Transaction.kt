package com.vds.wishow.kwebblockchain.domain.model

import java.math.BigDecimal

data class Transaction(
    val sender: String,
    val receiver: String,
    val amount: BigDecimal
) {
//    fun getTransactionHash() = hash(stringToHash = "$sender$receiver$amount${randomSalt()}")
}
