rootProject.name = "kotlin-monorepo"


pluginManagement {
    // Include 'plugins build' to define convention plugins.
    includeBuild("build-logic")
}
plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    // id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

listOf("app-service", "kafka-consumer", "common-events").forEach {
    if (file(it).exists()) {
        include(it)
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenCentral()
        maven {
            url = uri("https://packages.confluent.io/maven/")
        }
    }
}