package de.christophsens.pdfgenerator

//import de.christophsens.pdfgenerator.dto.TemplateDto
//import de.christophsens.pdfgenerator.dto.TranslationDto
//import de.christophsens.pdfgenerator.entity.TranslationEntity
//import de.christophsens.pdfgenerator.service.HtmlGenerationService
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.thymeleaf.TemplateEngine
//import org.thymeleaf.context.Context
//import org.thymeleaf.templateresolver.StringTemplateResolver
//import kotlin.test.assertEquals
//
//@SpringBootTest
//class Test {
//
//    @Autowired
//    private lateinit var templateEngine: TemplateEngine
//
//    @Autowired
//    private lateinit var htmlGenerationService: HtmlGenerationService
//
//    @Test
//    fun test() {
//        val translationDto = TranslationDto(1, name = "name", value = "value", languageCode = "de")
//        val templateDto =
//            TemplateDto(1, "template", "DE",
//                """<h1 th:text="${'$'}{translation.name}"></h1>""", listOf(translationDto))
//        val generateHtml = htmlGenerationService.generateHtml(templateDto, emptyMap())
//        assertEquals("<h1>value</h1>", generateHtml)

//          val expectedResult = """
//            <table>
//                <tbody>
//                    <tr>
//                        <td>a</td>
//                    </tr>
//                    <tr>
//                        <td>b</td>
//                    </tr>
//                </tbody>
//            </table>
//        """.replace(Regex("\\s+"), "")
//
//        val list = listOf("a", "b")
//        val template = """
//        <table>
//            <tbody>
//                <tr th:each="ele: ${'$'}{list}">
//                    <td th:text=" ${'$'}{ele}"/>
//                </tr>
//            </tbody>
//        </table>
//        """
//
//        val context = Context().apply {
//            setVariable("list", list)
//        }
//
//        val stringTemplateResolver = StringTemplateResolver()
//        templateEngine.setTemplateResolver(stringTemplateResolver)
//        val generatedTemplate = templateEngine.process(template, context)
//
//        Assertions.assertEquals(expectedResult, generatedTemplate.replace(Regex("\\s+"), ""))
//    }
//}