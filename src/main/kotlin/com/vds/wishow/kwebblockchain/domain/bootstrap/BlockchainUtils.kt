package com.vds.wishow.kwebblockchain.domain.bootstrap

import java.security.MessageDigest
import kotlin.random.Random

object BlockchainUtils {
    private const val MAX_TRANSACTIONS_PER_BLOCK = 10
    private const val HASH_ALGORITHM = "SHA-256"
    private const val RANDOM_STRING_LENGTH = 40
    private const val MINING_DIFFICULTY: Int = 5
    private val lowercaseUppercaseNumbers: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val uppercaseNumbers: List<Char> = ('A'..'Z') + ('0'..'9')
    private const val WORDS_FOR_PRIVATE_KEY = 8

    fun getDifficulty() = MINING_DIFFICULTY

    fun getMaxTransactionsLimitPerBlock() = MAX_TRANSACTIONS_PER_BLOCK

    fun hash(
        algorithm: String = HASH_ALGORITHM,
        stringToHash: String = generateRandomString(characters = lowercaseUppercaseNumbers)
    ): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(stringToHash.toByteArray())
            .fold("") { acc, byte -> acc + "%02x".format(byte) }
    }

    fun generatePrivateWords(): Set<String> {
        val words = mutableSetOf<String>()

        while (words.size < WORDS_FOR_PRIVATE_KEY) {
            words.add(generateRandomString(length = WORDS_FOR_PRIVATE_KEY, characters = uppercaseNumbers))
        }
        return words
    }

    fun generatePrivateKey(words: Set<String>, secret: String) =
        "WIC" + hash(algorithm = "SHA-512", stringToHash = words.joinToString() + secret)


    fun randomSalt() = Random.nextInt(0, Int.MAX_VALUE)

    private fun generateRandomString(length: Int = RANDOM_STRING_LENGTH, characters: List<Char>): String {
        return (1..length)
            .map { Random.nextInt(0, characters.size) }
            .map(characters::get)
            .joinToString("")
    }
}