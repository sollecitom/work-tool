dependencies {
    api(projects.sharedAccountDomainModel)
    api(projects.sharedAccountSerializationAvro)
    api(projects.sharedMessagingTopics)
    api(libs.swissknife.messaging.domain)

    implementation(projects.sharedSerializationAvro)
    implementation(libs.pillar.messaging.conventions)
}