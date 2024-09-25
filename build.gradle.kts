plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.solafides"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
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

    // Spring Boot R2DBC Starter for using reactive database access with R2DBC
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    // Spring Boot WebFlux Starter for building reactive web applications with Spring WebFlux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // MongoDB chuchus
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    // Lombok for reducing boilerplate code in Java, like getters, setters, etc.
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // H2 database driver for in-memory testing
    runtimeOnly("com.h2database:h2")

    // MySQL Connector/J for JDBC-based access to MySQL (not required for R2DBC)
    runtimeOnly("com.mysql:mysql-connector-j")

    // R2DBC MySQL driver for reactive access to MySQL databases
    implementation("dev.miku:r2dbc-mysql:0.8.2.RELEASE")

    // Spring Boot Starter Test for testing Spring Boot applications
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Reactor Test for testing reactive components with Project Reactor
    testImplementation("io.projectreactor:reactor-test")

    // JUnit Platform Launcher for running tests from the IDE or build tools
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}


tasks.withType<Test> {
    useJUnitPlatform()
}
