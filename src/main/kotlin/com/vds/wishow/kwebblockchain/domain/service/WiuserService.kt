package com.vds.wishow.kwebblockchain.domain.service

import com.vds.wishow.kwebblockchain.domain.model.Wiuser
import com.vds.wishow.kwebblockchain.domain.repository.WiuserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class WiuserService(val repository: WiuserRepository) {

    fun findBy(email: String, password: String): Wiuser? = repository.findByEmailAndPassword(email, password)
    fun save(wiuser: Wiuser): Wiuser = repository.save(wiuser)
    fun findById(id: Long): Wiuser? = repository.findByIdOrNull(id)
}
