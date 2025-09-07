package sollecitom.services.modulith_example.shared.account.messaging.adapters

import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.PulsarClient
import sollecitom.libs.pillar.messaging.pulsar.avro.newMessageConsumer
import sollecitom.libs.swissknife.core.domain.identity.InstanceInfo
import sollecitom.libs.swissknife.messaging.domain.message.consumer.messages
import sollecitom.libs.swissknife.messaging.domain.message.source.MessageSource
import sollecitom.libs.swissknife.pulsar.messaging.adapter.configuration.defaultConsumerCustomization
import sollecitom.services.modulith_example.shared.account.domain.model.event.AccountEvent
import sollecitom.services.modulith_example.shared.account.messaing.converter.accountEvents
import sollecitom.services.modulith_example.shared.messaging.topics.ModulithExampleEventTopics

private class PulsarAccountEventMessageSource(pulsar: PulsarClient, instanceInfo: InstanceInfo, customizeConsumer: ConsumerBuilder<AccountEvent>.() -> ConsumerBuilder<AccountEvent>) : MessageSource<AccountEvent> {

    private val messageConsumer = pulsar.newMessageConsumer(topicAndSerde = ModulithExampleEventTopics.accountEvents, instanceInfo = instanceInfo, customize = customizeConsumer)

    override val messages get() = messageConsumer.messages

    override suspend fun start() = messageConsumer.start()

    override suspend fun stop() = messageConsumer.stop()
}

fun MessageSource.Companion.accountEvents(pulsar: PulsarClient, instanceInfo: InstanceInfo, customizeConsumer: ConsumerBuilder<AccountEvent>.() -> ConsumerBuilder<AccountEvent> = { defaultConsumerCustomization() }): MessageSource<AccountEvent> = PulsarAccountEventMessageSource(pulsar, instanceInfo, customizeConsumer)