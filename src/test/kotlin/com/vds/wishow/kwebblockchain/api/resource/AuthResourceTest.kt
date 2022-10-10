package com.vds.wishow.kwebblockchain.api.resource

import com.ninjasquad.springmockk.MockkBean
import com.vds.wishow.kwebblockchain.bootstrap.JwsUtils
import com.vds.wishow.kwebblockchain.domain.model.Wiuser
import com.vds.wishow.kwebblockchain.domain.service.WiuserService
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(value = [AuthResource::class], excludeAutoConfiguration = [SecurityAutoConfiguration::class])
class AuthResourceTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean private lateinit var service: WiuserService

    @Test
    fun `should generate JWS token if id wiuser is found`() {
        every { service.findById(1) } returns expectedWiuser

        mockMvc.perform(get("/auth/token/${expectedWiuser.id}"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().response.contentAsString.isNotEmpty()

        verify(exactly = 1) { service.findById(any()) }
    }

    @Test
    fun `should respond user not found for a wrong id`() {
        every { service.findById(any()) } returns null

        mockMvc.perform(get("/auth/token/-1"))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().response.contentAsString.isEmpty()

        verify(exactly = 1) { service.findById(any()) }
    }

    @Test
    fun `should retrieve user details if JWS given is valid`() {
        val jws = "123456789"
        val id = 1L

        mockkObject(JwsUtils)
        every { JwsUtils.verifyJWS(any(), jws) } returns true
        every { JwsUtils.extractIssuer(any(), jws) } returns id
        every { service.findById(id) } returns expectedWiuser

        mockMvc.perform(get("/auth/user/$jws"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("marty.mcfly"))

        verify(exactly = 1) { JwsUtils.verifyJWS(any(), jws) }
        verify(exactly = 1) { JwsUtils.extractIssuer(any(), jws) }
        verify(exactly = 1) { service.findById(id) }
    }

    @Test
    fun `should retrieve not found message if JWS given is for unknown user`() {
        mockkObject(JwsUtils)
        every { JwsUtils.verifyJWS(any(), any()) } returns true
        every { JwsUtils.extractIssuer(any(), any()) } returns -1
        every { service.findById(-1) } returns null

        mockMvc.perform(get("/auth/user/jws"))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").value("Not Found"))

        verify(exactly = 1) { JwsUtils.verifyJWS(any(), any()) }
        verify(exactly = 1) { JwsUtils.extractIssuer(any(), any()) }
        verify(exactly = 1) { service.findById(any()) }
    }

    @Test
    fun `should retrieve unauthorized message if JWS given is not valid`() {
        mockkObject(JwsUtils)
        every { JwsUtils.verifyJWS(any(), any()) } returns false

        mockMvc.perform(get("/auth/user/jws"))
            .andExpect(status().isUnauthorized)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").value("Unauthorized"))

        verify(exactly = 1) { JwsUtils.verifyJWS(any(), any()) }
    }

    companion object {
        val expectedWiuser = Wiuser(
            id = 1,
            username = "marty.mcfly",
            email = "marti.mcfly@gmail.com",
            password = "martiTheBest123"
        )
    }
}
