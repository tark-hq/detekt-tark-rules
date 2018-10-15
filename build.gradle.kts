import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.71"
}

group = "pro.tark.detekt.tarkrules"
version = "1.0-SNAPSHOT"


val detektVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    implementation("io.gitlab.arturbosch.detekt:detekt-api:$detektVersion")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
