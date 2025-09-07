package sollecitom.services.modulith_example.shared.account.serialization.avro.reference

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference
import sollecitom.services.modulith_example.shared.account.domain.model.reference.InternalAccountNumber
import sollecitom.services.modulith_example.shared.account.domain.test.utils.create

@TestInstance(PER_CLASS)
private class AccountReferenceAvroSerdeTests : AcmeAvroSerdeTestSpecification<AccountReference>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = AccountReference.avroSerde

    override fun parameterizedArguments() = listOf(
        "internal-account-number" to InternalAccountNumber.create()
    )
}