package com.vds.wishow.kwebblockchain.domain.model

import com.vds.wishow.kwebblockchain.domain.bootstrap.BlockchainUtils.getDifficulty

object Blockchain {
    private val validPrefix: String = "0".repeat(getDifficulty())
    private var blocks = mutableListOf<Block>()

    init {
        addFirstBlock()
    }

    fun resetBlockchain() {
        blocks.clear()
        addFirstBlock()
    }

    fun add(block: Block): Block? {
        return if (block.isFull()) {
            val minedBlock = mine(block).copy(previousHash = blocks.last().hash)
            blocks.add(minedBlock)
            minedBlock
        } else {
            null
        }
    }

    fun contains(block: Block) = blocks.contains(block)

    fun size() = blocks.size

    fun elementAt(position: Int) = blocks.elementAt(position)

    private fun addFirstBlock() = blocks.add(Block(previousHash = "0"))

    private fun mine(block: Block): Block {
        var minedBlock = block.copy()
        while (!isMined(minedBlock)) {
            minedBlock = minedBlock.copy(nonce = minedBlock.nonce + 1)
        }
        return minedBlock
    }

    private fun isMined(block: Block) = block.hash.startsWith(validPrefix)
}