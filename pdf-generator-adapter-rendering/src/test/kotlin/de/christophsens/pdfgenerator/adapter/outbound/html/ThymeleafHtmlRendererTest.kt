package de.christophsens.pdfgenerator.adapter.outbound.html

import de.christophsens.pdfgenerator.application.port.outbound.RenderRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class ThymeleafHtmlRendererTest {

    private val renderer = ThymeleafHtmlRenderer()

    @Test
    fun `renders translations and data with SpEL expressions`() {
        val html = renderer.render(
            RenderRequest(
                templateContent = """<p th:text="${'$'}{translation.title + ': ' + data.items.size()}"></p>""",
                translations = mapOf("title" to "Artikel"),
                data = mapOf("items" to listOf(1, 2, 3))
            )
        )

        assertEquals("<p>Artikel: 3</p>", html)
    }
}
