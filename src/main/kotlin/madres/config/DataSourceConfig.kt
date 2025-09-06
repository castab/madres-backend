package madres.config

import com.zaxxer.hikari.HikariDataSource
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "datasource")
class DataSourceConfig(
    val jdbcUrl: String,
    val username: String?,
    val password: String?,
    val driverClassName: String = "org.postgresql.Driver",
    val maximumPoolSize: Int = 4,
    val minimumIdle: Int = 0,
) {
    @Bean
    fun dataSource() = HikariDataSource().apply {
        jdbcUrl = this@DataSourceConfig.jdbcUrl
        username = this@DataSourceConfig.username
        password = this@DataSourceConfig.password
        driverClassName = this@DataSourceConfig.driverClassName
        maximumPoolSize = this@DataSourceConfig.maximumPoolSize
        minimumIdle = this@DataSourceConfig.minimumIdle
        addDataSourceProperty(
            "connectionInitSql",
            "SET search_path TO madres, public;"
        )
    }

    @Bean
    fun jdbi(ds: HikariDataSource): Jdbi = Jdbi.create(ds)
}
