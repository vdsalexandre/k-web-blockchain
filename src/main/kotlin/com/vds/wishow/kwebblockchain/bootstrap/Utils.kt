package com.vds.wishow.kwebblockchain.bootstrap

import java.security.MessageDigest

object Utils {
    const val USER_NOT_FOUND = "User not found - wrong email and/or password"

    const val LOGIN_TITLE = "Login - Wicoin Blockchain"
    const val REGISTER_TITLE = "Register - Wicoin Blockchain"
    const val HOME_TITLE = "Home - Wicoin Blockchain"

    fun hash(algorithm: String, stringToHash: String): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(stringToHash.toByteArray())
            .fold("") { acc, byte -> acc + "%02x".format(byte) }
    }
}