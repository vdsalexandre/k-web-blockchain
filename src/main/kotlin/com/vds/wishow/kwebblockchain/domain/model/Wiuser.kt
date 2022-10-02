package com.vds.wishow.kwebblockchain.domain.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Wiuser(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val username: String,
    val email: String,
    val password: String
) {
    override fun toString() = "Wiuser(id=$id, username=$username, email=********, password=********)"
}
