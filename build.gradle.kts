plugins {
    //alias(libs.plugins.jgitver)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.sonarlint) apply false
    alias(libs.plugins.spotless) apply false
}

allprojects {
    group = "ru.otus"

    repositories {
        mavenLocal()
        mavenCentral()
    }


    apply(plugin = "io.spring.dependency-management")
}