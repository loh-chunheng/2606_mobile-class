plugins {
    kotlin("jvm") version "2.1.0"
}

group = "iss.nus.edu.sg"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}