dependencies {
    implementation(project(":common"))
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

tasks.register<Test>("smokeUiTests") {
    group = "verification"
    description = "Smoke UI-тесты с Allure"
    useJUnitPlatform {
        includeTags("smoke")
        excludeTags("fail", "bug")
    }
}

tasks.register<Test>("runUiTests") {
    group = "verification"
    description = "Все UI-тесты без известных багов"
    useJUnitPlatform {
        includeTags("ui")
        excludeTags("fail", "bug", "requires_empty_catalog")
    }
}

tasks.register<Test>("runOnlyGreenTests") {
    group = "verification"
    description = "UI-тесты без намеренно падающих и известных багов"
    useJUnitPlatform {
        includeTags("ui")
        excludeTags("fail", "bug", "requires_empty_catalog")
    }
}
