import java.net.URI

plugins {
    id("java-library")
    kotlin("jvm") version "1.9.22"
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "3.0.1"
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.netflix.nebula.maven-publish") version "20.3.0"
//    id("maven-publish")
    id("signing")
//    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "ca.pragmaticcoding"
val archivesBaseName = "WidgetsFX"
//version = "0.1.0-SNAPSHOT"
version = "0.1.0"
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
    jvmToolchain(21)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "21"
    }
}

//application {
//    mainModule = "ca.pragmaticcoding.widgetsfx"
//    mainClass = "ca.pragmaticcoding.widgetsfx.LabelBoxApplication"
//}

javafx {
    version = "17.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.test {
    useJUnitPlatform()
}

//jlink {
//    imageZip = project.file("${layout.buildDirectory}/distributions/app-${javafx.platform.classifier}.zip")
//    options = listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
//    launcher {
//        name = "app"
//    }
//}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "widgetsfx"
            artifact(tasks["dokkaJavadocJar"])
            pom {
                name.value("WidgetsFX")
                description.value("Tools for JavaFX development")
                url.value("https://github.com/optimatika/okAlgo")

                scm {
                    connection.value("scm:git:git://github.com/PragmaticCoding/WidgetsFX.git")
                    developerConnection.value("scm:git:git@github.com:PragmaticCoding/WidgetsFX.git")
                    url.value("https://github.com/PragmaticCoding/WidgetsFX")
                }

                licenses {
                    license {
                        name.value("Apache License 2.0")
                        url.value("http://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        name.value("Dave Barrett")
                        email.value("barrettda@gmail.com")
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

