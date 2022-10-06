plugins {
    id("org.springframework.boot") version("2.7.2")
}



dependencies {
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    //import bom
    implementation(platform("com.linecorp.armeria:armeria-bom:1.18.0"))

    // netty
    implementation(platform("io.netty:netty-bom:4.1.80.Final"))

    // spring boot
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-security:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-aop:2.7.3")

    // armeria
    implementation("com.linecorp.armeria:armeria-spring-boot2-webflux-starter")
    implementation("com.linecorp.armeria:armeria")
//    implementation("com.linecorp.armeria:armeria-grpc")
    implementation("com.linecorp.armeria:armeria-brave")
//    implementation("com.linecorp.armeria:armeria-protobuf")
    implementation("com.linecorp.armeria:armeria-logback")
//    implementation("com.linecorp.armeria:armeria-oauth2")
    implementation("com.linecorp.armeria:armeria-reactor3")

    // lombok
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    // reactor
    implementation("io.projectreactor.netty:reactor-netty:1.0.22")

    // lettuce
    implementation("io.lettuce:lettuce-core:6.2.0.RELEASE")

    // test
    testImplementation("io.projectreactor:reactor-test:3.4.22")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jmock:jmock-junit4:2.12.0")
    testImplementation("org.mockito:mockito-core:4.7.0")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    testImplementation("org.mockito:mockito-inline:4.7.0")
}



//test {
//    useJUnitPlatform()
//}