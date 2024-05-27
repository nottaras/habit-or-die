plugins {
    `java-convention`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
    alias(libs.plugins.flyway)
}

group = "com.zadziarnouski"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":habit"))
    implementation(project(":security"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.flywaydb:flyway-core:10.13.0")
    implementation("org.flywaydb:flyway-database-postgresql:10.13.0")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    jar {
        enabled = false
    }
}

flyway {
    url = "jdbc:postgresql://localhost:5432/habitordie"
    user = "postgres"
    password = "postgres"
}
