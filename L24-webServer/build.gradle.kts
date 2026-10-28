val jetty = libs.versions.jetty.get()
val freemarker = libs.versions.freemarker.get()

dependencies {
    implementation("ch.qos.logback:logback-classic")
    implementation("com.google.code.gson:gson")

    implementation("org.eclipse.jetty.ee10:jetty-ee10-servlet:$jetty")
    implementation("org.eclipse.jetty:jetty-server:$jetty")
    implementation("org.eclipse.jetty.ee10:jetty-ee10-webapp:$jetty")
    implementation("org.eclipse.jetty:jetty-security:$jetty")
    implementation("org.eclipse.jetty:jetty-http:$jetty")
    implementation("org.eclipse.jetty:jetty-io:$jetty")
    implementation("org.eclipse.jetty:jetty-util:$jetty")
    implementation("org.freemarker:freemarker:$freemarker")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
}
