package com.vds.wishow.kwebblockchain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class KWebBlockchainApplication

fun main(args: Array<String>) {
	runApplication<KWebBlockchainApplication>(*args)
}
