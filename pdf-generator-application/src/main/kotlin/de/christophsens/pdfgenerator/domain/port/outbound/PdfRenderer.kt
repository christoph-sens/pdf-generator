package de.christophsens.pdfgenerator.domain.port.outbound

interface PdfRenderer {
    fun renderFromHtml(html: String): ByteArray
}
