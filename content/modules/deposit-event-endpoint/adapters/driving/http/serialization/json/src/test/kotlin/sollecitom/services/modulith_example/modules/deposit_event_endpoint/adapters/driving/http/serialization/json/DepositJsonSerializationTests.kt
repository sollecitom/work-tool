package sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.serialization.json

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.services.modulith_example.shared.account.domain.model.event.Deposit
import sollecitom.services.modulith_example.shared.account.domain.test.utils.create

@TestInstance(PER_CLASS)
class DepositJsonSerializationTests : AcmeJsonSerdeTestSpecification<Deposit>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Deposit.jsonSerde

    override fun parameterizedArguments() = listOf(
        "random" to Deposit.create()
    )
}