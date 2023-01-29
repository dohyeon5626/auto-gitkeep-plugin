plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
    id("org.jetbrains.intellij") version "1.4.0"
}

group = "com.dohyeon5626"
version = "1.0"

repositories {
    mavenCentral()
}

intellij {
    version.set("2021.2")
    type.set("IC")

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    patchPluginXml {
        sinceBuild.set("212")
        untilBuild.set("222.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
