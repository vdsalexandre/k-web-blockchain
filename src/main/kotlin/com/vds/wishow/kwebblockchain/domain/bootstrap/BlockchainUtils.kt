package com.vds.wishow.kwebblockchain.domain.bootstrap

import com.vds.wishow.kwebblockchain.bootstrap.Variables
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.MessageDigest
import kotlin.random.Random

object BlockchainUtils {
    private const val KEYPAIR_ALGORITHM = "RSA"
    private const val KEY_SIZE = 4096

    fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(KEYPAIR_ALGORITHM)
        keyPairGenerator.initialize(KEY_SIZE)
        return keyPairGenerator.generateKeyPair()
    }

    fun hash(algorithm: String = "SHA-256", stringToHash: String = generateRandomString()): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(stringToHash.toByteArray())
            .fold("") { acc, byte -> acc + "%02x".format(byte) }
    }

    private fun generateRandomString(length: Int = Variables.RANDOM_STRING_LENGTH): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}