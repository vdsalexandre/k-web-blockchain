package com.vds.wishow.kwebblockchain.repository

import com.vds.wishow.kwebblockchain.model.Wiuser
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WiuserRepository : CrudRepository<Wiuser, Long> {

    fun findByEmailAndPassword(email: String, password: String): Wiuser?
}