plugins {
    base
    idea
    java
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "com.gl.springsandbox"
    version = "1.0-SNAPSHOT"
}

project(":sse") {
    apply(plugin = "java")
}

project(":scg") {
    apply(plugin = "java")
}