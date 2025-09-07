package sollecitom.services.modulith_example.shared.account.serialization.avro.event

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.test.utils.asEvent
import sollecitom.services.modulith_example.shared.account.domain.model.event.AccountEvent
import sollecitom.services.modulith_example.shared.account.domain.model.event.Deposit
import sollecitom.services.modulith_example.shared.account.domain.model.event.OutboundPayment
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand
import sollecitom.services.modulith_example.shared.account.domain.test.utils.create
import sollecitom.services.modulith_example.shared.serialization.avro.ModulithExampleEventAvroSerdes

@TestInstance(PER_CLASS)
class AccountEventAvroSerdeTests : AcmeAvroSerdeTestSpecification<AccountEvent>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = ModulithExampleEventAvroSerdes.accountEvent

    override fun parameterizedArguments() = listOf(
        "deposit" to Deposit.create().asEvent(),
        "outbound-payment" to OutboundPayment.create().asEvent(),
        "inbound-payment" to OutboundPayment.create().asEvent(),
        "send-payment-command-received" to SendPaymentCommand.create().asEvent(),
        "insufficient-balance-for-send-payment-command-received" to SendPaymentCommand.InsufficientBalanceError.create().asEvent(),
        "account-not-found-for-send-payment-command-received" to SendPaymentCommand.AccountNotFoundError.create().asEvent()
    )
}