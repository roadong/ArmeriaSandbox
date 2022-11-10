plugins {
    base
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "com.gl.springsandbox"
    version = "1.0-SNAPSHOT"
}

project(":sse") {
    apply(plugin = "java")
}

project(":scg") {
    apply(plugin = "java")
}

project(":api") {
    apply(plugin = "java")
}

project(":authorization_server") {
    apply(plugin = "java")
}
