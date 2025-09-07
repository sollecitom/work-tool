package sollecitom.services.modulith_example.shared.serialization.json.account.domain.model

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference
import sollecitom.services.modulith_example.shared.account.domain.model.reference.InternalAccountNumber
import sollecitom.services.modulith_example.shared.account.domain.test.utils.create

@TestInstance(PER_CLASS)
class AccountReferenceJsonSerializationTests : AcmeJsonSerdeTestSpecification<AccountReference>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = AccountReference.jsonSerde

    override fun parameterizedArguments() = listOf(
        "internal-account-number" to InternalAccountNumber.create()
    )
}