dependencies {
    api(libs.swissknife.service.domain)
    api(libs.swissknife.pulsar.utils)

    implementation(projects.modulesPaymentCommandEndpointAdaptersDrivingHttpEndpoint)
    implementation(projects.modulesPaymentCommandEndpointAdaptersDrivenPulsar)
    implementation(projects.modulesPaymentCommandEndpointAdaptersDrivenNats)
    implementation(libs.swissknife.logger.core)

    implementation(libs.swissknife.kotlin.extensions)
}