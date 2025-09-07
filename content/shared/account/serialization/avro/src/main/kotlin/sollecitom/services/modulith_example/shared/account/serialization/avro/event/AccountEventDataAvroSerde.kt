package sollecitom.services.modulith_example.shared.account.serialization.avro.event

import org.apache.avro.generic.GenericRecord
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeWith
import sollecitom.libs.swissknife.avro.serialization.utils.getRecordFromUnion
import sollecitom.services.modulith_example.shared.account.domain.model.event.*
import sollecitom.services.modulith_example.shared.account.serialization.avro.AccountAvroSchemas

val AccountEventData.Companion.avroSchema get() = AccountAvroSchemas.Event.eventData
val AccountEventData.Companion.avroSerde: AvroSerde<AccountEventData> get() = AccountEventDataAvroSerde

private object AccountEventDataAvroSerde : AvroSerde<AccountEventData> {

    override val schema get() = AccountEventData.avroSchema

    override fun serialize(value: AccountEventData): GenericRecord = buildRecord {
        val record = when (value) {
            is SendPaymentCommand -> SendPaymentCommand.avroSerde.serialize(value)
            is Deposit -> Deposit.avroSerde.serialize(value)
            is OutboundPayment -> OutboundPayment.avroSerde.serialize(value)
            is InboundPayment -> InboundPayment.avroSerde.serialize(value)
            is SendPaymentCommand.InsufficientBalanceError -> SendPaymentCommand.InsufficientBalanceError.avroSerde.serialize(value)
            is SendPaymentCommand.AccountNotFoundError -> SendPaymentCommand.AccountNotFoundError.avroSerde.serialize(value)
        }
        setRecordInUnion(value.type(), record)
    }

    override fun deserialize(value: GenericRecord) = value.getRecordFromUnion { unionTypeName, unionRecord ->
        when (unionTypeName) {
            Types.DEPOSIT -> unionRecord.deserializeWith(Deposit.avroSerde)
            Types.INBOUND_PAYMENT -> unionRecord.deserializeWith(InboundPayment.avroSerde)
            Types.OUTBOUND_PAYMENT -> unionRecord.deserializeWith(OutboundPayment.avroSerde)
            Types.SEND_PAYMENT_COMMAND_RECEIVED -> unionRecord.deserializeWith(SendPaymentCommand.avroSerde)
            Types.ACCOUNT_NOT_FOUND_ERROR_FOR_SEND_PAYMENT_COMMAND_RECEIVED -> unionRecord.deserializeWith(SendPaymentCommand.AccountNotFoundError.avroSerde)
            Types.INSUFFICIENT_BALANCE_ERROR_FOR_SEND_PAYMENT_COMMAND_RECEIVED -> unionRecord.deserializeWith(SendPaymentCommand.InsufficientBalanceError.avroSerde)
            else -> error("Unknown account event data type $unionTypeName")
        }
    }

    private fun AccountEventData.type(): String = when (this) {
        is Deposit -> Types.DEPOSIT
        is InboundPayment -> Types.INBOUND_PAYMENT
        is OutboundPayment -> Types.OUTBOUND_PAYMENT
        is SendPaymentCommand -> Types.SEND_PAYMENT_COMMAND_RECEIVED
        is SendPaymentCommand.AccountNotFoundError -> Types.ACCOUNT_NOT_FOUND_ERROR_FOR_SEND_PAYMENT_COMMAND_RECEIVED
        is SendPaymentCommand.InsufficientBalanceError -> Types.INSUFFICIENT_BALANCE_ERROR_FOR_SEND_PAYMENT_COMMAND_RECEIVED
    }

    private object Types {
        const val SEND_PAYMENT_COMMAND_RECEIVED = "send-payment-command-received"
        const val INSUFFICIENT_BALANCE_ERROR_FOR_SEND_PAYMENT_COMMAND_RECEIVED = "insufficient-balance-error-for-send-payment-command-received"
        const val ACCOUNT_NOT_FOUND_ERROR_FOR_SEND_PAYMENT_COMMAND_RECEIVED = "account-not-found-error-for-send-payment-command-received"
        const val DEPOSIT = "deposit"
        const val OUTBOUND_PAYMENT = "outbound-payment"
        const val INBOUND_PAYMENT = "inbound-payment"
    }
}
