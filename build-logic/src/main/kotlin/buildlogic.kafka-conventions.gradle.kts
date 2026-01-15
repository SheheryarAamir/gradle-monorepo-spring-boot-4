plugins {
    `java-library`
    id("com.github.davidmc24.gradle.plugin.avro")
}

// Access the version catalog safely in build-logic
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    api(libs.findLibrary("avro-core").get())
    api(libs.findLibrary("confluent-avro-serializer").get())
}

// To use the Avro Task type, you might need an import:
// import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask

tasks.withType<com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask> {
    setFieldVisibility("PRIVATE")
}