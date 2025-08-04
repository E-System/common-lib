import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.*

/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
plugins {
    java
    groovy
    jacoco
    `java-library`
    `maven-publish`
    alias(libs.plugins.versions)
    alias(libs.plugins.sonarqube)
}

tasks.wrapper {
    gradleVersion = "9.0.0"
}

fun resolve(name: String): String? {
    return (System.getenv()[name] ?: properties[name]) as String?
}

apply("build-${resolve("profile")}.gradle.kts")

group = extra["mainGroup"] as String
version = extra["mainVersion"] as String + (if (extra["snapshot"] as Boolean) "-SNAPSHOT" else "")

repositories {
    mavenCentral()
    mavenLocal()
}

var filePath = Paths.get(project.projectDir.toString(), "src/main/resources/com/eslibs/common/")
Files.createDirectories(filePath)
filePath = filePath.resolve("build.properties")
println("Create file for property: $filePath")
val props = Properties()
props["name"] = rootProject.name
props["version"] = project.version
props["date"] = LocalDateTime.now().toString()
props.store(Files.newOutputStream(filePath), null)

tasks {
    jar {
        manifest {
            attributes(
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to "E-SYSTEM"
            )
        }
    }
    javadoc {
        isFailOnError = false
        options.encoding = "utf-8"
    }
}
tasks.named<UpdateDaemonJvm>("updateDaemonJvm") {
    languageVersion = JavaLanguageVersion.of(24)
    vendor = JvmVendorSpec.ADOPTIUM
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
        vendor = JvmVendorSpec.ADOPTIUM
    }
    withSourcesJar()
    withJavadocJar()
}
publishing {
    repositories {
        maven {
            url = uri("${resolve("repos_url")}${(if (extra["snapshot"] as Boolean) "snapshots" else "releases")}")
            credentials(PasswordCredentials::class) {
                username = resolve("repos_user")
                password = resolve("repos_password")
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
}
dependencies {
    api(libs.commons.lang3)
    api(libs.commons.io)
    api(libs.slf4j.api)
    api(libs.jackson.databind)
    api(libs.angus.mail)

    api(libs.bcrypt)
    api(libs.reflections)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(libs.bundles.spock)
    testImplementation(libs.logback)
    testImplementation(libs.jackson.datatype.jsr310)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val emailTestEnabled =
    resolve("test_email_server") != null && resolve("test_email_login") != null && resolve("test_email_password") != null
if (emailTestEnabled) {
    tasks.test {
        if (emailTestEnabled) {
            systemProperties(
                mapOf(
                    "test_email_server" to resolve("test_email_server"),
                    "test_email_login" to resolve("test_email_login"),
                    "test_email_password" to resolve("test_email_password")
                )
            )
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

val sonarAvailable =
    resolve("sonar_url") != null && resolve("sonar_user") != null && resolve("sonar_password") != null
if (sonarAvailable) {
    val url = resolve("sonar_url")
    val user = resolve("sonar_user")
    val pass = resolve("sonar_password")
    sonar {
        properties {
            properties(
                mapOf(
                    "sonar.host.url" to url,
                    "sonar.login" to user,
                    "sonar.password" to pass,
                    "sonar.gradle.skipCompile" to true
                )
            )
        }
    }
}
