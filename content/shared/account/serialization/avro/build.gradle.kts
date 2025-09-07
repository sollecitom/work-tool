dependencies {
    api(projects.sharedAccountDomainModel)
    api(libs.swissknife.correlation.core.domain)
    api(libs.pillar.avro.serialization.core)
    api(libs.pillar.avro.serialization.ddd)
    api(libs.swissknife.avro.serialization.utils)
    api(libs.swissknife.avro.schema.catalogue.domain)
    api(libs.pillar.messaging.avro)

    implementation(projects.sharedSerializationAvro)
    implementation(libs.pillar.avro.serialization.correlation.core)
    implementation(libs.swissknife.kotlin.extensions)
    implementation(libs.acme.schema.catalogue.avro.modulith.example)
    implementation(libs.acme.schema.catalogue.avro.common)

    testImplementation(projects.sharedAccountDomainTestUtils)
    testImplementation(libs.pillar.avro.serialization.test.utils)
    testImplementation(libs.swissknife.correlation.core.test.utils)
    testImplementation(libs.swissknife.avro.schema.catalogue.test.utils)
    testImplementation(libs.swissknife.ddd.test.utils)
    testImplementation(libs.swissknife.core.test.utils)
    testImplementation(libs.swissknife.test.utils)
}
