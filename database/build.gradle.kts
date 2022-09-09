plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    api(libs.dagger)
    kapt(libs.daggerCompiler)
//    implementation(libs.sqlite)
    // SQLite Database & Exposed Library
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("org.jetbrains.exposed:exposed-core:0.37.3")
    implementation("org.jetbrains.exposed:exposed-dao:0.37.3")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.37.3")
}