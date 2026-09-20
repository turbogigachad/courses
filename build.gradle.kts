plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.rest-assured:rest-assured:5.5.6")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        events("passed", "skipped", "failed")
    }
}

tasks.register("runAllTests") {
    group = "verification"
    description = "Запуск всех тестов"
    dependsOn(tasks.test)
}

tasks.register("afterTests") {
    group = "verification"
    description = "Информация о завершении теста"
    dependsOn("runAllTests")
    doLast {
        println("Test run is over")
    }
}