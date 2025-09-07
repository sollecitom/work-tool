package sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.endpoint.serde.result

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.json.JSONObject
import sollecitom.libs.swissknife.json.utils.getRequiredBoolean
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult.Failed.CannotTargetSourceAccount
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.asResult
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult.ProcessedSuccessfully
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult.Rejected.InsufficientBalanceOnSourceAccount
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult.Rejected.NonexistentAccount
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference
import sollecitom.services.modulith_example.shared.serialization.json.account.domain.model.jsonSerde

private object ResultJsonSerde : JsonSerde.SchemaAware<ApplicationResult> {

    private const val SCHEMA_LOCATION = "modulith-example/payment-command-endpoint/result/Result.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun serialize(value: ApplicationResult): JSONObject {

        val json = JSONObject()
        when (value) {
            is ApplicationResult.Successful -> {
                json.put(Fields.IS_ERROR, false)
                when (value.processingResult.getCompleted()) {
                    ProcessedSuccessfully -> json.put(Fields.TYPE, Types.PROCESSED_SUCCESSFULLY).put(Fields.WAS_PROCESSED, true)
                    InsufficientBalanceOnSourceAccount -> json.put(Fields.TYPE, Types.INSUFFICIENT_BALANCE_ON_SOURCE_ACCOUNT).put(Fields.WAS_PROCESSED, false)
                    is NonexistentAccount -> json.put(Fields.TYPE, Types.NONEXISTENT_ACCOUNT).put(Fields.WAS_PROCESSED, false).setValue(Fields.ACCOUNT, (value.processingResult.getCompleted() as NonexistentAccount).account, AccountReference.jsonSerde)
                }
            }

            is ApplicationResult.Failed -> {
                json.put(Fields.IS_ERROR, true)
                when (value) {
                    is CannotTargetSourceAccount -> json.put(Fields.TYPE, Types.CANNOT_TARGET_SOURCE_ACCOUNT_ERROR)
                }
            }
        }
        return json
    }

    override fun deserialize(value: JSONObject): ApplicationResult =

        when (value.getRequiredBoolean(Fields.IS_ERROR)) {
            true -> when (val type = value.getRequiredString(Fields.TYPE)) {
                Types.CANNOT_TARGET_SOURCE_ACCOUNT_ERROR -> CannotTargetSourceAccount
                else -> error("Invalid error application result type $type")
            }

            false -> when (val type = value.getRequiredString(Fields.TYPE)) {
                Types.PROCESSED_SUCCESSFULLY -> ProcessedSuccessfully.asResult()
                Types.INSUFFICIENT_BALANCE_ON_SOURCE_ACCOUNT -> InsufficientBalanceOnSourceAccount.asResult()
                Types.NONEXISTENT_ACCOUNT -> NonexistentAccount(value.getValue(Fields.ACCOUNT, AccountReference.jsonSerde)).asResult()
                else -> error("Invalid successful application result type $type")
            }
        }
}

private object Fields {
    const val TYPE = "type"
    const val IS_ERROR = "is-error"
    const val WAS_PROCESSED = "was-processed"
    const val ACCOUNT = "account"
}

private object Types {
    const val PROCESSED_SUCCESSFULLY = "processed-successfully"
    const val INSUFFICIENT_BALANCE_ON_SOURCE_ACCOUNT = "insufficient-balance-on-source-account"
    const val NONEXISTENT_ACCOUNT = "nonexistent-account"
    const val CANNOT_TARGET_SOURCE_ACCOUNT_ERROR = "cannot-target-source-account"
}

val ApplicationResult.Companion.jsonSerde: JsonSerde.SchemaAware<ApplicationResult> get() = ResultJsonSerde