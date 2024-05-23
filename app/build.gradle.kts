plugins {
    `java-convention`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
}

group = "com.zadziarnouski"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":habit"))
    implementation(project(":security"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    jar {
        enabled = false
    }
}
