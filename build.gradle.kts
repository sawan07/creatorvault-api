import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24" // 👈 add this
}


group = "com.creatorvault"
version = "0.1.0"
java.sourceCompatibility = JavaVersion.VERSION_21


repositories { mavenCentral() }


dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect") // Kept only one instance

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // JSON & HTTP client
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // ✅ Flyway for DB migrations - CRITICAL FIX
    implementation("org.flywaydb:flyway-core:10.20.1") // <--- UPDATED VERSION
    implementation("org.flywaydb:flyway-database-postgresql:10.20.1") // <-- ADD THE VERSION HERE!
    // ✅ PostgreSQL JDBC driver - REMOVED EXPLICIT VERSION
    runtimeOnly("org.postgresql:postgresql")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testImplementation("io.mockk:mockk:1.13.12")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
    }
}


tasks.withType<Test> { useJUnitPlatform() }