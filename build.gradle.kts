plugins {
	kotlin("jvm") version "2.2.0"
	kotlin("plugin.spring") version "2.2.0"
	id("org.flywaydb.flyway") version "11.10.5"
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "madres"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// logger
	implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

	// resend
	implementation("com.resend:resend-java:3.1.0")

	// okhttp
	implementation(platform("com.squareup.okhttp3:okhttp-bom:5.1.0"))
	implementation("com.squareup.okhttp3:okhttp")
	implementation("com.squareup.okhttp3:logging-interceptor")
	testImplementation("com.squareup.okhttp3:mockwebserver3")

	// jdbi + postgres
	implementation("org.jdbi:jdbi3-core:3.46.0")
	implementation("org.jdbi:jdbi3-sqlobject:3.46.0")
	implementation("org.jdbi:jdbi3-kotlin:3.46.0")
	implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.46.0")
	implementation("com.zaxxer:HikariCP:5.0.1")
	runtimeOnly("org.postgresql:postgresql")

	// flyway
	implementation("org.flywaydb:flyway-database-postgresql:11.10.5")

	// kotlin-result
	implementation("com.michael-bull.kotlin-result:kotlin-result:2.1.0")

	// kotlin coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
