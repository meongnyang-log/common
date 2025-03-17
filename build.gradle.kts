plugins {
    kotlin("jvm") version "1.9.25"
    id("maven-publish")
}

group = "com.back"
version = "1.0.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.3")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-security:3.4.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.3")
    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    testImplementation("org.springframework.security:spring-security-test:3.4.3")

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
