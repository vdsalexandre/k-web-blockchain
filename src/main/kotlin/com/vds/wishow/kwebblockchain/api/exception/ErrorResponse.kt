package com.vds.wishow.kwebblockchain.api.exception

import org.springframework.http.HttpStatus

data class ErrorResponse(val errorCode: HttpStatus, val errorMessage: String?)