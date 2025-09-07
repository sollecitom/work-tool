package sollecitom.services.modulith_example.modules.deposit_event_endpoint.module.test.specification

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import assertk.assertions.startsWith
import kotlinx.coroutines.flow.first
import org.apache.pulsar.client.api.SubscriptionInitialPosition.Earliest
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PulsarContainer
import sollecitom.libs.pillar.web.api.utils.api.withInvocationContext
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.test.utils.testWithInvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.test.utils.asEvent
import sollecitom.libs.swissknife.ddd.test.utils.create
import sollecitom.libs.swissknife.ddd.test.utils.hasInvocationContext
import sollecitom.libs.swissknife.ddd.test.utils.isOriginating
import sollecitom.libs.swissknife.http4k.utils.body
import sollecitom.libs.swissknife.http4k.utils.invoke
import sollecitom.libs.swissknife.messaging.domain.event.utils.eventType
import sollecitom.libs.swissknife.messaging.domain.event.utils.hasType
import sollecitom.libs.swissknife.messaging.domain.message.consumer.messages
import sollecitom.libs.swissknife.messaging.domain.message.correlation.utils.wasCausedBy
import sollecitom.libs.swissknife.messaging.domain.message.isOriginating
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.pulsar.messaing.test.utils.avro.newMessageConsumer
import sollecitom.libs.swissknife.pulsar.test.utils.client
import sollecitom.libs.swissknife.web.api.test.utils.WithHttpDrivingAdapterTestSpecification
import sollecitom.libs.swissknife.web.api.test.utils.httpURLWithPath
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.endpoint.serde.arguments.jsonSerde
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.publish_deposit_event.PublishDepositEvent
import sollecitom.services.modulith_example.shared.account.domain.model.event.Deposit
import sollecitom.services.modulith_example.shared.account.domain.test.utils.create
import sollecitom.services.modulith_example.shared.account.serialization.avro.event.accountEvent
import sollecitom.services.modulith_example.shared.serialization.avro.ModulithExampleEventAvroSerdes

interface DepositEventEndpointModuleTestSpecification : WithHttpDrivingAdapterTestSpecification, CoreDataGenerator, HttpApiDefinition, MessagePropertyNames {

    val accountEventsTopic: Topic
    val pulsar: PulsarContainer

    @Test
    fun `receiving and publishing a deposit event`() = testWithInvocationContext {

        val consumer = pulsar.client().newMessageConsumer(serde = ModulithExampleEventAvroSerdes.accountEvent, topic = accountEventsTopic) { subscriptionInitialPosition(Earliest) }
        val event = Deposit.create().asEvent(Event.Metadata.create(context = Event.Context.create()))
        val payload = PublishDepositEvent.Arguments.jsonSerde.serialize(PublishDepositEvent.Arguments(event))
        val request = Request(POST, service.httpURLWithPath("events/deposit/v1")).body(payload).withInvocationContext()

        val response = httpClient(request)
        assertThat(response.status).isEqualTo(Status.OK)
        val receivedMessage = consumer.messages.first { it.hasType(Deposit.type) && it.wasCausedBy(event) }.apply { acknowledge() }

        assertThat(receivedMessage.topic).isEqualTo(accountEventsTopic)
        assertThat(receivedMessage.producerName.value).startsWith("modulith-example-service.deposit-event-endpoint.producer.")
        assertThat(receivedMessage.isOriginating).isTrue()
        assertThat(receivedMessage.value.data).isEqualTo(event.data)
        assertThat(receivedMessage.value).isOriginating()
        assertThat(receivedMessage.value).hasInvocationContext(event.context.invocation)
        assertThat(receivedMessage.eventType()).isEqualTo(Deposit.type)
    }
}