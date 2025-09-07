dependencies {
    api(projects.modulesPaymentCommandEndpointDomainModel)
    api(libs.swissknife.correlation.core.domain)
    api(libs.swissknife.service.domain)
    api(libs.swissknife.pulsar.utils)
    api(libs.swissknife.messaging.domain)

    implementation(projects.sharedAccountMessagingConverter)
    implementation(libs.swissknife.logger.core)
}