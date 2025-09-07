dependencies {
    api(libs.swissknife.service.domain)
    api(libs.swissknife.pulsar.utils)

    implementation(projects.modulesDepositEventEndpointAdaptersDrivingHttpEndpoint)
    implementation(projects.modulesDepositEventEndpointAdaptersDrivenPulsar)
    implementation(libs.swissknife.logger.core)

    implementation(libs.swissknife.kotlin.extensions)
}