allprojects {
    // We use 'tasks.maybeCreate' so we don't get 'DuplicateTaskException'
    // if the Kotlin plugin eventually decides to show up.
    tasks.maybeCreate("prepareKotlinBuildScriptModel").apply {
        group = "ide"
        description = "Satisfies IntelliJ sync for Configuration Cache compatibility"
    }
}