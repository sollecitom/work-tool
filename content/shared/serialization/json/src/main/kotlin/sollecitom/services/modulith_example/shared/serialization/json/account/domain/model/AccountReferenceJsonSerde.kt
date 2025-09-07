package sollecitom.services.modulith_example.shared.serialization.json.account.domain.model

import org.json.JSONObject
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference
import sollecitom.services.modulith_example.shared.account.domain.model.reference.InternalAccountNumber

private object AccountReferenceJsonSerde : JsonSerde.SchemaAware<AccountReference> {

    private const val SCHEMA_LOCATION = "modulith-example/shared/account/domain/model/AccountReference.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: AccountReference) = when (value) {
        is InternalAccountNumber -> InternalAccountNumberJsonSerde.serialize(value)
    }

    override fun deserialize(value: JSONObject) = when (val type = value.getRequiredString(Fields.TYPE)) {
        InternalAccountNumberJsonSerde.TYPE_VALUE -> InternalAccountNumberJsonSerde.deserialize(value)
        else -> error("Unsupported account reference type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val AccountReference.Companion.jsonSerde: JsonSerde.SchemaAware<AccountReference> get() = AccountReferenceJsonSerde