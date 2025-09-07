package sollecitom.services.modulith_example.shared.account.messaing.converter

import sollecitom.libs.pillar.messaging.avro.TopicAndSerde
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.services.modulith_example.shared.account.domain.model.event.AccountEventData
import sollecitom.services.modulith_example.shared.account.serialization.avro.event.accountEvent
import sollecitom.services.modulith_example.shared.messaging.topics.ModulithExampleEventTopics
import sollecitom.services.modulith_example.shared.serialization.avro.ModulithExampleEventAvroSerdes

private val accountEventTopic = "persistent://acme/banking/account-events".let(Topic::parse)
private val accountEventTopicAndSerde by lazy { TopicAndSerde(accountEventTopic, ModulithExampleEventAvroSerdes.accountEvent, AccountEventData.messageConverter) }

val ModulithExampleEventTopics.accountEvents: TopicAndSerde<Event.Composite<AccountEventData>> get() = accountEventTopicAndSerde