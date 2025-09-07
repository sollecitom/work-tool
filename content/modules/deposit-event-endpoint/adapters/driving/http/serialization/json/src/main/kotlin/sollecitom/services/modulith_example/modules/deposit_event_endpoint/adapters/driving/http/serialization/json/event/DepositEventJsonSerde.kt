package sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.serialization.json.event

import org.json.JSONObject
import sollecitom.libs.pillar.json.serialization.ddd.event.jsonSerde
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.serialization.json.jsonSerde
import sollecitom.services.modulith_example.shared.account.domain.model.event.Deposit
import sollecitom.services.modulith_example.shared.account.domain.model.event.DepositEvent

private object DepositEventJsonSerde : JsonSerde.SchemaAware<DepositEvent> {

    private const val SCHEMA_LOCATION = "modulith-example/deposit-event-endpoint/event/DepositEvent.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: DepositEvent) = JSONObject().apply {
        setValue(Fields.DATA, value.data, Deposit.jsonSerde)
        setValue(Fields.METADATA, value.metadata, Event.Metadata.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {
        val data = getValue(Fields.DATA, Deposit.jsonSerde)
        val metadata = getValue(Fields.METADATA, Event.Metadata.jsonSerde)
        Event.Composite(data = data, metadata)
    }

    private object Fields {
        const val DATA = "data"
        const val METADATA = "metadata"
    }
}

val depositEventJsonSerde: JsonSerde.SchemaAware<DepositEvent> get() = DepositEventJsonSerde