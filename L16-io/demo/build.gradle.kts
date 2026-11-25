plugins {
    id("com.google.protobuf") version "0.10.1"
    id("idea")
}

dependencies {
    implementation ("ch.qos.logback:logback-classic")
    implementation("com.google.guava:guava:33.4.0-jre")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.glassfish:jakarta.json:2.0.1")
    implementation("com.google.protobuf:protobuf-java")
    implementation("com.google.protobuf:protobuf-java-util")
    implementation("com.google.errorprone:error_prone_annotations")
    implementation("com.google.j2objc:j2objc-annotations:3.0.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.google.code.gson:gson")

    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")
}

val protoSrcDir = "$projectDir/build/generated"

idea {
    module {
        sourceDirs = sourceDirs.plus(file(protoSrcDir))
    }
}

protobuf {
    val protobufVersion = project.property("protobufVersion") as String

    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }

    generateProtoTasks {
        ofSourceSet("main")
    }
}
