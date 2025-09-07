package sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.serialization.json.event

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.test.utils.asEvent
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommandReceived
import sollecitom.services.modulith_example.shared.account.domain.test.utils.create

@TestInstance(PER_CLASS)
class SendPaymentCommandReceivedJsonSerializationTests : AcmeJsonSerdeTestSpecification<SendPaymentCommandReceived>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = sendPaymentCommandReceivedJsonSerde

    override fun parameterizedArguments() = listOf(
        "random" to SendPaymentCommand.create().asEvent()
    )
}