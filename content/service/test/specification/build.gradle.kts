dependencies {
    api(libs.swissknife.web.service.domain)
    api(libs.pillar.web.api.test.utils)
    api(libs.swissknife.test.utils)
    api(libs.swissknife.correlation.core.test.utils)
    api(libs.swissknife.ddd.test.utils)
    api(libs.swissknife.kotlin.extensions)
    api(libs.swissknife.sql.postgres.container)
    api(libs.swissknife.sql.postgres.utils)
    api(libs.swissknife.web.openapi.test.utils)
    api(projects.migratorPostgresDomain)
    api(platform(libs.http4k.bom))
    api(libs.http4k.client.apache.async)
    api(libs.pillar.service.logging)
    api(libs.swissknife.opentelemetry.test.container.utils)

    api(projects.modulesDepositEventEndpointModuleTestSpecification)
    api(projects.modulesPaymentCommandEndpointModuleTestSpecification)

    implementation(projects.sharedSerializationAvro)
    implementation(libs.swissknife.pulsar.json.serialization)
}