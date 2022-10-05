package com.vds.wishow.kwebblockchain.domain.service

import com.vds.wishow.kwebblockchain.domain.model.Wiuser
import com.vds.wishow.kwebblockchain.domain.repository.WiuserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

internal class WiuserServiceTest {
    private val repository: WiuserRepository = mockk()
    private val service: WiuserService = WiuserService(repository)

    @Test
    fun `should retrieve the good wiuser with his id calling wiuser repository`() {
        val expectedWiuser = Wiuser(1, "marty.mcfly", "marty.mcfly@gmail.com", "martyIsTheBest123")
        every {
            repository.findByIdOrNull(1)
        } returns expectedWiuser

        val wiuser = service.findById(1)

        verify(exactly = 1) { repository.findByIdOrNull(1) }
        assertThat(wiuser).isEqualTo(expectedWiuser)
    }

    @Test
    fun `should retrieve null if wiuser repository didn't found wiuser`() {
        every {
            repository.findByIdOrNull(1)
        } returns null

        val wiuser = service.findById(1)

        verify(exactly = 1) { repository.findByIdOrNull(1) }
        assertThat(wiuser).isNull()
    }
}