package com.vds.wishow.kwebblockchain.domain.bootstrap

import java.io.File

object FileUtils {

    fun getFilePath(fileName: String) = object {}.javaClass.getResource(fileName)?.file ?: throw NoSuchFileException(File(fileName))

    fun getLines(fileName: String) = File(fileName).useLines { it.toList() }
}