dependencies {
    testImplementation(projects.serviceStarter)
    testImplementation(projects.configuration)
    testImplementation(projects.serviceTestSpecification)
    testImplementation(libs.swissknife.core.test.utils)
    testImplementation(libs.pillar.acme.business.domain)
    testImplementation(libs.pillar.messaging.conventions)
}