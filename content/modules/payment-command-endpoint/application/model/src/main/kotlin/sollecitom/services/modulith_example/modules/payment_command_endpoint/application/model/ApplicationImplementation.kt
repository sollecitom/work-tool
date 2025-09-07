package sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model

import kotlinx.coroutines.Deferred
import kotlin.time.Instant
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.asEvent
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames
import sollecitom.libs.swissknife.messaging.domain.message.publisher.MessagePublisher
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult.Failed.CannotTargetSourceAccount
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.send_payment.SendPayment
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.SendPaymentProcessingResultSubscriber
import sollecitom.services.modulith_example.shared.account.domain.model.event.AccountEvent
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommandReceived

private class ApplicationImplementation(private val publisher: MessagePublisher<AccountEvent>, commandOutcomeSubscriber: SendPaymentProcessingResultSubscriber, coreDataGenerator: CoreDataGenerator, messagePropertyNames: MessagePropertyNames) : Application, SendPaymentProcessingResultSubscriber by commandOutcomeSubscriber, MessagePropertyNames by messagePropertyNames, CoreDataGenerator by coreDataGenerator {

    context(_: InvocationContext<*>)
    override suspend fun sendPayment(arguments: SendPayment.Arguments): ApplicationResult {

        val event = arguments.asEvent { error ->
            if (error is SendPaymentCommand.CannotTargetSourceAccountException) return CannotTargetSourceAccount
            throw error
        }
        val processingResult = event.processingResult()
        publisher.publish(event)
        return processingResult.asApplicationResult()
    }

    private fun Deferred<PaymentProcessingResult>.asApplicationResult(): ApplicationResult = ApplicationResult.Successful(this)

    context(_: InvocationContext<*>)
    private inline fun SendPayment.Arguments.asEvent(id: Id = newId(), timestamp: Instant = clock.now(), onError: (Exception) -> Nothing): SendPaymentCommandReceived {

        val commandReceived = try {
            SendPaymentCommand(sourceAccount, amount, targetAccount)
        } catch (error: Exception) {
            onError(error)
        }
        return commandReceived.asEvent(id = id, timestamp = timestamp)
    }

    context(context: InvocationContext<*>)
    private fun SendPaymentCommand.asEvent(id: Id = newId(), timestamp: Instant = clock.now()) = asEvent(Event.Metadata(id = id, timestamp = timestamp, context = Event.Context(invocation = context)))
}

fun Application.Companion.create(publisher: MessagePublisher<AccountEvent>, commandOutcomeSubscriber: SendPaymentProcessingResultSubscriber, coreDataGenerator: CoreDataGenerator, messagePropertyNames: MessagePropertyNames): Application = ApplicationImplementation(publisher = publisher, commandOutcomeSubscriber = commandOutcomeSubscriber, coreDataGenerator = coreDataGenerator, messagePropertyNames = messagePropertyNames)

context(generator: CoreDataGenerator, propertyNames: MessagePropertyNames)
fun Application.Companion.create(publisher: MessagePublisher<AccountEvent>, commandOutcomeSubscriber: SendPaymentProcessingResultSubscriber): Application = ApplicationImplementation(publisher, commandOutcomeSubscriber, generator, propertyNames)