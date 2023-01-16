package com.vds.wishow.kwebblockchain.domain.model

import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils
import com.vds.wishow.kwebblockchain.domain.error.BlockException
import io.mockk.every
import io.mockk.mockkObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal.valueOf

class BlockChainTest {
    private val firstWiuser = Wiuser(1234567890, "MartyFromTheFuture", "marty123456789", "marty.mcfly@past.com")
    private val secondWiuser = Wiuser(1122334455, "DocFromThePast", "doc123456789", "doc.brown@future.com")

    private val firstWallet = Wallet(wiuserId = firstWiuser.id!!)
    private val secondWallet = Wallet(wiuserId = secondWiuser.id!!)

    private val maximumTransactions = mutableListOf(
        Transaction(sender = firstWallet.walletId, receiver = secondWallet.walletId, amount = valueOf(1)),
        Transaction(sender = firstWallet.walletId, receiver = secondWallet.walletId, amount = valueOf(2)),
        Transaction(sender = firstWallet.walletId, receiver = secondWallet.walletId, amount = valueOf(3))
    )
    private val transactions = mutableListOf(
        Transaction(sender = firstWallet.walletId, receiver = secondWallet.walletId, amount = valueOf(1)),
        Transaction(sender = firstWallet.walletId, receiver = secondWallet.walletId, amount = valueOf(2))
    )

    @BeforeEach
    fun setUp() {
        mockkObject(BlockchainUtils)
        every { BlockchainUtils.getMaxTransactionsLimitPerBlock() } returns 3
    }

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
        assertThat(blockchain.size()).isEqualTo(2)
    }

    @Test
    fun `when a block is not full it should not be added to the blockchain`() {
        val blockchain = Blockchain(difficulty = 1)
        val block = Block()

        block.addAll(transactions)
        val minedBlock = blockchain.add(block)

        assertThat(minedBlock).isNull()
        assertThat(blockchain.size()).isEqualTo(1)
    }

    @Test
    fun `the first block added should have initial block hash property`() {
        val blockchain = Blockchain(difficulty = 1)
        val firstBlock = Block()

        firstBlock.addAll(maximumTransactions)
        blockchain.add(firstBlock)

        assertThat(blockchain.elementAt(1).previousHash).isEqualTo(blockchain.elementAt(0).hash)
    }

    @Test
    fun `the second block added should have previous hash block property`() {
        val blockchain = Blockchain(difficulty = 1)
        val firstBlock = Block()
        val secondBlock = Block()

        firstBlock.addAll(maximumTransactions)
        blockchain.add(firstBlock)
        secondBlock.addAll(transactions)
        secondBlock.add(Transaction(secondWallet.walletId, firstWallet.walletId, valueOf(500)))
        blockchain.add(secondBlock)

        assertThat(blockchain.elementAt(2).previousHash).isEqualTo(blockchain.elementAt(1).hash)
    }
}