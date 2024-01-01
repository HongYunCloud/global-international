plugins {
    java
    `java-library`
    `maven-publish`
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "io.github.hongyuncloud"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()

        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
            mavenContent { includeModule("^net\\.kyori\$", "^adventure.+\$") }
        }

        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
            mavenContent { includeModule("org.bukkit", "bukkit") }
        }

        maven("https://r.irepo.space/maven/") {
            mavenContent {
                includeModuleByRegex(
                    "^org\\.inksnow(\\.ankh-invoke.+|)\$",
                    "^(org.inksnow.|)ankh-invoke(-.+|)\$"
                )
            }
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        withSourcesJar()
    }

    publishing {
        repositories {
            if (System.getenv("CI").toBoolean()) {
                maven("https://r.bgp.ink/maven/") {
                    credentials {
                        username = System.getenv("R_BGP_INK_USERNAME")
                        password = System.getenv("R_BGP_INK_PASSWORD")
                    }
                }
            } else {
                maven(rootProject.layout.buildDirectory.dir("maven"))
            }
        }

        publications {
            create<MavenPublication>("mavenJar") {
                from(components["java"])
            }
        }
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:24.1.0")
        compileOnly("org.slf4j:slf4j-api:2.0.9")

        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

dependencies {
    api("it.unimi.dsi:fastutil:8.5.12")
    api("com.google.code.gson:gson:2.10.1")
    api("com.google.guava:guava:33.0.0-jre")

    api("com.ibm.icu:icu4j:74.2")
    api("com.ibm.icu:icu4j-localespi:74.2")

    implementation("net.kyori:adventure-api:4.16.0-SNAPSHOT")
    implementation("net.kyori:adventure-nbt:4.16.0-SNAPSHOT")
    implementation("net.kyori:adventure-text-serializer-gson:4.16.0-SNAPSHOT")
}