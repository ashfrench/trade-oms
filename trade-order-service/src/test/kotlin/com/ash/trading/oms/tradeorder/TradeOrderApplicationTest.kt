package com.ash.trading.oms.tradeorder

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TradeOrderApplicationTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun contextLoads() {
    }

    @Test
    fun testSwagger() {
        val swaggerPage = restTemplate.getForEntity<String>("/swagger-ui/index.html")
        Assertions.assertThat(swaggerPage.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(swaggerPage.hasBody()).isTrue()
        kotlin.test.assertNotNull(swaggerPage.body)
    }

    @Test
    fun testApiDocs() {
        val apiDocsPage = restTemplate.getForEntity<String>("/v3/api-docs")
        Assertions.assertThat(apiDocsPage.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(apiDocsPage.hasBody()).isTrue()
        kotlin.test.assertNotNull(apiDocsPage.body)
    }

}
