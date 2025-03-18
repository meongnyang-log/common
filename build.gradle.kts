plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("kapt") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("plugin.allopen") version "1.9.25"
    kotlin("plugin.noarg") version "1.9.25"
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("maven-publish")
}

val queryDslVersion = "5.1.0"

group = "com.back"
version = "1.0.29"

repositories {
    mavenCentral()
}

kotlin.sourceSets.main {
    kotlin.srcDir("$buildDir/generated/source/kapt/main")
}

tasks.bootJar { enabled = false }
tasks.jar {
    enabled = true
    dependsOn("kaptKotlin")

    // QClass 파일들을 jar에 포함
    from(sourceSets.main.get().output)
    from(file("build/generated/source/kapt/main"))
}

dependencies {
    api("com.querydsl:querydsl-jpa:$queryDslVersion:jakarta")
    api("com.querydsl:querydsl-kotlin:$queryDslVersion")
    kapt("com.querydsl:querydsl-apt:$queryDslVersion:jakarta")
    kapt("jakarta.persistence:jakarta.persistence-api")

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

tasks.withType<JavaCompile> {
    options.annotationProcessorPath = configurations.kapt.get()
}

tasks.test {
    useJUnitPlatform()
}
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
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
