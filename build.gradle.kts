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

    // Apache POI for working with Excel files
    implementation ("org.apache.poi:poi:5.2.2")
    implementation( "org.apache.poi:poi-ooxml:5.2.2")
    runtimeOnly ("mysql:mysql-connector-java:8.0.29")

    implementation ("org.apache.logging.log4j:log4j-core:2.14.1")
    implementation ("org.apache.logging.log4j:log4j-api:2.14.1")

}

tasks.test {
    useJUnitPlatform()
}