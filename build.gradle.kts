import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.71"
    maven
}

val detektVersion: String by project
val detektTarkRulesVersion: String by project

group = "pro.tark.detekt.tarkrules"
version = detektTarkRulesVersion



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
