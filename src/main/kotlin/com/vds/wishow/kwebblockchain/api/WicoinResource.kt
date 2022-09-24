package com.vds.wishow.kwebblockchain.api

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/wicoin"], produces = ["application/json"])
class WicoinResource {
    private val logger = LoggerFactory.getLogger(WicoinResource::class.java)


}