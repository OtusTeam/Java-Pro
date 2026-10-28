plugins {
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.sonarlint) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.lombok) apply false
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "io.freefair.lombok")

    group = "ru.otus"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        "implementation"(platform(rootProject.libs.spring.dependencies))
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }
}
