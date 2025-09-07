package sollecitom.services.modulith_example.shared.serialization.json.account.domain.model

import org.json.JSONObject
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.services.modulith_example.shared.account.domain.model.reference.InternalAccountNumber

internal object InternalAccountNumberJsonSerde : JsonSerde.SchemaAware<InternalAccountNumber> {

    const val TYPE_VALUE = "internal-account-number"
    private const val SCHEMA_LOCATION = "modulith-example/shared/account/domain/model/InternalAccountNumber.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: InternalAccountNumber) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        put(Fields.VALUE, value.value)
    }

    override fun deserialize(value: JSONObject) = with(value) {
        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Field 'type' must be '$TYPE_VALUE' but was '$type'" }
        val value = getRequiredString(Fields.VALUE)
        InternalAccountNumber(value = value)
    }

    private object Fields {
        const val TYPE = "type"
        const val VALUE = "value"
    }
}