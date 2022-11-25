package com.vds.wishow.kwebblockchain.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

private val bcryptEncoder = BCryptPasswordEncoder(14)

object DataEncoder {
    fun encode(data: String): String = bcryptEncoder.encode(data)
    fun matches(clearString: String, encodedString: String) = bcryptEncoder.matches(clearString, encodedString)
}