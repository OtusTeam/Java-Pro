plugins {
    application
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(libs.guava)
    implementation(project(":lib"))

    implementation("org.slf4j:slf4j-api:2.0.9")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.11")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "ru.otus.Main"
}

tasks.test {
    useJUnitPlatform()
}