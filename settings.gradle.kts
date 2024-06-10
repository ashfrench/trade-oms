plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("swagger", "org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
            library("kotlinxjson", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0")
        }

        create("myplugins") {
            plugin("springboot", "org.springframework.boot").version("3.3.0")
            plugin("springbootdm", "io.spring.dependency-management").version("1.1.5")
            plugin("kotlin", "org.jetbrains.kotlin.jvm").version("2.0.0")
            plugin("kotlinspring", "org.jetbrains.kotlin.plugin.spring").version("2.0.0")
        }
    }
}

rootProject.name = "trade-oms"
include("data-model")
include("trade-service")
include("trade-order-service")
include("order-service")
include("fix-service")
include("admin-service")
include("database-service")
include("reporting-service")
include("authentication-service")
include("oms-testing")
