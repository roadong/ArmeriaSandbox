plugins {
    id("org.springframework.boot") version("2.7.2")
    id("io.spring.dependency-management") version ("1.0.14.RELEASE")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:2.7.4")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-devtools")

    // jpa model generator(meta model entity)
    implementation("org.hibernate:hibernate-jpamodelgen:5.6.12.Final")
    annotationProcessor("org.hibernate:hibernate-jpamodelgen:5.6.12.Final")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework.security:spring-security-oauth2-authorization-server:0.3.1")
}