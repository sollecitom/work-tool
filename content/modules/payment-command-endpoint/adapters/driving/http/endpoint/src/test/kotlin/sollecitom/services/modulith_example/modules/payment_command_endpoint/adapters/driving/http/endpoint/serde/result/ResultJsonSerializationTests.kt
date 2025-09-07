package sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.endpoint.serde.result

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult.Failed
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult.Failed.CannotTargetSourceAccount
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult.Successful
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.asResult
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult.ProcessedSuccessfully
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult.Rejected.InsufficientBalanceOnSourceAccount
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult.Rejected.NonexistentAccount
import sollecitom.services.modulith_example.shared.account.domain.model.reference.InternalAccountNumber
import sollecitom.services.modulith_example.shared.account.domain.test.utils.create

@TestInstance(PER_CLASS)
class ResultJsonSerializationTests : AcmeJsonSerdeTestSpecification<ApplicationResult>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = ApplicationResult.jsonSerde

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun Assert<ApplicationResult>.matches(expected: ApplicationResult) = given { actual ->

        when(actual) {
            is Successful -> {
                assertThat(expected).isInstanceOf<Successful>()
                expected as Successful
                assertThat(actual.processingResult.getCompleted()).isEqualTo(expected.processingResult.getCompleted())
            }
            is Failed -> assertThat(actual).isEqualTo(expected)
        }
    }

    override fun parameterizedArguments() = listOf(
        "processed-successfully" to ProcessedSuccessfully.asResult(),
        "non-existent-account" to NonexistentAccount(account = InternalAccountNumber.create()).asResult(),
        "insufficient-balance-on-source-account" to InsufficientBalanceOnSourceAccount.asResult(),
        "cannot-target-source-account-error" to CannotTargetSourceAccount,
    )
}