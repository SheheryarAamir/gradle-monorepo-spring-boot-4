import org.gradle.api.tasks.SourceSetContainer
import java.io.ByteArrayOutputStream

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



tasks.register("verifyAsyncApiDocs") {
    group = "verification"
    // 1. Ensure generation runs first
    dependsOn("generateAsyncApi")

    // 2. Capture the path at CONFIGURATION time.
    // We convert the file to a String here so the task doesn't
    // need to reference the Project object later.
    val docsDirPath = rootProject.projectDir.resolve("docs").absolutePath

    // 3. Capture the provider for the exec output
    val gitStatusProvider = providers.exec {
        commandLine("git", "status", "--porcelain", docsDirPath)
    }.standardOutput.asText

    doLast {
        // 4. Access the captured path and result
        val output = gitStatusProvider.get().trim()

        if (output.isNotEmpty()) {
            throw GradleException(
                "❌ AsyncAPI documentation is out of sync!\n" +
                        "Please run './gradlew generateAsyncApi' locally and commit changes.\n" +
                        "Detected changes in: $docsDirPath\n" +
                        "Git status:\n$output"
            )
        } else {
            println("✅ AsyncAPI documentation is up to date.")
        }
    }
}