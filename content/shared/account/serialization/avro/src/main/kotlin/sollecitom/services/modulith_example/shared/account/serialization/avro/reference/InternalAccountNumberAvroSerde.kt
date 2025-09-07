package sollecitom.services.modulith_example.shared.account.serialization.avro.reference

import org.apache.avro.generic.GenericRecord
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getString
import sollecitom.services.modulith_example.shared.account.domain.model.reference.InternalAccountNumber
import sollecitom.services.modulith_example.shared.account.serialization.avro.AccountAvroSchemas

val InternalAccountNumber.Companion.avroSchema get() = AccountAvroSchemas.internalAccountNumber
val InternalAccountNumber.Companion.avroSerde: AvroSerde<InternalAccountNumber> get() = InternalAccountNumberAvroSerde

private object InternalAccountNumberAvroSerde : AvroSerde<InternalAccountNumber> {

    override val schema get() = InternalAccountNumber.avroSchema

    override fun serialize(value: InternalAccountNumber): GenericRecord = buildRecord {

        set(Fields.VALUE, value.value)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val value = getString(Fields.VALUE)
        InternalAccountNumber(value = value)
    }

    private object Fields {
        const val VALUE = "value"
    }
}