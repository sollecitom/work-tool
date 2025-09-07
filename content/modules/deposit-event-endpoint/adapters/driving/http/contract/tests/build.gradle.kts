dependencies {
    testImplementation(projects.modulesDepositEventEndpointAdaptersDrivingHttpEndpoint)
    testImplementation(projects.modulesDepositEventEndpointAdaptersDrivingHttpSerializationJson)
    testImplementation(projects.modulesDepositEventEndpointDomainTestUtils)
    testImplementation(projects.contractsHttp)
    testImplementation(libs.swissknife.logging.standard.slf4j.configuration)
    testImplementation(libs.swissknife.test.utils)
    testImplementation(libs.pillar.web.api.test.utils)
    testImplementation(libs.swissknife.openapi.checking.test.utils)
    testImplementation(libs.swissknife.correlation.core.test.utils)
    testImplementation(libs.pillar.correlation.logging.test.utils)
    testImplementation(libs.swissknife.resource.utils)
    testImplementation(libs.swissknife.openapi.validation.http4k.test.utils)
}