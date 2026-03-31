plugins {
    application
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(libs.guava)
}

application {
    mainClass = "ru.otus.App"
}

