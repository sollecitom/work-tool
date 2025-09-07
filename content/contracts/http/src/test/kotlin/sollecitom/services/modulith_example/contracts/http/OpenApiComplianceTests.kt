package sollecitom.services.modulith_example.contracts.http

import assertk.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.openapi.rules.AcmeOpenApiRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isCompliantWith
import sollecitom.libs.swissknife.logging.standard.configuration.configureLogging
import sollecitom.libs.swissknife.openapi.provider.provider.OpenApiProvider
import sollecitom.libs.swissknife.openapi.provider.provider.standard

@TestInstance(PER_CLASS)
class OpenApiComplianceTests : OpenApiProvider by OpenApiProvider.standard {

    init {
        configureLogging()
    }

    @Test
    fun `the declared OpenAPI specification complies with the standard OpenAPI guidelines`() {

        assertThat(openApi).isCompliantWith(AcmeOpenApiRules)
    }
}