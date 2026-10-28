plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(project(":L35-rabbitMQ:allServicesModels"))

    implementation("ch.qos.logback:logback-classic")
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation ("jakarta.persistence:jakarta.persistence-api")
}
