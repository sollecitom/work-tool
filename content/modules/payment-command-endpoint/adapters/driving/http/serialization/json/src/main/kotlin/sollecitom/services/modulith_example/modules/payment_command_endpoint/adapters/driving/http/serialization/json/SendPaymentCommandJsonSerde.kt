package sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.serialization.json

import org.json.JSONObject
import sollecitom.libs.pillar.json.serialization.core.currency.jsonSerde
import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference
import sollecitom.services.modulith_example.shared.serialization.json.account.domain.model.jsonSerde

private object SendPaymentCommandJsonSerde : JsonSerde.SchemaAware<SendPaymentCommand> {

    private const val SCHEMA_LOCATION = "modulith-example/payment-command-endpoint/SendPaymentCommand.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: SendPaymentCommand) = JSONObject().apply {
        setValue(Fields.SOURCE_ACCOUNT, value.sourceAccount, AccountReference.jsonSerde)
        setValue(Fields.TARGET_ACCOUNT, value.targetAccount, AccountReference.jsonSerde)
        setValue(Fields.AMOUNT, value.amount, CurrencyAmount.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {
        val sourceAccount = getValue(Fields.SOURCE_ACCOUNT, AccountReference.jsonSerde)
        val targetAccount = getValue(Fields.TARGET_ACCOUNT, AccountReference.jsonSerde)
        val amount = getValue(Fields.AMOUNT, CurrencyAmount.jsonSerde)
        SendPaymentCommand(sourceAccount = sourceAccount, targetAccount = targetAccount, amount = amount)
    }

    private object Fields {
        const val SOURCE_ACCOUNT = "source-account"
        const val TARGET_ACCOUNT = "target-account"
        const val AMOUNT = "amount"
    }
}

val SendPaymentCommand.Companion.jsonSerde: JsonSerde.SchemaAware<SendPaymentCommand> get() = SendPaymentCommandJsonSerde