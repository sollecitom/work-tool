package sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.endpoint.serde.arguments

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.test.utils.asEvent
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.publish_deposit_event.PublishDepositEvent
import sollecitom.services.modulith_example.shared.account.domain.model.event.Deposit
import sollecitom.services.modulith_example.shared.account.domain.test.utils.create

@TestInstance(PER_CLASS)
class ArgumentsJsonSerializationTests : AcmeJsonSerdeTestSpecification<PublishDepositEvent.Arguments>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = PublishDepositEvent.Arguments.jsonSerde

    override fun parameterizedArguments() = listOf(
        "random" to PublishDepositEvent.Arguments(event = Deposit.create().asEvent())
    )
}