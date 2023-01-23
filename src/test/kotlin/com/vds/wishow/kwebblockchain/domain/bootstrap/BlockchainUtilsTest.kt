package com.vds.wishow.kwebblockchain.domain.bootstrap

import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils.generatePrivateKey
import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils.generatePrivateWords
import com.vds.wishow.kwebblockchain.domain.model.Wallet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BlockchainUtilsTest {

    @Test
    fun `generatePrivateKey should generate the same private key from the same 8 random alphanumeric words`() {
        val words = generatePrivateWords()
        val wallet = Wallet(wiuserId = 1)
        val privateKey1 = generatePrivateKey(words = words, secret = wallet.walletId)
        val privateKey2 = generatePrivateKey(words = words, secret = wallet.walletId)

        assertThat(privateKey1).isEqualTo(privateKey2)
    }

    @Test
    fun `generatePrivateKey should generate two private keys from 8 different random alphanumeric words`() {
        val words1 = generatePrivateWords()
        val words2 = generatePrivateWords()
        val wallet1 = Wallet(wiuserId = 1)
        val wallet2 = Wallet(wiuserId = 2)
        val privateKey1 = generatePrivateKey(words = words1, secret = wallet1.walletId)
        val privateKey2 = generatePrivateKey(words = words2, secret = wallet2.walletId)

        assertThat(privateKey1).isNotEqualTo(privateKey2)
    }
}