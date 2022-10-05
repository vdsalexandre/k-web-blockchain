package com.vds.wishow.kwebblockchain.api.resource

import com.ninjasquad.springmockk.MockkBean
import com.vds.wishow.kwebblockchain.bootstrap.JwsUtils
import com.vds.wishow.kwebblockchain.domain.model.Wiuser
import com.vds.wishow.kwebblockchain.domain.service.WiuserService
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
internal class AuthResourceTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var service: WiuserService

    private val mockkJwsUtils = mockkObject(JwsUtils)

    @Test
    fun `should retrieve the good wiuser with his id calling wiuser repository`() {
        val expectedWiuser = Wiuser(1, "marty.mcfly", "marty.mcfly@gmail.com", "martyIsTheBest123")
        val params: MutableMap<String, String> = mutableMapOf()
        params["jws"] = "123456789"

        // TODO: update test
        every { mockkJwsUtils.invoke("get") }
        every {service.findById(1) } returns expectedWiuser

        mockMvc.perform(get("/user/{jws}", params))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("marty.mcfly"))
    }
}