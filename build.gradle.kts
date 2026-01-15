allprojects {
    // We use 'tasks.maybeCreate' so we don't get 'DuplicateTaskException'
    // if the Kotlin plugin eventually decides to show up.
    tasks.maybeCreate("prepareKotlinBuildScriptModel").apply {
        group = "ide"
        description = "Satisfies IntelliJ sync for Configuration Cache compatibility"
    }
}

val dockerComposePath = "/usr/local/bin/docker"

tasks.register<Exec>("dockerComposeUp") {
    group = "docker"
    description = "Builds and starts all services (infra + modules) using Docker Compose"
    workingDir = rootDir

    executable = dockerComposePath

    // 1. Start with the project-directory and infra file
    val composeArgs = mutableListOf(
        "compose",
        "--project-directory", rootDir.absolutePath,
        "-f", "docker-compose.infra.yaml"
    )

    // 2. Dynamically find all subprojects with a docker-compose.yaml
    subprojects.forEach { sub ->
        val composeFile = sub.file("docker-compose.yaml")
        if (composeFile.exists()) {
            composeArgs.add("-f")
            // Use relative path locally or absolute path
            composeArgs.add(composeFile.absolutePath)
        }
    }

    // 3. Add the 'up' command arguments
    composeArgs.addAll(listOf("up", "--build", "-d"))

    args(composeArgs)
    
    doFirst {
        println("Running Docker Compose with args: $composeArgs")
    }
}

tasks.register<Exec>("dockerComposeDown") {
    group = "docker"
    description = "Stops and removes all services (infra + modules) using Docker Compose"
    workingDir = rootDir

    executable = dockerComposePath

    val composeArgs = mutableListOf(
        "compose",
        "--project-directory", rootDir.absolutePath,
        "-f", "docker-compose.infra.yaml"
    )

    subprojects.forEach { sub ->
        val composeFile = sub.file("docker-compose.yaml")
        if (composeFile.exists()) {
            composeArgs.add("-f")
            composeArgs.add(composeFile.absolutePath)
        }
    }

    composeArgs.add("down")

    args(composeArgs)
}