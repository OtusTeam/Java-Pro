plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-webclient")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation (libs.findbugs.jsr305)

    implementation("org.webjars:webjars-locator-core")
    implementation(libs.webjars.socksjs)
    implementation(libs.webjars.stomp)
    implementation(libs.webjars.bootstrap)
}


