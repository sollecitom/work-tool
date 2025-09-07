package sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.serialization.json

import org.json.JSONObject
import sollecitom.libs.pillar.json.serialization.core.currency.jsonSerde
import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import sollecitom.services.modulith_example.shared.account.domain.model.event.Deposit
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference
import sollecitom.services.modulith_example.shared.serialization.json.account.domain.model.jsonSerde

private object DepositJsonSerde : JsonSerde.SchemaAware<Deposit> {

    private const val SCHEMA_LOCATION = "modulith-example/deposit-event-endpoint/Deposit.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Deposit) = JSONObject().apply {
        setValue(Fields.TARGET_ACCOUNT, value.targetAccount, AccountReference.jsonSerde)
        setValue(Fields.AMOUNT, value.amount, CurrencyAmount.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {
        val targetAccount = getValue(Fields.TARGET_ACCOUNT, AccountReference.jsonSerde)
        val amount = getValue(Fields.AMOUNT, CurrencyAmount.jsonSerde)
        Deposit(targetAccount = targetAccount, amount = amount)
    }

    private object Fields {
        const val TARGET_ACCOUNT = "target-account"
        const val AMOUNT = "amount"
    }
}

val Deposit.Companion.jsonSerde: JsonSerde.SchemaAware<Deposit> get() = DepositJsonSerde