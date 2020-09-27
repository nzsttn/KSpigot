@file:Suppress("PropertyName")

import java.util.Date

/*
 * BUILD CONSTANTS
 */

val JVM_VERSION = JavaVersion.VERSION_1_8
val JVM_VERSION_STRING = "1.8"
val CONSISTENT_VERSION_STRING = "8"

val GITHUB_URL = "https://github.com/bluefireoly/KSpigot"

/*
 * PROJECT
 */

group = "net.axay"
version = "1.16.3_R4"

description = "A Kotlin API for the Minecraft Server Software \"Spigot\"."

plugins {

    java
    kotlin("jvm") version "1.4.0"

    maven
    `maven-publish`

    id("com.jfrog.bintray") version "1.8.5"

}

/*
 * DEPENDENCY MANAGEMENT
 */

repositories {
    mavenCentral()
    mavenLocal() // for retrieving the local available binaries of spigot (use the BuildTools)
}

dependencies {

    // KOTLIN
    implementation(kotlin("stdlib-jdk$CONSISTENT_VERSION_STRING"))

    // SPIGOT
    compileOnly("org.spigotmc", "spigot", "1.16.3-R0.1-SNAPSHOT")
    testCompileOnly("org.spigotmc", "spigot", "1.16.3-R0.1-SNAPSHOT")

}

/*
 * BUILD
 */

java.sourceCompatibility = JVM_VERSION

tasks {

    compileKotlin {
        kotlinOptions.jvmTarget = JVM_VERSION_STRING
    }

}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

artifacts {
    add("archives", sourcesJar)
}

/*
 * PUBLISHING
 */

bintray {

    user = project.findProperty("bintray.username") as String
    key = project.findProperty("bintray.api_key") as String

    setPublications("KSpigot")

    pkg.apply {

        version.apply {
            name = project.version.toString()
            released = Date().toString()
        }

        repo = project.name
        name = project.name

        setLicenses("Apache-2.0")

        vcsUrl = GITHUB_URL

    }

}

publishing {

    publications {
        create<MavenPublication>("KSpigot") {

            from(components["java"])

            artifact(sourcesJar)

            this.groupId = project.group.toString()
            this.artifactId = project.name
            this.version = project.version.toString()

            pom {

                name.set(project.name)
                description.set(project.description)

                developers {
                    developer {
                        name.set("bluefireoly")
                    }
                }

                url.set(GITHUB_URL)
                scm { url.set(GITHUB_URL) }

            }

        }
    }

}