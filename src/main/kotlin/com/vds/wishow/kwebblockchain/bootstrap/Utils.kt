package com.vds.wishow.kwebblockchain.bootstrap

import java.security.MessageDigest

object Utils {
    fun hash(algorithm: String, stringToHash: String): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(stringToHash.toByteArray())
            .fold("") { acc, byte -> acc + "%02x".format(byte) }
    }
}