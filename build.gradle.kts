buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${libs.versions.kotlin.get()}")
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.agpApplication) apply false
    alias(libs.plugins.agpLibrary) apply false

    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.kotlinParcelize) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.gradleVersions)
    alias(libs.plugins.versionCatalogUpdate)
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
