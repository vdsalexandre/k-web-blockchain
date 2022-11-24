package com.vds.wishow.kwebblockchain.domain.repository

import com.vds.wishow.kwebblockchain.domain.model.Wallet
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository : CrudRepository<Wallet, String> {
    fun findByWiuserId(id: Long): Wallet?
}