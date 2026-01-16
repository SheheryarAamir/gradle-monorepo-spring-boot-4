import org.gradle.api.tasks.SourceSetContainer

plugins {
    id("org.jetbrains.kotlin.jvm")
}


tasks.register<Test>("generateAsyncApi") {
    group = "documentation"

    // Link source sets so the task "sees" your test code
    val testSourceSet = project.extensions.getByType<SourceSetContainer>().getByName("test")
    testClassesDirs = testSourceSet.output.classesDirs
    classpath = testSourceSet.runtimeClasspath

    // Use the specific class name to be 100% sure
    filter {
        includeTestsMatching("com.example.*.asyncapi.AsyncApiGeneratorTest")
        isFailOnNoMatchingTests = true // Change to TRUE to debug
    }

    // Pass the root directory to the test
    systemProperty("ROOT_DOCS_DIR", rootProject.projectDir.absolutePath)

    useJUnitPlatform()
    outputs.upToDateWhen { false } // Force run

    testLogging {
        showStandardStreams = true // This lets you see the println in terminal
        events("started", "passed", "failed")
    }
}