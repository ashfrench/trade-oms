plugins {
    alias(myplugins.plugins.springboot)
    alias(myplugins.plugins.springbootdm)
    alias(myplugins.plugins.kotlin)
    alias(myplugins.plugins.kotlinspring)
}

group = "com.ash.trading.oms"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":data-model"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    implementation(libs.swagger)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}