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
    maven { url = uri("https://repo.spring.io/release") }
    maven { url = uri("https://repository.jboss.org/maven2") }
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
    implementation(libs.bundles.exposed)
    implementation(libs.lGoodDatePicker)
    implementation(libs.excelkt)

//    implementation(libs.decompose)
    implementation(libs.junit)
    implementation(libs.testCoroutines)
    testImplementation(libs.truth)
}

compose.desktop {
    application {
        mainClass = "com.acc.ApplicationKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            modules("java.sql")

            packageName = "goodwillstore"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}