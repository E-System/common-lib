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
    id("com.github.ben-manes.versions") version "0.44.0"
    id("org.sonarqube") version "4.4.1.3373"
}

tasks.wrapper {
    gradleVersion = "8.4"
}

apply("build-${properties["profile"]}.gradle.kts")

group = extra["mainGroup"] as String
version = extra["mainVersion"] as String + (if (extra["snapshot"] as Boolean) "-SNAPSHOT" else "")

repositories {
    mavenCentral()
    mavenLocal()
}

var filePath = "${project.projectDir}/src/main/resources/com/eslibs/common/"
File(filePath).mkdirs()
filePath = "$filePath/build.properties"
println("Create file for property: $filePath")
val props = Properties()
props["name"] = rootProject.name
props["version"] = project.version
props["date"] = LocalDateTime.now().toString()
props.store(Files.newOutputStream(Paths.get(filePath)), null)

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
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.ADOPTIUM
    }
    withSourcesJar()
    withJavadocJar()
}
publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
}
val jacksonVersion = "2.15.3"
dependencies {
    api("org.apache.commons:commons-lang3:3.13.0")
    api("commons-io:commons-io:2.14.0")
    api("org.slf4j:slf4j-api:2.0.9")
    api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    api("org.eclipse.angus:angus-mail:2.0.2")

    api("de.svenkubiak:jBCrypt:0.4.3")
    api("org.reflections:reflections:0.10.2")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonVersion}")
}

val emailTestEnabled = properties["test_email_server"] != null && properties["test_email_login"] != null && properties["test_email_password"] != null
if (emailTestEnabled) {
    tasks.test {
        if (emailTestEnabled) {
            systemProperties(mapOf(
                    "test_email_server" to properties["test_email_server"],
                    "test_email_login" to properties["test_email_login"],
                    "test_email_password" to properties["test_email_password"]
            ))
        }
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

val sonarAvailable = properties["sonar_url"] != null && properties["sonar_user"] != null && properties["sonar_password"] != null
if (sonarAvailable) {
    val url = properties["sonar_url"] as String
    val user = properties["sonar_user"] as String
    val pass = properties["sonar_password"] as String
    sonar {
        properties {
            properties(mapOf(
                    "sonar.host.url" to url,
                    "sonar.login" to user,
                    "sonar.password" to pass,
                    "sonar.gradle.skipCompile" to true
            ))
        }
    }
}
