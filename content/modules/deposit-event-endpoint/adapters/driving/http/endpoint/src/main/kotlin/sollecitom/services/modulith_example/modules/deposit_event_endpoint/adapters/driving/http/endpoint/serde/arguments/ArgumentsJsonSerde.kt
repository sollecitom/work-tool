package sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.endpoint.serde.arguments

import org.json.JSONObject
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.serialization.json.event.depositEventJsonSerde
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.publish_deposit_event.PublishDepositEvent

private object ArgumentsJsonSerde : JsonSerde.SchemaAware<PublishDepositEvent.Arguments> {

    private const val SCHEMA_LOCATION = "modulith-example/deposit-event-endpoint/arguments/Arguments.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: PublishDepositEvent.Arguments) = JSONObject().apply {

        setValue(Fields.EVENT, value.event, depositEventJsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val event = getValue(Fields.EVENT, depositEventJsonSerde)
        PublishDepositEvent.Arguments(event = event)
    }

    private object Fields {
        const val EVENT = "event"
    }
}

val PublishDepositEvent.Arguments.Companion.jsonSerde: JsonSerde.SchemaAware<PublishDepositEvent.Arguments> get() = ArgumentsJsonSerde