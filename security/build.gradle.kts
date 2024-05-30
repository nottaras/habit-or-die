plugins {
    `java-convention`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
    alias(libs.plugins.spotless)
    alias(libs.plugins.lombok)
}

dependencies {
    implementation(project(":common"))

    implementation(libs.springboot.starter.web)
    implementation(libs.springboot.starter.jpa)
    implementation(libs.springboot.starter.security)
    implementation(libs.springboot.starter.validation)
    implementation(libs.openapi)
    implementation(libs.bundles.jjwt)

    testImplementation(libs.springboot.starter.test)
    testImplementation(libs.springboot.security.test)

    testRuntimeOnly(libs.junit)
}

tasks {
    bootJar {
        enabled = false
    }
}

spotless {
    java {
        palantirJavaFormat()

        toggleOffOn()
        importOrder()
        endWithNewline()
        formatAnnotations().addTypeAnnotation("NotEmpty").addTypeAnnotation("DurationMin")
        removeUnusedImports()
        trimTrailingWhitespace()
    }
}

