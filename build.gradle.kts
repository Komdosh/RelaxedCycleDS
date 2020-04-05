import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.71"
}

group = "pro.komdosh"
version = "0.0.1"

repositories {
    maven(url = "https://kotlin.bintray.com/kotlinx")
    maven(url = "https://dl.bintray.com/devexperts/Maven/")
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "13"
}
