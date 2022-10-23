package com.vds.wishow.kwebblockchain.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

private const val BCRYPT_STRENGTH = 14

object DataEncoder {
    fun encode(data: String): String = BCryptPasswordEncoder(BCRYPT_STRENGTH).encode(data)

    fun matches(clearString: String, encodedString: String) =
        BCryptPasswordEncoder(BCRYPT_STRENGTH).matches(clearString, encodedString)
}