plugins {
    java
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_21
}

tasks {
    test {
        useJUnitPlatform()
    }
}
