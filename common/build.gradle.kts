plugins {
    `java-convention`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
    alias(libs.plugins.lombok)
}

dependencies {
    implementation(libs.jakarta.persistence.api)
    implementation(libs.hibernate.core)
}

tasks {
    bootJar {
        enabled = false
    }
}
