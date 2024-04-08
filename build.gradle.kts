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
    id("com.github.ben-manes.versions") version "0.50.0"
    id("org.sonarqube") version "4.4.1.3373"
}

tasks.wrapper {
    gradleVersion = "8.7"
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
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
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
val jacksonVersion = "2.17.0"
dependencies {
    api("org.apache.commons:commons-lang3:3.14.0")
    api("commons-io:commons-io:2.16.0")
    api("org.slf4j:slf4j-api:2.0.12")
    api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    api("org.eclipse.angus:angus-mail:2.0.3")

    api("de.svenkubiak:jBCrypt:0.4.3")
    api("org.reflections:reflections:0.10.2")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")
    testImplementation("org.apache.groovy:groovy:4.0.20")
    testImplementation("ch.qos.logback:logback-classic:1.5.3")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonVersion}")
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
