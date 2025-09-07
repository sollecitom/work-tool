dependencies {
    api(projects.sharedAccountDomainModel)
    api(libs.pillar.json.serialization.core)
    api(libs.pillar.json.serialization.ddd)

    implementation(libs.pillar.json.serialization.correlation.core)

    testImplementation(projects.sharedAccountDomainTestUtils)
    testImplementation(libs.pillar.json.serialization.test.utils)
    testImplementation(libs.swissknife.correlation.core.test.utils)
    testImplementation(libs.swissknife.core.test.utils)
    testImplementation(libs.swissknife.test.utils)
}
