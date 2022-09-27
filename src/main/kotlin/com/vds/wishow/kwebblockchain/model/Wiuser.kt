package com.vds.wishow.kwebblockchain.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Wiuser(
    @Id @GeneratedValue val id: Long,
    val username: String,
    val email: String,
    val password: String
)
