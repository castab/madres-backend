package madres.config

import madres.common.objectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class ObjectMapperConfig {
    @Bean
    @Primary
    fun objectMapper() = objectMapper
}