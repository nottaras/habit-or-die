plugins {
    `java-convention`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
    alias(libs.plugins.flyway)
    alias(libs.plugins.spotless)
}

dependencies {
    implementation(project(":habit"))
    implementation(project(":security"))

    implementation(libs.springboot.starter)
    implementation(libs.springboot.starter.jpa)
    implementation(libs.bundles.flyway)
    implementation(libs.springboot.devtools)
    implementation(libs.springboot.starter.actuator)

    runtimeOnly(libs.micrometer)

    testImplementation(libs.springboot.starter.test)

    testRuntimeOnly(libs.junit)
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

spotless {
    java {
        palantirJavaFormat()

        toggleOffOn()
        importOrder()
        endWithNewline()
        formatAnnotations()
        removeUnusedImports()
        trimTrailingWhitespace()
    }
}
