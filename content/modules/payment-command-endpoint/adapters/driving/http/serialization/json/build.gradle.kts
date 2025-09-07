dependencies {
    api(projects.modulesPaymentCommandEndpointDomainModel)
    api(projects.sharedSerializationJson)
    api(libs.pillar.json.serialization.core)
    api(libs.pillar.json.serialization.ddd)

    implementation(libs.pillar.json.serialization.correlation.core)
    implementation(libs.pillar.json.serialization.ddd)
    implementation(libs.acme.schema.catalogue.json.common)

    testImplementation(projects.sharedAccountDomainTestUtils)
    testImplementation(libs.pillar.json.serialization.test.utils)
    testImplementation(libs.swissknife.ddd.test.utils)
    testImplementation(libs.swissknife.correlation.core.test.utils)
    testImplementation(libs.swissknife.core.test.utils)
    testImplementation(libs.swissknife.test.utils)
}
