plugins {
    `java-convention`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
    alias(libs.plugins.spotless)
    alias(libs.plugins.lombok)
    jacoco
}

dependencies {
    implementation(project(":common"))
    implementation(project(":security"))

    implementation(libs.springboot.starter)
    implementation(libs.springboot.starter.web)
    implementation(libs.springboot.starter.jpa)
    implementation(libs.springboot.starter.validation)
    implementation(libs.openapi)
    implementation(libs.mapstruct)

    runtimeOnly(libs.postgresql)

    annotationProcessor(libs.mapstruct.processor)

    testImplementation(libs.springboot.starter.test)

    testRuntimeOnly(libs.junit)
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

tasks {
    bootJar {
        enabled = false
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    jacocoTestReport {
        finalizedBy(jacocoTestCoverageVerification)

        reports {
            html.required = true
        }
    }

    jacocoTestCoverageVerification {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it) {
                include(
                    "**/com/zadziarnouski/habitordie/**/service/**",
                )
            }
        }))

        violationRules {
            rule {
                limit {
                    minimum = 0.80.toBigDecimal()
                }
            }
        }
    }
}
