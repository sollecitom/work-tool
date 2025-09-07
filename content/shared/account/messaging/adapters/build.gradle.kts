dependencies {
    api(libs.swissknife.messaging.domain)
    api(libs.swissknife.pulsar.utils)
    api(projects.sharedAccountDomainModel)

    implementation(projects.sharedAccountMessagingConverter)
    implementation(projects.sharedAccountSerializationAvro)
    implementation(libs.swissknife.pulsar.messaging.adapter)
    implementation(libs.pillar.messaging.pulsar.avro)
    implementation(libs.swissknife.pulsar.messaging.adapter)
    implementation(libs.pillar.messaging.conventions)
    implementation(libs.pillar.correlation.logging.utils)
    implementation(libs.swissknife.logger.core)
    implementation(projects.sharedSerializationAvro)
    implementation(libs.pillar.messaging.conventions)
}