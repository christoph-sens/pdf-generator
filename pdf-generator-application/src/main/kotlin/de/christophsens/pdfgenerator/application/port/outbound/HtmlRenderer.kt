package de.christophsens.pdfgenerator.application.port.outbound

interface HtmlRenderer {
    fun render(request: RenderRequest): String
}

data class RenderRequest(
    val templateContent: String,
    val translations: Map<String, String>,
    val data: Map<String, Any?>
)
