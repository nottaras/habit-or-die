plugins {
    `java-convention`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
}

group = "com.zadziarnouski"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":security"))
    implementation(project(":user"))

    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

project.layout.buildDirectory = file("../build")

tasks {
    jar {
        enabled = false
    }
}
