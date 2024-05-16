plugins {
    `java-convention`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
}

group = rootProject.group
version = rootProject.version


dependencies {
    implementation(project(":security"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-graphql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    bootJar {
        enabled = false
    }
}
