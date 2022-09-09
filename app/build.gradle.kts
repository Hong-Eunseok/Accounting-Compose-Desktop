import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Suppressing bug:
 * https://youtrack.jetbrains.com/issue/KTIJ-19369
 */

repositories {
    mavenCentral()
    google()
    maven {
        url = uri("https://repo.spring.io/release")
    }
    maven {
        url = uri("https://repository.jboss.org/maven2")
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.compose)
    kotlin("jvm")
    kotlin("kapt")
}

group = "me.zero"
version = "1.0"

dependencies {
    api(libs.dagger)
    kapt(libs.daggerCompiler)
    implementation(project(":navigation"))
    implementation(project(":database"))
    implementation(project(":utils"))
    implementation(project(":preferences"))
    implementation(compose.desktop.currentOs)
    implementation(libs.junit)
//    implementation(libs.sqlite)
    implementation(libs.testCoroutines)
//    implementation(libs.bundles.exposed)

    // SQLite Database & Exposed Library
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("org.jetbrains.exposed:exposed-core:0.37.3")
    implementation("org.jetbrains.exposed:exposed-dao:0.37.3")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.37.3")


//    implementation(libs.decompose)
    testImplementation(libs.truth)
    // https://mvnrepository.com/artifact/androidx.compose.ui/ui-tooling-preview
//    runtimeOnly("androidx.compose.ui:ui-tooling-preview:1.3.0-beta01")

//    runtimeOnly(libs.preview.tooling.preview)
//    runtimeOnly(libs.preview.tooling)

}

compose.desktop {
    application {
        mainClass = "com.acc.ApplicationKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            modules("java.sql")

            packageName = "acc"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}