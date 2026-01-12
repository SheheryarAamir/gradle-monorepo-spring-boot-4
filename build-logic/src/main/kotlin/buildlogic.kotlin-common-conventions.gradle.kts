val libs = the<VersionCatalogsExtension>().named("libs")
plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm")
    id("org.springframework.boot")           // This creates the 'bootJar' task
    id("io.spring.dependency-management")    // Optional but common
    kotlin("plugin.spring")
    id("buildlogic.spot-less-conventions")
    //id("org.graalvm.buildtools.native")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {

    implementation(libs.findLibrary("spring-boot-starter-web").get())
    implementation(libs.findLibrary("spring-boot-starter-actuator").get())

    implementation(libs.findLibrary("mysql-connector").get())
    implementation(libs.findLibrary("datasource-micrometer").get())

    implementation(libs.findLibrary("spring-boot-starter-otel").get())
    implementation(libs.findLibrary("otel-logback-appender").get())
    implementation(libs.findLibrary("micrometer-tracing-bridge-otel").get())
    implementation(libs.findLibrary("opentelemetry-exporter-otlp").get())
    implementation(libs.findLibrary("spring-boot-starter-aspectj").get())
    implementation(libs.findLibrary("spring-boot-starter-restclient").get())
    implementation(libs.findLibrary("spring-boot-starter-jdbc").get())
    implementation(libs.findLibrary("spring-boot-h2").get())

    implementation(libs.findLibrary("kotlin-logging").get())
    implementation(libs.findLibrary("kotlin-reflect").get())
    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
    implementation(libs.findLibrary("kotlinx-coroutines-reactor").get())

    //testImplementation(libs.findLibrary("spring-boot-starter-web-test").get())
    //testImplementation(libs.findLibrary("spring-boot-starter-jdbc-test").get())
    testImplementation(libs.findLibrary("spring-starter-test").get())

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
