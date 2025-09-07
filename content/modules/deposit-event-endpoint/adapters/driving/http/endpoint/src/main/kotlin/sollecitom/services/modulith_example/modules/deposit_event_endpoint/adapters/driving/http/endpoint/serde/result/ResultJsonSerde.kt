import org.json.JSONObject
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.result.ApplicationResult

private object ResultJsonSerde : JsonSerde.SchemaAware<ApplicationResult> {

    private const val SCHEMA_LOCATION = "modulith-example/deposit-event-endpoint/result/Result.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ApplicationResult): JSONObject = when (value) {

        is ApplicationResult.Successful -> JSONObject().put(Fields.TYPE, Values.SUCCESSFUL).put(Fields.IS_ERROR, false)
        ApplicationResult.Error -> JSONObject().put(Fields.TYPE, Values.ERROR).put(Fields.IS_ERROR, true)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        when (val type = value.getRequiredString(Fields.TYPE)) {
            Values.SUCCESSFUL -> ApplicationResult.Successful
            Values.ERROR -> ApplicationResult.Error
            else -> error("Unknown application result type '$type'")
        }
    }

    private object Fields {
        const val TYPE = "type"
        const val IS_ERROR = "is-error"
    }

    private object Values {
        const val SUCCESSFUL = "successful"
        const val ERROR = "error"
    }
}

val ApplicationResult.Companion.jsonSerde: JsonSerde.SchemaAware<ApplicationResult> get() = ResultJsonSerde