package sollecitom.services.modulith_example.shared.account.serialization.avro.event

import org.apache.avro.generic.GenericRecord
import sollecitom.libs.pillar.avro.serialization.core.currency.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.services.modulith_example.shared.account.domain.model.event.Deposit
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference
import sollecitom.services.modulith_example.shared.account.serialization.avro.AccountAvroSchemas
import sollecitom.services.modulith_example.shared.account.serialization.avro.reference.avroSerde

val Deposit.Companion.avroSchema get() = AccountAvroSchemas.Event.deposit
val Deposit.Companion.avroSerde: AvroSerde<Deposit> get() = DepositAvroSerde

private object DepositAvroSerde : AvroSerde<Deposit> {

    override val schema get() = Deposit.avroSchema

    override fun serialize(value: Deposit): GenericRecord = buildRecord {

        setValue(Fields.TARGET_ACCOUNT, value.targetAccount, AccountReference.avroSerde)
        setValue(Fields.AMOUNT, value.amount, CurrencyAmount.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val targetAccount = getValue(Fields.TARGET_ACCOUNT, AccountReference.avroSerde)
        val amount = getValue(Fields.AMOUNT, CurrencyAmount.avroSerde)
        Deposit(targetAccount = targetAccount, amount = amount)
    }

    private object Fields {
        const val TARGET_ACCOUNT = "target_account"
        const val AMOUNT = "amount"
    }
}