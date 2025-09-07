dependencies {
    api(libs.swissknife.openapi.provider)
    api(libs.swissknife.openapi.parser)
    api(libs.pillar.http.api.conventions)

    runtimeOnly(projects.modulesDepositEventEndpointAdaptersDrivingHttpEndpoint)
    runtimeOnly(projects.modulesPaymentCommandEndpointAdaptersDrivingHttpEndpoint)
    runtimeOnly(libs.pillar.json.serialization.web.api)

    testImplementation(libs.pillar.web.api.test.utils)
    testImplementation(libs.pillar.correlation.logging.test.utils)
    testImplementation(libs.pillar.openapi.rules)
    testImplementation(libs.swissknife.logging.standard.slf4j.configuration)
    testImplementation(libs.swissknife.test.utils)
    testImplementation(libs.swissknife.openapi.checking.test.utils)
    testImplementation(libs.swissknife.correlation.core.test.utils)
    testImplementation(libs.swissknife.resource.utils)
    testImplementation(libs.swissknife.openapi.validation.http4k.test.utils)
}