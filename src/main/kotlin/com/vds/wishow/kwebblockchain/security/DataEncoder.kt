package com.vds.wishow.kwebblockchain.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object DataEncoder {
    private val bcryptEncoder = BCryptPasswordEncoder(14)

    fun encode(data: String): String = bcryptEncoder.encode(data)

    fun matches(encodedString1: String, encodedString2: String) = bcryptEncoder.matches(encodedString1, encodedString2)
}