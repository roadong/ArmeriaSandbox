plugins {
    id("org.springframework.boot") version("2.7.2")
    id("io.spring.dependency-management") version ("1.0.14.RELEASE")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:2.7.4")
    }
}

configurations {
    all {
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
        exclude(group = "commons-logging", module = "commons-logging")
        exclude(group = "log4j", module = "log4j")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-devtools")
//    implementation("org.springframework.boot:spring-boot-starter-logging")
    // aop
    implementation("org.springframework.boot:spring-boot-starter-aop")

//    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-authorization-server:0.3.1")

//    compileOnly("javax.xml.bind:jaxb-api")
//    compileOnly("jakarta.xml.bind:jakarta.xml.bind-api")

    //db
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.h2database:h2")
    //hibernate 6 - spring boot 3.x
    //https://stackoverflow.com/questions/73257636/using-hibernate-6-x-with-spring-boot-2-7-x-not-working
    implementation("org.hibernate:hibernate-jpamodelgen:5.6.12.Final")
    annotationProcessor("org.hibernate:hibernate-jpamodelgen:5.6.12.Final")
    annotationProcessor("javax.annotation:javax.annotation-api")
    annotationProcessor("javax.persistence:javax.persistence-api")



    // slf4j 2.x - springboot 3.x
    // https://github.com/spring-projects/spring-boot/issues/12649
//    implementation("org.slf4j:slf4j-api:1.7.36")
//    implementation("org.slf4j:slf4j-simple:1.7.36")
//    implementation("org.slf4j:jcl-over-slf4j:1.7.36")
//    implementation("ch.qos.logback:logback-classic:1.4.4") {
//        exclude(group = "org.slf4j", module = "slf4j-api")
//    }
//    implementation("ch.qos.logback:logback-core:1.4.4")

    //lombok
    implementation("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")


}

//sourceSets {
//    main {
//        java {
//            srcDirs("src/main/java")
//        }
//        resources {
//            srcDirs("src/main/resources")
//        }
//    }
//    test {
//        java {
//            srcDirs("src/test/java")
//        }
//        resources {
//            srcDirs("src/test/resources")
//        }
//    }
//}