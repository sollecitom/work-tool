package sollecitom.services.modulith_example.shared.account.serialization.avro.event

import org.apache.avro.generic.GenericRecord
import sollecitom.libs.pillar.avro.serialization.core.currency.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.services.modulith_example.shared.account.domain.model.event.OutboundPayment
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference
import sollecitom.services.modulith_example.shared.account.serialization.avro.AccountAvroSchemas
import sollecitom.services.modulith_example.shared.account.serialization.avro.reference.avroSerde

val OutboundPayment.Companion.avroSchema get() = AccountAvroSchemas.Event.outboundPayment
val OutboundPayment.Companion.avroSerde: AvroSerde<OutboundPayment> get() = OutboundPaymentAvroSerde

private object OutboundPaymentAvroSerde : AvroSerde<OutboundPayment> {

    override val schema get() = OutboundPayment.avroSchema

    override fun serialize(value: OutboundPayment): GenericRecord = buildRecord {

        setValue(Fields.SOURCE_ACCOUNT, value.sourceAccount, AccountReference.avroSerde)
        setValue(Fields.TARGET_ACCOUNT, value.targetAccount, AccountReference.avroSerde)
        setValue(Fields.AMOUNT, value.amount, CurrencyAmount.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val sourceAccount = getValue(Fields.SOURCE_ACCOUNT, AccountReference.avroSerde)
        val targetAccount = getValue(Fields.TARGET_ACCOUNT, AccountReference.avroSerde)
        val amount = getValue(Fields.AMOUNT, CurrencyAmount.avroSerde)
        OutboundPayment(sourceAccount = sourceAccount, targetAccount = targetAccount, amount = amount)
    }

    private object Fields {
        const val SOURCE_ACCOUNT = "source_account"
        const val TARGET_ACCOUNT = "target_account"
        const val AMOUNT = "amount"
    }
}