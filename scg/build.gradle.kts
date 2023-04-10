plugins {
    id("io.spring.dependency-management") version ("1.0.14.RELEASE")
    id("org.springframework.boot") version("2.7.4")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.4")
    }
}

configurations {
    all {
//        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
        exclude(group = "commons-logging", module = "commons-logging")
        exclude(group = "log4j", module = "log4j")
    }
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:")
    implementation("org.springframework.cloud:spring-cloud-sleuth-zipkin:")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth:")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j:")
    implementation("org.projectlombok:lombok:1.18.24")
    annotationProcessor ("org.projectlombok:lombok:1.18.24")

    // 버전업 안됨 spring boot 3.x에서나 지원
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.slf4j:jcl-over-slf4j:1.7.36")

    // 버전업 안됨 (https://github.com/spring-projects/spring-boot/issues/12649)
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("ch.qos.logback:logback-core:1.2.11")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
    //testImplementation("io.projectreactor:reactor-test")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

