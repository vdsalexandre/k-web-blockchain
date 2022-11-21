package com.vds.wishow.kwebblockchain.bootstrap

import com.vds.wishow.kwebblockchain.bootstrap.Variables.RANDOM_STRING_LENGTH
import java.security.MessageDigest
import kotlin.random.Random

object HashUtils {

    fun hash(algorithm: String = "SHA-256", stringToHash: String = generateRandomString()): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(stringToHash.toByteArray())
            .fold("") { acc, byte -> acc + "%02x".format(byte) }
    }

    private fun generateRandomString(length: Int = RANDOM_STRING_LENGTH): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}