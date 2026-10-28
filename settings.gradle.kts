plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "test"

include("L01-gradle")

//include("L02-GradleLogging")
//include("lib")

include("L08-gc:demo")
include("L08-gc:homework")

include("L20-hibernate")
include("L21-jpql:class-demo")
include("L24-webServer")
include("L25-di:class-demo")
include("L26-springBootMvc")
include("L32-concurrentCollections:ConcurrentCollections")
include("L32-concurrentCollections:QueueDemo")
include("L33-virtualThreads:base")
include("L33-virtualThreads:springBoot")
include("L34-multiprocess:processes-demo")
include("L34-multiprocess:sockets-demo")
include("L34-multiprocess:rmi-demo")
include("L34-multiprocess:grpc-demo")
include("L34-multiprocess:grpc-reader")
include("L34-multiprocess:grpc-writer")
include("L35-rabbitMQ:allServicesModels")
include("L35-rabbitMQ:approvalService")
include("L35-rabbitMQ:mainService")

include("L38-webflux:source")
include("L38-webflux:processor")
include("L38-webflux:client")
include("L38-webflux:reactor")
include("L38-webflux-chat:client-service")
include("L38-webflux-chat:datastore-service")
