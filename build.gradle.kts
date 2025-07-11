plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.astondev"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    // включить log4j
    {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")

    // Log4j2
    implementation("org.springframework.boot:spring-boot-starter-log4j2")

    //jackson
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Annotation processor for Log4j plugin processing
    annotationProcessor("org.apache.logging.log4j:log4j-core")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
