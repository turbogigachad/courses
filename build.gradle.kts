plugins {
    id("base")
    id("io.qameta.allure") version "4.3.0" apply false
}

allprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.qameta.allure")

    dependencies {
        "testImplementation"(platform("org.junit:junit-bom:5.10.0"))
        "testImplementation"("org.junit.jupiter:junit-jupiter")
        "testImplementation"("org.assertj:assertj-core:3.27.3")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
            events("passed", "skipped", "failed")
        }
    }
}

tasks.register("smokeApiTests") {
    group = "verification"
    description = "Smoke-прогон API-автотестов"
    dependsOn(":api-tests:smokeApiTests")
}

tasks.register("smokeUiTests") {
    group = "verification"
    description = "Smoke-прогон UI-автотестов"
    dependsOn(":ui-tests:smokeUiTests")
}

tasks.register("smokeTests") {
    group = "verification"
    description = "Общий Smoke-прогон API- и UI-автотестов"
    dependsOn("smokeApiTests", "smokeUiTests")
}

tasks.register("runOnlyGreenTests") {
    group = "verification"
    description = "Все тесты без намеренно падающих и известных багов"
    dependsOn(":common:runOnlyGreenTests", ":api-tests:runOnlyGreenTests", ":ui-tests:runOnlyGreenTests")
}

tasks.register("runAllTests") {
    group = "verification"
    description = "Все тесты всех модулей, включая намеренно падающие"
    dependsOn(":common:test", ":api-tests:test", ":ui-tests:test")
}

tasks.register("runUiAndApiTests") {
    group = "verification"
    description = "UI- и API-тесты без известных багов"
    dependsOn(":api-tests:runApiTests", ":ui-tests:runUiTests")
}

tasks.register("afterTests") {
    group = "verification"
    description = "Информация о завершении всех тестов"
    dependsOn("runAllTests")
    doLast {
        println("Test run is over")
    }
}
