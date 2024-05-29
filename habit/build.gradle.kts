plugins {
    `java-convention`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
    alias(libs.plugins.lombok)
    jacoco
    alias(libs.plugins.spotless)
}

group = rootProject.group
version = rootProject.version


dependencies {
    implementation(project(":common"))
    implementation(project(":security"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")

    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
