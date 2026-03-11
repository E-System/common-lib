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
    gradleVersion = "9.4.0"
}

fun resolve(name: String): String? {
    return (findProperty(name) ?: System.getenv(name)) as String?
}

fun resolveServer(urlName: String, userName: String, passwordName: String): Triple<String, String, String>? {
    val url = resolve(urlName)
    val user = resolve(userName)
    val password = resolve(passwordName)
    if (url != null && user != null && password != null) {
        return Triple(url, user, password)
    }
    return null
}

apply("build-${resolve("profile")}.gradle.kts")

group = extra["mainGroup"] as String
version = extra["mainVersion"] as String + (if (extra["snapshot"] as Boolean) "-SNAPSHOT" else "")

repositories {
    mavenCentral()
    mavenLocal()
}

Paths.get(project.projectDir.toString(), "src/main/resources/com/eslibs/common/").let {
    Files.createDirectories(it)
    val path = it.resolve("build.properties")
    println("Create file for property: $path")
    Properties().let {
        it["name"] = rootProject.name
        it["version"] = project.version
        it["date"] = LocalDateTime.now().toString()
        it.store(Files.newOutputStream(path), null)
    }
}


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
val jVersion = JavaLanguageVersion.of(25)
tasks.named<UpdateDaemonJvm>("updateDaemonJvm") {
    languageVersion = jVersion
    vendor = JvmVendorSpec.ADOPTIUM
}
java {
    toolchain {
        languageVersion = jVersion
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

    testImplementation(libs.spock)
    testImplementation(libs.logback)
    testImplementation(libs.jackson.datatype.jsr310)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
    resolveServer(
        "test_email_server",
        "test_email_login",
        "test_email_password"
    )?.let {
        systemProperties(
            mapOf(
                "test_email_server" to it.first,
                "test_email_login" to it.second,
                "test_email_password" to it.third
            )
        )
    }
}

resolveServer(
    "sonar_url",
    "sonar_user",
    "sonar_password"

)?.let {
    sonar {
        properties {
            properties(
                mapOf(
                    "sonar.host.url" to it.first,
                    "sonar.login" to it.second,
                    "sonar.password" to it.third,
                    "sonar.gradle.skipCompile" to true
                )
            )
        }
    }
}

