import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.KotlinExtension

plugins {
    id("com.diffplug.spotless")
}

configure<SpotlessExtension> {
    kotlin {
        // This targets all kotlin files in the module
        target("**/*.kt")
        targetExclude("**/build/**")
        ktlint().editorConfigOverride(mapOf(
            // Removes unused imports (your previous issue)
            "ktlint_standard_no-unused-imports" to "enabled",
            // Groups and sorts imports alphabetically
            "ktlint_standard_import-ordering" to "enabled",
            // Common practice: disable wildcard imports
            "ktlint_standard_no-wildcard-imports" to "enabled"
        ))
        suppressLintsFor {
            step = "ktlint"
            shortCode = "standard:no-wildcard-imports"
        }
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}