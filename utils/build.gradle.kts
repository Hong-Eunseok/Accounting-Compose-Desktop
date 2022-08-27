plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    api(libs.dagger)
    kapt(libs.daggerCompiler)
}