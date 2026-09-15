plugins {
    kotlin("jvm") version "2.2.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // For Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")

    // For Serialization (JSON)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}