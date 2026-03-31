plugins {
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.sonarlint) apply false
    alias(libs.plugins.spotless) apply false
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    group = "ru.otus"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }
}