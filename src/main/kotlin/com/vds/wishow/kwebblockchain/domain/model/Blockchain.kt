package com.vds.wishow.kwebblockchain.domain.model

class Blockchain(difficulty: Int = 5) {
    private val validPrefix = "0".repeat(difficulty)
    private val blocks = mutableListOf<Block>()

    init {
        blocks.add(Block(previousHash = "0"))
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

    private fun mine(block: Block): Block {
        var minedBlock = block.copy()
        while (!isMined(minedBlock)) {
            minedBlock = minedBlock.copy(nonce = minedBlock.nonce + 1)
        }
        return minedBlock
    }

    private fun isMined(block: Block) = block.hash.startsWith(validPrefix)
}