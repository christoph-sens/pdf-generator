package de.christophsens.pdfgenerator.adapter.outbound.pdf

import de.christophsens.pdfgenerator.domain.port.outbound.PdfRenderer
import org.springframework.stereotype.Component
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream

@Component
class FlyingSaucerPdfRenderer : PdfRenderer {

    override fun renderFromHtml(html: String): ByteArray {
        val renderer = ITextRenderer()
        renderer.setDocumentFromString(html)
        renderer.layout()
        val outputStream = ByteArrayOutputStream()
        renderer.createPDF(outputStream)
        return outputStream.toByteArray()
    }
}

