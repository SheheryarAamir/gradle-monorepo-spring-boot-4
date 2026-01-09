plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm")
    id("org.springframework.boot")           // This creates the 'bootJar' task
    id("io.spring.dependency-management")    // Optional but common
    kotlin("plugin.spring")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}
val libs = the<VersionCatalogsExtension>().named("libs")
dependencies {

    implementation(libs.findLibrary("spring-boot-starter-web").get())
    implementation(libs.findLibrary("spring-boot-starter-actuator").get())
    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
    implementation(libs.findLibrary("kotlinx-coroutines-reactor").get())
    implementation(libs.findLibrary("spring-starter-test").get())
    implementation(libs.findLibrary("kotlin-logging").get())
    implementation(libs.findLibrary("spring-boot-starter-otel").get())
    implementation(libs.findLibrary("otel-logback-appender").get())
    implementation(libs.findLibrary("micrometer-tracing-bridge-otel").get())
    implementation(libs.findLibrary("opentelemetry-exporter-otlp").get())
    //implementation(libs.findLibrary("otel-logback-mdc").get())
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
            dependencies {
                // If the 'libs' accessor is still not resolving, use the findLibrary method:
                implementation(libs.findLibrary("spring-starter-test").get())
            }
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
