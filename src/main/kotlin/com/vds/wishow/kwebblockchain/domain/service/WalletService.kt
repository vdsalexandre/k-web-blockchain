package com.vds.wishow.kwebblockchain.domain.service

import com.vds.wishow.kwebblockchain.domain.model.Wallet
import com.vds.wishow.kwebblockchain.domain.repository.WalletRepository
import org.springframework.stereotype.Service

@Service
class WalletService(val repository: WalletRepository) {

    fun findByWiuserId(id: Long): Wallet? = repository.findByWiuserId(id)

    fun save(wallet: Wallet): Wallet = repository.save(wallet)
}