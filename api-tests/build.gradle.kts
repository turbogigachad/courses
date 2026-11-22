dependencies {
    implementation(project(":common"))
    implementation("org.assertj:assertj-core:3.27.3")
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

tasks.register<Test>("smokeApiTests") {
    group = "verification"
    description = "Smoke API-тесты с Allure"
    useJUnitPlatform {
        includeTags("smoke")
        excludeTags("fail", "bug", "requires_empty_catalog")
    }
}

tasks.register<Test>("runApiTests") {
    group = "verification"
    description = "Все API-тесты без известных багов"
    useJUnitPlatform {
        includeTags("api")
        excludeTags("fail", "bug", "requires_empty_catalog")
    }
}

tasks.register<Test>("runOnlyGreenTests") {
    group = "verification"
    description = "API-тесты без намеренно падающих и известных багов"
    useJUnitPlatform {
        includeTags("api")
        excludeTags("fail", "bug", "requires_empty_catalog")
    }
}
