dependencies {
    api(projects.sharedAccountDomainModel)
    api(libs.swissknife.core.utils)
    api(libs.swissknife.correlation.core.test.utils)
    api(libs.swissknife.ddd.test.utils)
    api(libs.swissknife.test.utils)

    implementation(libs.swissknife.logger.core)
    implementation(libs.swissknife.configuration.utils)
}