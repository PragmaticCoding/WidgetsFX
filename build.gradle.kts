import java.net.URI

plugins {
    id("java-library")
//    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    kotlin("jvm") version "1.8.10"
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
    id("org.jetbrains.dokka") version "1.9.10"
    id("maven-publish")
    id("signing")
//    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "ca.pragmaticcoding"
val archivesBaseName = "WidgetsFX"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.9.2"
val user = findProperty("user")
val password = findProperty("password")
val nexusUrl = findProperty("nexusUrl")
println("Hello ${layout.buildDirectory}")


java {
    withSourcesJar()
}

tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}


kotlin {
    jvmToolchain(17)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

application {
    mainModule = "ca.pragmaticcoding.widgetsfx"
    mainClass = "ca.pragmaticcoding.widgetsfx.LabelBoxApplication"
}

javafx {
    version = "17.0.6"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.test {
    useJUnitPlatform()
}

jlink {
    imageZip = project.file("${layout.buildDirectory}/distributions/app-${javafx.platform.classifier}.zip")
    options = listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    launcher {
        name = "app"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "widgetsfx"
            artifact(tasks["dokkaJavadocJar"])
            pom {
                name = "WidgetsFX"
                description = "Tools for JavaFX development"
                url = "https://github.com/optimatika/okAlgo"

                scm {
                    connection = "scm:git:git://github.com/PragmaticCoding/WidgetsFX.git"
                    developerConnection = "scm:git:git@github.com:PragmaticCoding/WidgetsFX.git"
                    url = "https://github.com/PragmaticCoding/WidgetsFX"
                }

                licenses {
                    license {
                        name = "Apache License 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0"
                    }
                }
                developers {
                    developer {
                        name = "Dave Barrett"
                        email = "barrettda@gmail.com"
                    }
                }
            }
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = URI("${nexusUrl}/content/repositories/releases")
            val snapshotsRepoUrl = URI("${nexusUrl}/content/repositories/snapshots")
            url = if ((version as String).endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            println("Publishing: $user and $password to $url")
            credentials {
                username = "$user"
                password = "$password"
            }
        }
    }
}


signing {
    sign(publishing.publications["maven"])
}

