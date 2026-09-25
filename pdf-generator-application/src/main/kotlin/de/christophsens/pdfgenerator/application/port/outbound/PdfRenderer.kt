package de.christophsens.pdfgenerator.application.port.outbound

interface PdfRenderer {
    fun renderFromHtml(html: String): ByteArray
}
