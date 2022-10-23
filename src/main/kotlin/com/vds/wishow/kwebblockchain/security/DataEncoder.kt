package com.vds.wishow.kwebblockchain.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object DataEncoder {
    fun encode(data: String): String = BCryptPasswordEncoder(14).encode(data)

    fun matches(clearString: String, encodedString: String) =
        BCryptPasswordEncoder(14).matches(clearString, encodedString)
}