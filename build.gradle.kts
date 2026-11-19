plugins {
    id("java")
    id("io.qameta.allure") version "4.3.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.3")
    implementation("io.rest-assured:rest-assured:5.5.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("com.codeborne:selenide:7.18.2")
    implementation("io.qameta.allure:allure-java-commons:2.35.5")
    implementation("io.qameta.allure:allure-selenide:2.35.5")
    implementation("io.qameta.allure:allure-rest-assured:2.35.5")
}

allure {
    version = "3.14.3"
    adapter {
        allureJavaVersion.set("2.35.5")
        aspectjVersion.set("1.9.25.1")
        resultsDir.set(layout.buildDirectory.dir("allure-results"))
    }
    report {
        singleFile.set(true)
    }
}

tasks.test {
    useJUnitPlatform {
        excludeTags("fail")
    }
    testLogging {
        showStandardStreams = true
        events("passed", "skipped", "failed")
    }
}

tasks.register<Test>("runFailingTests") {
    group = "verification"
    description = "Падающие тесты(по заданию)"
    useJUnitPlatform { includeTags("fail") }
}

tasks.register<Test>("runOnlyGreenTests") {
    group = "verification"
    description = "Прогон без падающих тестов и без багов приложения"
    useJUnitPlatform { excludeTags("fail", "bug", "requires_empty_catalog") }
}

tasks.register<Test>("runAllTests") {
    group = "verification"
    description = "UI+API все тесты с аллюром"
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        events("passed", "skipped", "failed")
    }
}

tasks.register<Test>("runUiAndApiTests") {
    group = "verification"
    description = "UI+API тесты без багов с аллюром"
    useJUnitPlatform {
        includeTags("ui", "api")
        excludeTags("fail", "bug", "requires_empty_catalog")
    }
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

tasks.register<Test>("runBooleanTests") {
    group = "verification"
    description = "Тесты с тегом boolean"
    useJUnitPlatform { includeTags("boolean") }
}

tasks.register<Test>("runListTests") {
    group = "verification"
    description = "Тесты с тегом list"
    useJUnitPlatform { includeTags("list") }
}

tasks.register<Test>("runApiTests") {
    group = "verification"
    description = "Все API-тесты"
    useJUnitPlatform {
        includeTags("api")
        excludeTags("fail", "bug")
    }
}

tasks.register<Test>("runStringTests") {
    group = "verification"
    description = "Тесты с тегом string"
    useJUnitPlatform { includeTags("string") }
}