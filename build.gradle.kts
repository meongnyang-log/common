plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("kapt") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("plugin.allopen") version "1.9.25"
    kotlin("plugin.noarg") version "1.9.25"
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.7"
    // 라이브러리 배포할 때 사용
    id("maven-publish")
}

val queryDslVersion = "5.1.0"

group = "com.back"
version = "1.0.38"

repositories {
    mavenCentral()
}


kotlin.sourceSets.main {
    kotlin.srcDir("$buildDir/generated/source/kapt/main") // Q클래스를 소스 경로에 포함
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
    /**
     * api = 외부 공개용
     *
     * implementation(), 컴파일 시, 런타임 시 필요하다
     * kapt(), 컴파일 시에만 필요하다
     * */
    api("com.querydsl:querydsl-jpa:$queryDslVersion:jakarta")
    api("com.querydsl:querydsl-kotlin:$queryDslVersion")
    kapt("com.querydsl:querydsl-apt:$queryDslVersion:jakarta")
    /**
     * QueryDSL 이 @Entity 같은 어노테이션을 인식하고 Q클래스를 생성하는 데 필요한 API를 제공
     * */
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

/**
 * QueryDSL 사용을 위한 설정
 * kapt에서 생성된 Q클래스를 JavaCompile 단계에서 처리할 때 querydsl-apt를 인식하도록 하여
 * 컴파일 실패나 Q클래스 누락을 방지한다
 * */
tasks.withType<JavaCompile> {
    options.annotationProcessorPath = configurations.kapt.get()
}

tasks.test {
    useJUnitPlatform()
}
/**
 * @Entity, @MappedSuperclass, @Embeddable 가 붙은 클래스들은 자동으로 open 키워드가 적용되어 JPA에서 사용할 수 있게 됨.
 * */
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

kotlin {
    jvmToolchain(21)
}

/**
 * Maven 아티팩트를 생성하여 GitHub Packages에 배포하는 설정
 * */
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
