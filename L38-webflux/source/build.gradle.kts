plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-webflux")
    implementation (libs.findbugs.jsr305)

    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testImplementation ("org.springframework.boot:spring-boot-starter-webflux-test")
    testImplementation ("io.projectreactor:reactor-test")
}

tasks.test {
    useJUnitPlatform()
}
