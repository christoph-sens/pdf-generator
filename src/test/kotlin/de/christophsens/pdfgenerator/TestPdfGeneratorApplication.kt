package de.christophsens.pdfgenerator

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.with

@TestConfiguration(proxyBeanMethods = false)
class TestPdfGeneratorApplication

fun main(args: Array<String>) {
	fromApplication<PdfGeneratorApplication>().with(TestPdfGeneratorApplication::class).run(*args)
}
