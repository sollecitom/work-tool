package sollecitom.services.modulith_example.shared.account.serialization.avro.event

import org.apache.avro.generic.GenericRecord
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand
import sollecitom.services.modulith_example.shared.account.serialization.avro.AccountAvroSchemas

val SendPaymentCommand.AccountNotFoundError.Companion.avroSchema get() = AccountAvroSchemas.Event.accountNotFoundErrorForSendPaymentCommandReceived
val SendPaymentCommand.AccountNotFoundError.Companion.avroSerde: AvroSerde<SendPaymentCommand.AccountNotFoundError> get() = AccountNotFoundErrorForSendPaymentCommandReceivedAvroSerde

private object AccountNotFoundErrorForSendPaymentCommandReceivedAvroSerde : AvroSerde<SendPaymentCommand.AccountNotFoundError> {

    override val schema get() = SendPaymentCommand.AccountNotFoundError.avroSchema

    override fun serialize(value: SendPaymentCommand.AccountNotFoundError): GenericRecord = buildRecord {

        setValue(Fields.ATTEMPT, value.attempt, SendPaymentCommand.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val attempt = getValue(Fields.ATTEMPT, SendPaymentCommand.avroSerde)
        SendPaymentCommand.AccountNotFoundError(attempt = attempt)
    }

    private object Fields {
        const val ATTEMPT = "attempt"
    }
}