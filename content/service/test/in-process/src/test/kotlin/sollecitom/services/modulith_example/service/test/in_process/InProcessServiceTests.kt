package sollecitom.services.modulith_example.service.test.in_process

import org.http4k.client.ApacheAsyncClient
import org.http4k.config.Environment
import org.http4k.config.EnvironmentKey
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.acme.business.domain.Acme
import sollecitom.libs.pillar.acme.conventions.CompanyConventions
import sollecitom.libs.swissknife.configuration.utils.from
import sollecitom.libs.swissknife.configuration.utils.instanceGroupName
import sollecitom.libs.swissknife.configuration.utils.instanceNodeName
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.domain.lifecycle.startBlocking
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.lens.core.extensions.networking.healthPort
import sollecitom.libs.swissknife.lens.core.extensions.networking.servicePort
import sollecitom.libs.swissknife.logger.core.LoggingLevel
import sollecitom.libs.swissknife.logging.standard.configuration.configureLogging
import sollecitom.libs.swissknife.pulsar.test.utils.newPulsarContainer
import sollecitom.services.modulith_example.configuration.ServiceProperties
import sollecitom.services.modulith_example.service.starter.Service
import sollecitom.services.modulith_example.service.test.specification.ServiceTestSpecification

@TestInstance(PER_CLASS)
class InProcessServiceTests : ServiceTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider, CompanyConventions by Acme {

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    //    override val openTelemetryCollector = GrafanaOpenTelemetryStack.newContainer()
    override val pulsar = newPulsarContainer()

    //    override val postgres = PostgresDockerContainer()
    private val httpServerProperties = mapOf(EnvironmentKey.servicePort to "0", EnvironmentKey.healthPort to "0")

    //    override val postgresConnectionOptions by lazy { postgres.connectionOptions() }
//    private val openTelemetryProperties by lazy {
//        mapOf(
//            ServiceProperties.openTelemetryEndpointUrl to openTelemetryCollector.otlpGrpcUrl
//        )
//    }
//    private val postgresProperties by lazy {
//        mapOf(
//            ServiceProperties.postgresConnectionURI to postgresConnectionOptions.schemeLessURI.toString(),
//            ServiceProperties.postgresUsername to postgresConnectionOptions.user.value,
//            ServiceProperties.postgresPassword to postgresConnectionOptions.password.value
//        )
//    }
    private val pulsarProperties by lazy {
        mapOf(
            ServiceProperties.pulsarBrokerUrl to pulsar.pulsarBrokerUrl
        )
    }
    private val serviceInstanceProperties = mapOf(
        EnvironmentKey.instanceNodeName to newId().stringValue,
        EnvironmentKey.instanceGroupName to "modulith-example-service"
    )
    private val environment by lazy { Environment.from(httpServerProperties + pulsarProperties + serviceInstanceProperties) }
    override val service by lazy { Service(environment) }
    override val httpClient = ApacheAsyncClient()

    @BeforeAll
    fun beforeAll() {
        specificationBeforeAll()
        service.startBlocking()
    }

    @AfterAll
    fun afterAll() = specificationAfterAll()
}