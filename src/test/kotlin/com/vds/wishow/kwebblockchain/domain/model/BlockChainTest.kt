package com.vds.wishow.kwebblockchain.domain.model

import com.vds.wishow.kwebblockchain.domain.error.BlockException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal.valueOf

class BlockChainTest {
    private val firstWiuser = Wiuser(1234567890, "MartyFromTheFuture", "marty123456789", "marty.mcfly@past.com")
    private val secondWiuser = Wiuser(1122334455, "DocFromThePast", "doc123456789", "doc.brown@future.com")
    private val firstWallet = Wallet(wiuserId = firstWiuser.id!!)
    private val secondWallet = Wallet(wiuserId = secondWiuser.id!!)
    private val maximumTransactions = mutableListOf(
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(1)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(2)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(3)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(4)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(5)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(6)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(7)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(8)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(9)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(10))
    )
    private val transactions = mutableListOf(
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(1)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(2)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(3)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(4)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(5)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(6)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(7)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(8)),
        Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(9))
    )

    @Test
    fun `the new transaction should be present in the block`() {
        val transaction = Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(12))
        val block = Block()

        block.add(transaction)

        assertThat(block.data).contains(transaction)
    }

    @Test
    fun `the new transaction should be added if block is not full`() {
        val block = Block()

        block.addAll(transactions)
        val newTransaction = Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(10))
        block.add(newTransaction)

        assertThat(block.data).contains(newTransaction)
    }

    @Test
    fun `the new transaction should not be added if block is full`() {
        val block = Block()

        block.addAll(maximumTransactions)
        val newTransaction = Transaction(firstWallet.walletId, secondWallet.walletId, valueOf(100))

        assertThrows<BlockException>(message = "The block is full, can't add more transactions") {
            block.add(newTransaction)
        }
    }

    @Test
    fun `when a block is full it should be added to the blockchain`() {
        val blockchain = Blockchain(difficulty = 1)
        val block = Block()

        block.addAll(maximumTransactions)
        val minedBlock = blockchain.add(block)

        assertThat(minedBlock?.let { blockchain.contains(it) }).isTrue
        assertThat(blockchain.size()).isEqualTo(1)
    }

    @Test
    fun `when a block is not full it should not be added to the blockchain`() {
        val blockchain = Blockchain(difficulty = 1)
        val block = Block()

        block.addAll(transactions)
        val minedBlock = blockchain.add(block)

        assertThat(minedBlock).isNull()
        assertThat(blockchain.size()).isEqualTo(0)
    }
}