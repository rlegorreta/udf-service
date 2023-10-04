import org.gradle.internal.classpath.Instrumented.systemProperty
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.8.21"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    kotlin("plugin.jpa") version "1.8.21"
    kotlin("plugin.lombok") version "1.9.0"
    id("io.freefair.lombok") version "8.1.0"
}

group = "com.ailegorreta"
version = "2.0.0"
description = "Server repo for UDF database. udfdb"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/" +
        project.findProperty("registryPackageUrl") as String? ?:
            System.getenv("URL_PACKAGE") ?:
            "rlegorreta/ailegorreta-kit")
        credentials {
            username = project.findProperty("registryUsername") as String? ?:
                    System.getenv("USERNAME") ?:
                    "rlegorreta"
            password = project.findProperty("registryToken") as String? ?: System.getenv("TOKEN")
        }
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

extra["springCloudVersion"] = "2022.0.3"
extra["testcontainersVersion"] = "1.18.1"
extra["otelVersion"] = "1.26.0"
extra["ailegorreta-kit-version"] = "2.0.0"

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client") {
        exclude(group = "org.springframework.cloud", module = "spring-cloud-starter-ribbon")
        exclude(group = "com.netflix.ribbon", module = "ribbon-eureka")
    }

    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-streams")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")

    implementation("org.projectlombok:lombok")

    implementation("com.ailegorreta:ailegorreta-kit-commons-utils:${property("ailegorreta-kit-version")}")
    implementation("com.ailegorreta:ailegorreta-kit-resource-server-security:${property("ailegorreta-kit-version")}")
    implementation("com.ailegorreta:ailegorreta-kit-commons-event:${property("ailegorreta-kit-version")}")
    implementation("com.ailegorreta:ailegorreta-kit-data-jpa:${property("ailegorreta-kit-version")}")

    /* Flyway does not support R2DBC yet, so we need to provide a JDBC driver to communicate with the database */
    runtimeOnly("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.springframework:spring-jdbc")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder")
    testImplementation("com.squareup.okhttp3:mockwebserver")

    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.testcontainers:postgresql")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}


tasks.named<BootBuildImage>("bootBuildImage") {
    environment.set(environment.get() + mapOf("BP_JVM_VERSION" to "17.*"))
    imageName.set("ailegorreta/${project.name}")
    docker {
        publishRegistry {
            username.set(project.findProperty("registryUsername").toString())
            password.set(project.findProperty("registryToken").toString())
            url.set(project.findProperty("registryUrl").toString())
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        freeCompilerArgs += "-Xjvm-default=all-compatibility"			// needed to override default methods on interfaces
        jvmTarget = "17"
    }
}

kotlinLombok {
    lombokConfigurationFile(file("lombok.config"))
}

tasks.test {
    useJUnitPlatform()
}

allOpen {
    annotation("jakarta.persistence.Entity")
}