plugins {
    application
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(libs.guava)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

application {
    mainClass = "ru.otus.App"
}

