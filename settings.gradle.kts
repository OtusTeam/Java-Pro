plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "test"

include("L01-gradle")

//include("L02-GradleLogging")
//include("lib")

include("L20-hibernate")
include("L21-jpql:class-demo")
include("L24-webServer")
include("L25-di:class-demo")
include("L26-springBootMvc")