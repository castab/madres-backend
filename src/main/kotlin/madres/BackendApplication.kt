package madres

import madres.config.DataSourceConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableConfigurationProperties(
	DataSourceConfig::class
)
@EnableScheduling
class BackendApplication

fun main(args: Array<String>) {
	runApplication<BackendApplication>(*args)
}
