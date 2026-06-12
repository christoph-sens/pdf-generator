package de.christophsens.pdfgenerator

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
    }
}

