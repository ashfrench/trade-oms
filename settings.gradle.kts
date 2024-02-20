plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
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
