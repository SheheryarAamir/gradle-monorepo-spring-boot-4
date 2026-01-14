rootProject.name = "kotlin-monorepo"


pluginManagement {
    // Include 'plugins build' to define convention plugins.
    includeBuild("build-logic")
}
plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

listOf("app-service", "kafka-consumer").forEach {
    if (file(it).exists()) {
        include(it)
    }
}