plugins {
    kotlin("jvm") version "1.7.20"
    id("org.jetbrains.dokka") version "1.7.20"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    `java-library`
    `maven-publish`
}

group = "com.bnjns.integrations"
version = "0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotestVersion = "5.5.4"
val ktorVersion = "2.2.2"

dependencies {
    // Logging
    implementation("io.github.microutils:kotlin-logging:3.0.4")

    // HTTP
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:2.2.2")

    // Tests
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-assertions-ktor:1.0.3")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
}

java {
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    jvmToolchain(11)
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "11"
}

tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = "11"
}

tasks.detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$projectDir/detekt.yml")

    reports {
        md.required.set(true)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("Opsgenie Kotlin SDK")
                description.set("A Kotlin SDK for interacting with the Opsgenie REST API")
                url.set("https://github.com/bnjns/opsgenie-kotlin-sdk")

                scm {
                    connection.set("scm:git:git://github.com/bnjns/opsgenie-kotlin-sdk.git")
                    developerConnection.set("scm:git:ssh://github.com/bnjns/opsgenie-kotlin-sdk.git")
                    url.set("https://github.com/bnjns/opsgenie-kotlin-sdk")
                }

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://opensource.org/licenses/Apache-2.0")
                    }
                }

                developers {
                    developer {
                        id.set("bnjns")
                        name.set("Ben Jones")
                        email.set("ben@bnjns.uk")
                    }
                }
            }
        }
    }
}
