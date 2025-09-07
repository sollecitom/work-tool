package sollecitom.services.modulith_example.shared.account.serialization.avro.event

import org.apache.avro.generic.GenericRecord
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand
import sollecitom.services.modulith_example.shared.account.serialization.avro.AccountAvroSchemas

val SendPaymentCommand.InsufficientBalanceError.Companion.avroSchema get() = AccountAvroSchemas.Event.insufficientBalanceErrorForSendPaymentCommandReceived
val SendPaymentCommand.InsufficientBalanceError.Companion.avroSerde: AvroSerde<SendPaymentCommand.InsufficientBalanceError> get() = InsufficientBalanceErrorForSendPaymentCommandReceivedAvroSerde

private object InsufficientBalanceErrorForSendPaymentCommandReceivedAvroSerde : AvroSerde<SendPaymentCommand.InsufficientBalanceError> {

    override val schema get() = SendPaymentCommand.InsufficientBalanceError.avroSchema

    override fun serialize(value: SendPaymentCommand.InsufficientBalanceError): GenericRecord = buildRecord {

        setValue(Fields.ATTEMPT, value.attempt, SendPaymentCommand.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val attempt = getValue(Fields.ATTEMPT, SendPaymentCommand.avroSerde)
        SendPaymentCommand.InsufficientBalanceError(attempt = attempt)
    }

    private object Fields {
        const val ATTEMPT = "attempt"
    }
}