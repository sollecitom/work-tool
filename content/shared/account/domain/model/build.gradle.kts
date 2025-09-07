dependencies {
    api(libs.swissknife.core.domain)
    api(libs.swissknife.correlation.core.domain)
    api(libs.swissknife.ddd.domain)

    implementation(libs.swissknife.kotlin.extensions)
    implementation(libs.swissknife.core.utils)

    testImplementation(libs.swissknife.test.utils)
    testImplementation(libs.swissknife.core.test.utils)
}