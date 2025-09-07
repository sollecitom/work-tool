package sollecitom.services.modulith_example.shared.account.serialization.avro.reference

import org.apache.avro.generic.GenericRecord
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeWith
import sollecitom.libs.swissknife.avro.serialization.utils.getRecordFromUnion
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference
import sollecitom.services.modulith_example.shared.account.domain.model.reference.InternalAccountNumber
import sollecitom.services.modulith_example.shared.account.serialization.avro.AccountAvroSchemas

val AccountReference.Companion.avroSchema get() = AccountAvroSchemas.accountReference
val AccountReference.Companion.avroSerde: AvroSerde<AccountReference> get() = AccountReferenceAvroSerde

private object AccountReferenceAvroSerde : AvroSerde<AccountReference> {

    override val schema get() = AccountReference.avroSchema

    override fun serialize(value: AccountReference): GenericRecord = buildRecord {
        val record = when (value) {
            is InternalAccountNumber -> InternalAccountNumber.avroSerde.serialize(value)
        }
        setRecordInUnion(value.type(), record)
    }

    override fun deserialize(value: GenericRecord) = value.getRecordFromUnion { unionTypeName, unionRecord ->
        when (unionTypeName) {
            Types.INTERNAL_ACCOUNT_REFERENCE -> unionRecord.deserializeWith(InternalAccountNumber.avroSerde)
            else -> error("Unknown account reference type $unionTypeName")
        }
    }

    private fun AccountReference.type(): String = when (this) {
        is InternalAccountNumber -> Types.INTERNAL_ACCOUNT_REFERENCE
    }

    private object Types {
        const val INTERNAL_ACCOUNT_REFERENCE = "internal_account_reference"
    }
}
