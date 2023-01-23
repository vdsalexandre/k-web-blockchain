package com.vds.wishow.kwebblockchain.api.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.vds.wishow.kwebblockchain.api.dto.QrCodeDTO
import com.vds.wishow.kwebblockchain.bootstrap.AuthUtils
import com.vds.wishow.kwebblockchain.domain.model.Wallet
import com.vds.wishow.kwebblockchain.domain.model.Wiuser
import com.vds.wishow.kwebblockchain.domain.service.WalletService
import com.vds.wishow.kwebblockchain.domain.service.WiuserService
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(value = [AuthResource::class], excludeAutoConfiguration = [SecurityAutoConfiguration::class])
class AuthResourceTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var service: WiuserService

    @MockkBean
    private lateinit var walletService: WalletService

    @BeforeEach
    fun setUp() {
        mockkObject(AuthUtils)
    }

    @Test
    fun `should return 200 ok and generate JWS token for wiuser using a good id`() {
        every { service.findWiuser(any()) } returns expectedWiuser

        mockMvc.perform(
            post("/auth/token")
                .content(ObjectMapper().writeValueAsString(expectedWiuser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().response.contentAsString.isNotEmpty()

        verify(exactly = 1) { service.findWiuser(any()) }
    }

    @Test
    fun `should return 404 user not found if wiuser id doesn't exists`() {
        every { service.findWiuser(any()) } returns null

        mockMvc.perform(
            post("/auth/token")
                .content(ObjectMapper().writeValueAsString(expectedWiuser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().response.contentAsString.isEmpty()

        verify(exactly = 1) { service.findWiuser(any()) }
    }

    @Test
    fun `should return 200 ok user details if JWS given is valid`() {
        val jws = "123456789"
        val id = expectedWiuser.id!!

        every { AuthUtils.verifyJWS(any(), jws) } returns true
        every { AuthUtils.extractIssuer(any(), jws) } returns id
        every { service.findById(id) } returns expectedWiuser
        every { walletService.findByWiuserId(id) } returns expectedWallet

        mockMvc.perform(
            get("/auth/user/$jws")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("marty.mcfly"))

        verify(exactly = 1) { AuthUtils.verifyJWS(any(), jws) }
        verify(exactly = 1) { AuthUtils.extractIssuer(any(), jws) }
        verify(exactly = 1) { service.findById(id) }
    }

    @Test
    fun `should return 401 unauthorized if JWS given belongs to another wiuser id`() {
        val jws = "123456789"

        every { AuthUtils.verifyJWS(any(), any()) } returns true
        every { AuthUtils.extractIssuer(any(), any()) } returns -1
        every { service.findById(-1) } returns null

        mockMvc.perform(
            get("/auth/user/$jws")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").value("${HttpStatus.UNAUTHORIZED.value()} - ${HttpStatus.UNAUTHORIZED.reasonPhrase}"))

        verify(exactly = 1) { AuthUtils.verifyJWS(any(), any()) }
        verify(exactly = 1) { AuthUtils.extractIssuer(any(), any()) }
        verify(exactly = 1) { service.findById(-1) }
    }

    @Test
    fun `should return 401 unauthorized message if JWS given is not valid`() {
        every { AuthUtils.verifyJWS(any(), any()) } returns false

        mockMvc.perform(get("/auth/user/jws"))
            .andExpect(status().isUnauthorized)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").value("${HttpStatus.UNAUTHORIZED.value()} - ${HttpStatus.UNAUTHORIZED.reasonPhrase}"))

        verify(exactly = 1) { AuthUtils.verifyJWS(any(), any()) }
    }

    @Test
    fun `should return 400 bad request if wrong wiuser login body is given for the JWS token creation`() {
        mockMvc.perform(
            post("/auth/token")
                .content(ObjectMapper().writeValueAsString(null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$").value("${HttpStatus.BAD_REQUEST.value()} - ${HttpStatus.BAD_REQUEST.reasonPhrase}"))
    }

    @Test
    fun `should return 200 ok wallet qrcode address if good wallet id is given`() {
        every { walletService.findById(expectedWallet.walletId) } returns expectedWallet

        mockMvc.perform(
            get("/auth/wallet/${expectedWallet.walletId}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.qrCode").isNotEmpty)

        verify(exactly = 1) { walletService.findById(any()) }
    }

    @Test
    fun `should return 404 not found qrcode address if wallet id given if not found`() {
        every { walletService.findById(expectedWallet.walletId) } returns null

        mockMvc.perform(
            get("/auth/wallet/${expectedWallet.walletId}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        verify(exactly = 1) { walletService.findById(any()) }
    }

    companion object {
        val expectedWiuser = Wiuser(
            id = 1,
            username = "marty.mcfly",
            email = "marty.mcfly@gmail.com",
            password = "martiTheBest123"
        )

        private val expectedQrCode = QrCodeDTO(
            qrCode = "marty.mcfly.wallet.qrcode".toByteArray()
        )

        val expectedWallet = Wallet(
            walletId = "WIC123456789",
            privateKey = "AbCdEfGhIjKlMnOpQrStUvWxYz1234567890",
            wiuserId = 1,
            qrCode = expectedQrCode.qrCode
        )
    }
}
