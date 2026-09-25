package de.christophsens.pdfgenerator.adapter.outbound.pdf

import kotlin.test.Test
import kotlin.test.assertEquals

class FlyingSaucerPdfRendererTest {

    @Test
    fun `renders XHTML to a PDF document`() {
        val pdf = FlyingSaucerPdfRenderer().renderFromHtml("<html><body><p>Hallo</p></body></html>")

        assertEquals("%PDF", pdf.copyOfRange(0, 4).decodeToString())
    }
}
