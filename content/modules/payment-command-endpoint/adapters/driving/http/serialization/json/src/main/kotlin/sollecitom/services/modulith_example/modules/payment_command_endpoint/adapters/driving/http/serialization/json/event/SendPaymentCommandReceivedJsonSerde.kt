package sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.serialization.json.event

import org.json.JSONObject
import sollecitom.libs.pillar.json.serialization.ddd.event.jsonSerde
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.serialization.json.jsonSerde
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommandReceived

private object SendPaymentCommandReceivedJsonSerde : JsonSerde.SchemaAware<SendPaymentCommandReceived> {

    private const val SCHEMA_LOCATION = "modulith-example/payment-command-endpoint/event/SendPaymentCommandReceived.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: SendPaymentCommandReceived) = JSONObject().apply {
        setValue(Fields.DATA, value.data, SendPaymentCommand.jsonSerde)
        setValue(Fields.METADATA, value.metadata, Event.Metadata.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {
        val data = getValue(Fields.DATA, SendPaymentCommand.jsonSerde)
        val metadata = getValue(Fields.METADATA, Event.Metadata.jsonSerde)
        Event.Composite(data = data, metadata)
    }

    private object Fields {
        const val DATA = "data"
        const val METADATA = "metadata"
    }
}

val sendPaymentCommandReceivedJsonSerde: JsonSerde.SchemaAware<SendPaymentCommandReceived> get() = SendPaymentCommandReceivedJsonSerde