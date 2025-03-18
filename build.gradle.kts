plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("maven-publish")
}

group = "com.back"
version = "1.0.14"

repositories {
    mavenCentral()
}

tasks.bootJar { enabled = false }
tasks.jar { enabled = true }

dependencies {
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-data-jpa")

    // JWT
    api("io.jsonwebtoken:jjwt-api:0.11.5")
    api("io.jsonwebtoken:jjwt-impl:0.11.5")
    api("io.jsonwebtoken:jjwt-jackson:0.11.5")

    testImplementation("org.springframework.security:spring-security-test")

    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create("auth", MavenPublication::class) {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = "common"
            version = project.version.toString()
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/meongnyang-log/common")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
