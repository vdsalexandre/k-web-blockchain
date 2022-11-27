package com.vds.wishow.kwebblockchain.api.logger

data class Log(val resource: String, val method: String, val url: String, val httpVerb: String) {
    override fun toString(): String = "[$httpVerb] || [$url] || [$resource.$method()] -"
}
