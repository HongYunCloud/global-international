import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.inksnow.ankhinvoke.gradle.BuildMappingsTask

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.inksnow.ankh-invoke-gradle-plugin") version "1.0.8-SNAPSHOT"
}

configurations {
    create("runtimeShadow")
    create("singleShadow") {
        extendsFrom(configurations["runtimeShadow"])
    }
}

dependencies {
    api(project(":")) {
        exclude("net.kyori", "adventure-api")
        exclude("net.kyori", "adventure-nbt")
        exclude("net.kyori", "adventure-text-serializer-gson")
    }
    api(project(":netty")) {
        exclude("net.kyori", "adventure-api")
        exclude("net.kyori", "adventure-nbt")
        exclude("net.kyori", "adventure-text-serializer-gson")
    }

    compileOnly("org.inksnow:ankh-invoke-bukkit:1.0.8-SNAPSHOT")

    compileOnly("org.bukkit:bukkit:1.12.2-R0.1-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.1.104.Final")

    "runtimeShadow"("org.inksnow:ankh-invoke-bukkit:1.0.8-SNAPSHOT")
    "runtimeShadow"("net.kyori:adventure-api:4.16.0-SNAPSHOT") {
        exclude("net.kyori", "examination-api")
        exclude("net.kyori", "examination-string")
    }
    "runtimeShadow"("net.kyori:adventure-nbt:4.16.0-SNAPSHOT") {
        exclude("net.kyori", "examination-api")
        exclude("net.kyori", "examination-string")
    }
    "runtimeShadow"("net.kyori:adventure-text-serializer-gson:4.16.0-SNAPSHOT") {
        exclude("com.google.code.gson", "gson")
        exclude("net.kyori", "examination-api")
        exclude("net.kyori", "examination-string")
    }

    "singleShadow"(project(":")) {
        exclude("net.kyori", "adventure-api")
        exclude("net.kyori", "adventure-nbt")
        exclude("net.kyori", "adventure-text-serializer-gson")

        exclude("it.unimi.dsi", "fastutil")
        exclude("com.google.code.gson", "gson")
        exclude("com.google.guava", "guava")
    }
    "singleShadow"(project(":netty")) {
        exclude("net.kyori", "adventure-api")
        exclude("net.kyori", "adventure-nbt")
        exclude("net.kyori", "adventure-text-serializer-gson")

        exclude("it.unimi.dsi", "fastutil")
        exclude("com.google.code.gson", "gson")
        exclude("com.google.guava", "guava")
    }
}

tasks.shadowJar {
    enabled = false
}

tasks.create<BuildMappingsTask>("build-mappings") {
    ankhInvokePackage = "io.github.hongyuncloud.gi.ankhinvoke"

    registryName = "global-international"
    outputDirectory = layout.buildDirectory.file("cache/build-mappings").get().asFile

    mapping("nms", "1.20.2") {
        predicates = arrayOf("craftbukkit_version:{v1_20_R2}")
    }
}

tasks.processResources {
    from(tasks.getByName("build-mappings").outputs)
}

tasks.create<ShadowJar>("runtimeShadowJar") {
    dependsOn(tasks.jar)

    configurations = listOf(project.configurations["runtimeShadow"])
    archiveClassifier.set("runtime")
    from(tasks.jar)

    mergeServiceFiles()

    exclude("META-INF/versions/*/module-info.class")

    relocate("org.objectweb.asm", "io.github.hongyuncloud.gi.asm")
    relocate("bot.inker.acj", "io.github.hongyuncloud.gi.acj")
    relocate("net.kyori.adventure", "io.github.hongyuncloud.gi.adventureapi.adventure")
    relocate("net.kyori.option", "io.github.hongyuncloud.gi.adventureapi.option")
    relocate("org.inksnow.ankhinvoke", "io.github.hongyuncloud.gi.ankhinvoke")
}

tasks.create<ShadowJar>("singleShadowJar") {
    dependsOn(tasks.jar)

    configurations = listOf(project.configurations["singleShadow"])
    archiveClassifier.set("single")
    from(tasks.jar)

    mergeServiceFiles()

    exclude("LICENSE")
    exclude("META-INF/maven/**")
    exclude("**/module-info.class")
    exclude("META-INF/services/java.*")

    relocate("com.ibm.icu", "io.github.hongyuncloud.gi.icu")
    relocate("org.objectweb.asm", "io.github.hongyuncloud.gi.asm")
    relocate("bot.inker.acj", "io.github.hongyuncloud.gi.acj")
    relocate("net.kyori.adventure", "io.github.hongyuncloud.gi.adventureapi.adventure")
    relocate("net.kyori.option", "io.github.hongyuncloud.gi.adventureapi.option")
    relocate("org.inksnow.ankhinvoke", "io.github.hongyuncloud.gi.ankhinvoke")
}

publishing {
    publications {
        getByName<MavenPublication>("mavenJar") {
            artifact(tasks["runtimeShadowJar"]) {
                classifier = "runtime"
            }
            artifact(tasks["singleShadowJar"]) {
                classifier = "single"
            }
        }
    }
}

tasks.assemble {
    dependsOn(tasks.getByName("runtimeShadowJar"))
    dependsOn(tasks.getByName("singleShadowJar"))
}
