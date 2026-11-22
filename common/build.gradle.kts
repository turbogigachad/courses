plugins {
    id("java-library")
}

dependencies {
    api("io.rest-assured:rest-assured:5.5.6")
    api("com.codeborne:selenide:7.18.2")
    api("io.qameta.allure:allure-java-commons:2.35.5")
    api("io.qameta.allure:allure-selenide:2.35.5")
    api("io.qameta.allure:allure-rest-assured:2.35.5")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
}

allure {
    version = "3.14.3"
    adapter {
        allureJavaVersion.set("2.35.5")
        aspectjVersion.set("1.9.25.1")
    }
    report {
        singleFile.set(true)
    }
}

tasks.test {
    useJUnitPlatform {
        excludeTags("fail")
    }
}

tasks.register<Test>("runFailingTests") {
    group = "verification"
    description = "Намеренно падающие тесты по заданию"
    useJUnitPlatform { includeTags("fail") }
}

tasks.register<Test>("runOnlyGreenTests") {
    group = "verification"
    description = "Unit-тесты без намеренно падающих проверок"
    useJUnitPlatform { excludeTags("fail", "bug", "requires_empty_catalog") }
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

tasks.register<Test>("runStringTests") {
    group = "verification"
    description = "Тесты с тегом string"
    useJUnitPlatform { includeTags("string") }
}
