package com.vds.wishow.kwebblockchain.domain.model

import com.vds.wishow.kwebblockchain.bootstrap.Variables.ERROR_MESSAGE_BLOCK_IS_FULL
import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils.getMaxTransactionsLimitPerBlock
import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils.hash
import com.vds.wishow.kwebblockchain.domain.error.BlockException
import java.time.Instant

data class Block(
    val previousHash: String = "0",
    val datetime: Long = Instant.now().toEpochMilli(),
    private val data: MutableList<Transaction> = mutableListOf(),
    val nonce: Long = 0,
    var hash: String = ""
) {
    init {
        hash = hash(stringToHash = "$previousHash$data$datetime$nonce")
    }

    fun getData() = data

    fun addAll(transactions: List<Transaction>) {
        for (transaction in transactions) {
            add(transaction)
        }
    }

    fun add(transaction: Transaction) {
        if (data.size < getMaxTransactionsLimitPerBlock()) {
            data.add(transaction)
        } else {
            throw BlockException(ERROR_MESSAGE_BLOCK_IS_FULL)
        }
    }

    fun isFull() = data.size == getMaxTransactionsLimitPerBlock()

    override fun toString(): String {
        return "Block(previousHash=$previousHash, " +
                "datetime=$datetime, " +
                "nonce=$nonce, " +
                "hash=$hash, " +
                "data=${getData()})"
    }
}
