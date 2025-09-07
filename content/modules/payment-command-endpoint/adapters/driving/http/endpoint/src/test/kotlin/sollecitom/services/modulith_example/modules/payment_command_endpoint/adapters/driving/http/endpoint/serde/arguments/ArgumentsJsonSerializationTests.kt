package sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.endpoint.serde.arguments

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.endpoint.serde.argument.jsonSerde
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.send_payment.SendPayment
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand
import sollecitom.services.modulith_example.shared.account.domain.test.utils.create

@TestInstance(PER_CLASS)
class ArgumentsJsonSerializationTests : AcmeJsonSerdeTestSpecification<SendPayment.Arguments>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = SendPayment.Arguments.jsonSerde

    override fun parameterizedArguments() = listOf(
        "random" to SendPayment.Arguments(command = SendPaymentCommand.create())
    )
}