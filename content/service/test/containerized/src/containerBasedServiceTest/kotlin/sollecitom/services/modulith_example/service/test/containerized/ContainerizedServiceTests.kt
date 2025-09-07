package sollecitom.services.modulith_example.service.test.containerized

import org.http4k.client.ApacheAsyncClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.testcontainers.containers.Network
import org.testcontainers.containers.PulsarContainer
import sollecitom.libs.pillar.acme.business.domain.Example
import sollecitom.libs.pillar.acme.conventions.CompanyConventions
import sollecitom.libs.pillar.web.api.test.utils.local.pod.testContainers
import sollecitom.libs.swissknife.core.domain.lifecycle.startBlocking
import sollecitom.libs.swissknife.core.domain.lifecycle.stopBlocking
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.logger.core.LoggingLevel
import sollecitom.libs.swissknife.logging.standard.configuration.configureLogging
import sollecitom.libs.swissknife.pulsar.test.utils.newPulsarContainer
import sollecitom.libs.swissknife.pulsar.test.utils.withNetworkAndAliases
import sollecitom.libs.swissknife.web.api.test.utils.local.pod.LocalPodWithWebDrivingAdapter
import sollecitom.services.modulith_example.service.test.specification.ServiceTestSpecification
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
class ContainerizedServiceTests : ServiceTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider, CompanyConventions by Example {

    private val network = Network.newNetwork()
//    override val openTelemetryCollector = GrafanaOpenTelemetryStack.newContainer().withNetworkAndAliases(network)
//    override val postgres: PostgresDockerContainer = PostgresDockerContainer().withNetworkAndAliases(network)
    override val pulsar: PulsarContainer = newPulsarContainer().withNetworkAndAliases(network)
    override val service by lazy { LocalPodWithWebDrivingAdapter.testContainers(size = 2) { newModulithExampleServiceContainer(pulsar).withNetwork(network) } }
    override val httpClient = ApacheAsyncClient()
    override val timeout = 30.seconds

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    @BeforeAll
    fun beforeAll() {
        specificationBeforeAll()
        try {
            service.startBlocking()
        } finally {
            println(service.logs.pretty())
        }
    }

    @AfterAll
    fun afterAll() {
        service.stopBlocking()
        specificationAfterAll()
        network.close()
    }
}