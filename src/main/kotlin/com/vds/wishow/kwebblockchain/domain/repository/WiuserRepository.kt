package com.vds.wishow.kwebblockchain.domain.repository

import com.vds.wishow.kwebblockchain.domain.model.Wiuser
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WiuserRepository : CrudRepository<Wiuser, Long> {

    fun findByEmailAndPassword(email: String, password: String): Wiuser?
}