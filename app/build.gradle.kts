import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.twofasAndroidApplication)
    alias(libs.plugins.twofasCompose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.twofasapp"

    defaultConfig {
        applicationId = "com.twofasapp"
        versionName = "5.4.0"
        versionCode = 5000019

        val versionCodeOffset = 5000000

        archivesName.set("TwoFas-$versionName-${versionCode!! - versionCodeOffset}")
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:android"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:storage"))
    implementation(project(":core:network"))
    implementation(project(":core:locale"))
    implementation(project(":core:cipher"))

    implementation(project(":data:notifications"))
    implementation(project(":data:session"))
    implementation(project(":data:services"))
    implementation(project(":data:cloud"))

    implementation(project(":feature:startup"))
    implementation(project(":feature:home"))
    implementation(project(":feature:trash"))
    implementation(project(":feature:about"))
    implementation(project(":feature:externalimport"))
    implementation(project(":feature:appsettings"))
    implementation(project(":feature:qrscan"))
    implementation(project(":feature:backup"))
    implementation(project(":feature:widget"))
    implementation(project(":feature:security"))

    implementation(project(":base"))
    implementation(project(":prefs"))
    implementation(project(":truetime"))
    implementation(project(":parsers"))

    implementation(libs.bundles.appCompat)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.barcodeScanner)
    implementation(libs.bundles.room)
    implementation(libs.reLinker)
    ksp(libs.roomCompiler)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.viewModel)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.glance)
    implementation(libs.biometric)
    implementation(libs.timber)
    implementation(libs.webkit)
    implementation(libs.securityCrypto)
    implementation(libs.secureStorage)
    implementation(libs.lottie)
    implementation(libs.kotlinCoroutines)
    implementation(libs.workManager)
    implementation(libs.activityX)
    implementation(libs.coreSplash)
    implementation(libs.googleApiClientGson)
    implementation(libs.googleApiClientAndroid) {
        exclude("org.apache.httpcomponents", "guava-jdk5")
        exclude("com.google.http-client", "google-http-client")
    }
    implementation(libs.googleAuth) {
        exclude("com.google.http-client", "google-http-client")
        exclude("com.google.http-client", "google-http-client-jackson")
    }
    implementation(libs.googleDrive) {
        exclude("org.apache.httpcomponents", "guava-jdk5")
        exclude("com.google.http-client", "google-http-client")
    }
}
