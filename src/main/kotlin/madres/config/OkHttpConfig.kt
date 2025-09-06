package madres.config

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class OkHttpConfig {
    @Bean
    @Primary
    fun okHttpClient(): OkHttpClient = OkHttpClient()
}