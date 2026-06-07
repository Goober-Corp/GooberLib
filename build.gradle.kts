plugins {
    java

    alias(libs.plugins.loom)
    `maven-publish`
}

group = "com.goobercorp"
version = "1.0.0"

val testmod by sourceSets.registering {
    compileClasspath += sourceSets.main.get().compileClasspath + sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().runtimeClasspath + sourceSets.main.get().output
}

loom {
    runs {
        register("testmod") {
            client()
            ideConfigGenerated(true)
            name("Test Mod")
            source(testmod.get())
        }
    }

    accessWidenerPath = file("src/main/resources/gooberlib.classtweaker")

    createRemapConfigurations(testmod.get())
}

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/")
}

val minecraftVersion: String by project
val loaderVersion: String by project

dependencies {
    modImplementation("com.terraformersmc:modmenu:17.0.0")
    minecraft("com.mojang:minecraft:${minecraftVersion}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${loaderVersion}")
    modImplementation(libs.fabric.api)
}

tasks {
    processResources {
        val modId: String by project
        val modName: String by project
        val modDescription: String by project

        inputs.property("id", modId)
        inputs.property("group", project.group)
        inputs.property("name", modName)
        inputs.property("description", modDescription)
        inputs.property("version", project.version)
        inputs.property("loader_version", loaderVersion)
        inputs.property("minecraft_version", minecraftVersion)

        filesMatching(listOf("fabric.mod.json")) {
            expand(
                "id" to modId,
                "group" to project.group,
                "name" to modName,
                "description" to modDescription,
                "version" to project.version,
                "loader_version" to loaderVersion,
                "minecraft_version" to minecraftVersion
            )
        }
    }

    remapJar {
        archiveClassifier.set("fabric-$minecraftVersion")
    }

    remapSourcesJar {
        archiveClassifier.set("fabric-$minecraftVersion-sources")
    }
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mod") {
            groupId = "com.goobercorp"
            val modId: String by project
            artifactId = modId

            from(components["java"])
            artifact(tasks["remapSourcesJar"])
        }
    }
}
