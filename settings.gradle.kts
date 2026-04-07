plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "test"

include("L01-gradle")

include("L02-GradleLogging")
include("lib")