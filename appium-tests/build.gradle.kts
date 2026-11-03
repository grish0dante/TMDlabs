plugins {
    kotlin("jvm")
    id("java-library")
}

dependencies {
    implementation(kotlin("stdlib"))

    // Використовуємо оновлену сумісну версію Appium Java Client
    implementation("io.appium:java-client:9.2.2")

    // JUnit 5 (Jupiter) для запуску тестів
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.platform:junit-platform-launcher:1.10.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}