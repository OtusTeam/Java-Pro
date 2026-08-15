import com.google.protobuf.gradle.id

plugins {
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
  alias(libs.plugins.protobuf)
}

dependencies {
  implementation(libs.protobuf.java)
  implementation(libs.protobuf.java.util)
  implementation(libs.grpc.netty)
  implementation(libs.grpc.protobuf)
  implementation(libs.grpc.stub)
}

protobuf {
  protoc {
    artifact = libs.protoc.get().toString()
  }

  plugins {
    id("grpc") {
      artifact = libs.protocgen.grpc.java.get().toString()
    }
  }

  generateProtoTasks {
    all().forEach {
      it.plugins {
        id("grpc")
      }
    }
  }
}