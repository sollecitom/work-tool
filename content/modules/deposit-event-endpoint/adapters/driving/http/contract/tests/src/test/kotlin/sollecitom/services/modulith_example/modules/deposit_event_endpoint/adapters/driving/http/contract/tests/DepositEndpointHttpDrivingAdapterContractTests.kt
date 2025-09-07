package sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.contract.tests

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.http.api.conventions.companyWide
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.logger.core.LoggingLevel
import sollecitom.libs.swissknife.logging.standard.configuration.configureLogging
import sollecitom.libs.swissknife.openapi.provider.provider.OpenApiProvider
import sollecitom.libs.swissknife.openapi.provider.provider.standard
import sollecitom.libs.swissknife.openapi.validation.http4k.test.utils.WithHttp4kOpenApiValidationSupport
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.Http4kOpenApiValidator
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.implementation.invoke
import sollecitom.libs.swissknife.web.api.test.utils.LocalHttpDrivingAdapterTestSpecification
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition

@TestInstance(PER_CLASS)
class DepositEndpointHttpDrivingAdapterContractTests : WithHttp4kOpenApiValidationSupport, HttpApiDefinition by HttpApiDefinition.companyWide, CoreDataGenerator by CoreDataGenerator.testProvider, LocalHttpDrivingAdapterTestSpecification, OpenApiProvider by OpenApiProvider.standard {

    override val openApiValidator = Http4kOpenApiValidator(openApi)

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    @Nested
    inner class DepositEventEndpoint : DepositEventEndpointHttpTestSpecification, WithHttp4kOpenApiValidationSupport by this, HttpApiDefinition by this, CoreDataGenerator by this, LocalHttpDrivingAdapterTestSpecification by this
}