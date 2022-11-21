package com.vds.wishow.kwebblockchain.domain.bootstrap

import java.security.KeyPair
import java.security.KeyPairGenerator

object BlockchainUtils {
    private const val KEYPAIR_ALGORITHM = "RSA"
    private const val KEY_SIZE = 4096

    fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(KEYPAIR_ALGORITHM)
        keyPairGenerator.initialize(KEY_SIZE)
        return keyPairGenerator.generateKeyPair()
    }
}