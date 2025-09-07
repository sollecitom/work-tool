package sollecitom.services.modulith_example.shared.account.serialization.avro.event

import sollecitom.libs.pillar.avro.serialization.ddd.event.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.services.modulith_example.shared.account.domain.model.event.AccountEventData
import sollecitom.services.modulith_example.shared.account.serialization.avro.AccountAvroSchemas
import sollecitom.services.modulith_example.shared.serialization.avro.ModulithExampleEventAvroSerdes

private val accountEventAvroSerde by lazy { Event.Composite.avroSerde(AccountAvroSchemas.Event.event, AccountEventData.avroSerde) }

val ModulithExampleEventAvroSerdes.accountEvent: AvroSerde<Event.Composite<AccountEventData>> get() = accountEventAvroSerde