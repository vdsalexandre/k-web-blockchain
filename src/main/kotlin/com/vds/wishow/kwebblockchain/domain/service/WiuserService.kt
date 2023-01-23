package com.vds.wishow.kwebblockchain.domain.service

import com.vds.wishow.kwebblockchain.api.dto.WiuserLoginDTO
import com.vds.wishow.kwebblockchain.domain.model.Wiuser
import com.vds.wishow.kwebblockchain.domain.repository.WiuserRepository
import com.vds.wishow.kwebblockchain.security.DataEncoder.matches
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class WiuserService(val repository: WiuserRepository) {

    fun findWiuser(wiuserLoginDTO: WiuserLoginDTO): Wiuser? {
        return repository
            .findAll()
            .find { wiuser ->
                matches(wiuserLoginDTO.email, wiuser.email) &&
                matches(wiuserLoginDTO.password, wiuser.password)
            }
    }

    fun save(wiuser: Wiuser): Wiuser {
        if (wiuser.username.isEmpty()) throw IllegalArgumentException("Error - one or more fields are missing")

        return repository.save(wiuser)
    }

    fun findById(id: Long): Wiuser? = repository.findByIdOrNull(id)
}
