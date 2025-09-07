package sollecitom.services.modulith_example.service.test.specification

import kotlinx.coroutines.runBlocking
import org.apache.pulsar.client.admin.PulsarAdmin
import org.testcontainers.containers.PulsarContainer
import sollecitom.libs.pillar.acme.conventions.CompanyConventions
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.pulsar.test.utils.admin
import sollecitom.libs.swissknife.pulsar.utils.ensureTopicExistsWithSchema
import sollecitom.libs.swissknife.web.api.test.utils.MonitoringEndpointsTestSpecification
import sollecitom.libs.swissknife.web.api.test.utils.PrometheusMetricsEndpointTestSpecification
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.openapi.test.utils.OpenApiHttpEndpointTestSpecification
import sollecitom.services.modulith_example.shared.account.serialization.avro.event.accountEvent
import sollecitom.services.modulith_example.shared.serialization.avro.ModulithExampleEventAvroSerdes
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface ServiceTestSpecification : CoreDataGenerator, MonitoringEndpointsTestSpecification, PrometheusMetricsEndpointTestSpecification, OpenApiHttpEndpointTestSpecification, CompanyConventions, HttpApiDefinition, ModulesTestSpecification {

    //    val openTelemetryCollector: LgtmStackContainer
    override val timeout: Duration get() = 30.seconds
    override val accountEventsTopic get() = "persistent://acme/banking/account-events".let(Topic::parse)

    fun specificationBeforeAll() = runBlocking {
//        openTelemetryCollector.start()
        pulsar.start()
        pulsar.initializeTopicsAndSchemata()
    }

    fun specificationAfterAll() = runBlocking {
        pulsar.stop()
//        openTelemetryCollector.stop()
    }

    private fun PulsarContainer.initializeTopicsAndSchemata() = admin().initializeTopicsAndSchemata()

    private fun PulsarAdmin.initializeTopicsAndSchemata() = use {
        it.ensureTopicExistsWithSchema(topic = accountEventsTopic, schema = ModulithExampleEventAvroSerdes.accountEvent.schema, partitionsCount = 10)
    }
}