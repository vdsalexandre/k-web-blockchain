package com.vds.wishow.kwebblockchain.service

import com.vds.wishow.kwebblockchain.model.Wiuser
import com.vds.wishow.kwebblockchain.repository.WiuserRepository
import org.springframework.stereotype.Service

@Service
class WiuserService(val repository: WiuserRepository) {

    fun findBy(email: String, password: String) = repository.findByEmailAndPassword(email, password)

    fun save(wiuser: Wiuser) = repository.save(wiuser)
}
