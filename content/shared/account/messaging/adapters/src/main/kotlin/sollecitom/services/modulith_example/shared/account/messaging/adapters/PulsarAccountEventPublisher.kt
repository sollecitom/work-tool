package sollecitom.services.modulith_example.shared.account.messaging.adapters

import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.PulsarClient
import sollecitom.libs.pillar.messaging.pulsar.avro.newMessageProducer
import sollecitom.libs.swissknife.core.domain.identity.InstanceInfo
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.converter.MessageConverter
import sollecitom.libs.swissknife.messaging.domain.message.converter.produce
import sollecitom.libs.swissknife.messaging.domain.message.publisher.MessagePublisher
import sollecitom.libs.swissknife.pulsar.messaging.adapter.configuration.defaultProducerCustomization
import sollecitom.services.modulith_example.shared.account.domain.model.event.AccountEvent
import sollecitom.services.modulith_example.shared.account.messaing.converter.accountEvents
import sollecitom.services.modulith_example.shared.messaging.topics.ModulithExampleEventTopics

private class PulsarAccountEventPublisher(
    pulsar: PulsarClient,
    instanceInfo: InstanceInfo,
    customizeProducer: ProducerBuilder<AccountEvent>.() -> ProducerBuilder<AccountEvent>,
    coreDataGenerator: CoreDataGenerator
) : MessagePublisher<AccountEvent>, CoreDataGenerator by coreDataGenerator, MessageConverter<AccountEvent> by ModulithExampleEventTopics.accountEvents {

    private val messageProducer = pulsar.newMessageProducer(ModulithExampleEventTopics.accountEvents, instanceInfo, customizeProducer)

    context(_: InvocationContext<*>)
    override suspend fun publish(value: AccountEvent, parentMessageId: Message.Id?, originatingMessageId: Message.Id?) {

        val messageId = messageProducer.produce(value, parentMessageId, originatingMessageId)
        logger.info { "Successfully produced compliance plan event in Avro to topic Pulsar '${messageProducer.topic.fullName.value}'. Message ID: '${messageId.stringRepresentation}', Event: {$value}" }
    }

    override suspend fun start() = messageProducer.start()

    override suspend fun stop() = messageProducer.stop()

    companion object : Loggable()
}

context(generator: CoreDataGenerator)
fun MessagePublisher.Companion.accountEvent(
    pulsar: PulsarClient,
    instanceInfo: InstanceInfo,
    customizeProducer: ProducerBuilder<AccountEvent>.() -> ProducerBuilder<AccountEvent> = { defaultProducerCustomization() }
): MessagePublisher<AccountEvent> = PulsarAccountEventPublisher(
    pulsar,
    instanceInfo,
    customizeProducer,
    generator
)