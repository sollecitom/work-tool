package sollecitom.services.modulith_example.shared.account.messaging.adapters

import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.PulsarClient
import sollecitom.libs.swissknife.core.domain.identity.InstanceInfo
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.messaging.domain.message.connector.MessageConnector
import sollecitom.libs.swissknife.messaging.domain.message.publisher.MessagePublisher
import sollecitom.libs.swissknife.messaging.domain.message.source.MessageSource
import sollecitom.libs.swissknife.pulsar.messaging.adapter.configuration.defaultConsumerCustomization
import sollecitom.libs.swissknife.pulsar.messaging.adapter.configuration.defaultProducerCustomization
import sollecitom.services.modulith_example.shared.account.domain.model.event.AccountEvent

private class PulsarAccountEventConnector(
    private val source: MessageSource<AccountEvent>,
    private val publisher: MessagePublisher<AccountEvent>
) : MessageConnector<AccountEvent>, MessageSource<AccountEvent> by source, MessagePublisher<AccountEvent> by publisher {

    constructor(
        pulsar: PulsarClient,
        instanceInfo: InstanceInfo,
        customizeConsumer: ConsumerBuilder<AccountEvent>.() -> ConsumerBuilder<AccountEvent>,
        customizeProducer: ProducerBuilder<AccountEvent>.() -> ProducerBuilder<AccountEvent>,
        coreDataGenerator: CoreDataGenerator
    ) : this(
        source = MessageSource.accountEvents(pulsar, instanceInfo, customizeConsumer),
        publisher = with(coreDataGenerator) { MessagePublisher.Companion.accountEvent(pulsar, instanceInfo, customizeProducer) }
    )

    override suspend fun start() {
        publisher.start()
        source.start()
    }

    override suspend fun stop() {
        source.stop()
        publisher.stop()
    }
}

context(generator: CoreDataGenerator)
fun MessageConnector.Companion.accountEvents(
    pulsar: PulsarClient,
    instanceInfo: InstanceInfo,
    customizeConsumer: ConsumerBuilder<AccountEvent>.() -> ConsumerBuilder<AccountEvent> = { defaultConsumerCustomization() },
    customizeProducer: ProducerBuilder<AccountEvent>.() -> ProducerBuilder<AccountEvent> = { defaultProducerCustomization() }
): MessageConnector<AccountEvent> = PulsarAccountEventConnector(pulsar, instanceInfo, customizeConsumer, customizeProducer, generator)