plugins {
    id("java")
}

group = "me.kmaxi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.github.goober:coordinate-transformation-library:1.1")

}

tasks.test {
    useJUnitPlatform()
}