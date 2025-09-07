package sollecitom.services.modulith_example.modules.account_event_processor.application.model

import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.messaging.domain.event.processing.EventProcessingResult
import sollecitom.libs.swissknife.messaging.domain.event.processing.EventProcessingResult.NoOp
import sollecitom.libs.swissknife.messaging.domain.event.processing.processAsCompositeEvent
import sollecitom.libs.swissknife.messaging.domain.event.utils.eventType
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.message.into
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames
import sollecitom.libs.swissknife.messaging.domain.message.publisher.MessagePublisher
import sollecitom.services.modulith_example.shared.account.domain.model.event.*
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand.AccountNotFoundError
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand.InsufficientBalanceError

interface Application : ProcessAccountEvent {

    companion object
}

private class ApplicationImplementation(private val publisher: MessagePublisher<AccountEvent>, coreDataGenerator: CoreDataGenerator, messagePropertyNames: MessagePropertyNames) : Application, MessagePropertyNames by messagePropertyNames, CoreDataGenerator by coreDataGenerator {

    context(_: InvocationContext<*>)
    override suspend fun processAccountEvent(message: ReceivedMessage<AccountEvent>) = when (val type = message.eventType()) {
        Deposit.type -> processDeposit(message.into())
        SendPaymentCommand.type -> processSendPaymentCommand(message.into())
        InboundPayment.type -> processInboundPayment(message.into())
        OutboundPayment.type -> processOutboundPayment(message.into())
        AccountNotFoundError.type -> NoOp
        InsufficientBalanceError.type -> NoOp
        else -> {
            logger.warn { "Unexpected account event type: $type" }
            NoOp
        }
    }

    context(_: InvocationContext<*>)
    private suspend fun processDeposit(message: ReceivedMessage<DepositEvent>) = message.processAsCompositeEvent { data, event ->

        // TODO increase balance for target account, if it exists. If not, publish AccountNotFoundError
    }

    context(_: InvocationContext<*>)
    private suspend fun processSendPaymentCommand(message: ReceivedMessage<SendPaymentCommandReceived>) = message.processAsCompositeEvent { data, event ->

        // TODO if target account or source account don't exist, publish AccountNotFoundError
        // TODO if there's balance, publish outbound payment
        // TODO if there's no balance, publish NotEnoughBalance
    }

    context(_: InvocationContext<*>)
    private suspend fun processInboundPayment(message: ReceivedMessage<InboundPaymentEvent>) = message.processAsCompositeEvent { data, event ->

        // TODO increase balance for target account, if it exists. If not, publish AccountNotFoundError
    }

    context(_: InvocationContext<*>)
    private suspend fun processOutboundPayment(message: ReceivedMessage<OutboundPaymentEvent>) = message.processAsCompositeEvent { data, event ->

        // TODO decrease balance (already checked)
        // TODO publish inbound payment (which will be processed as inbound on another partition)
    }

    companion object : Loggable()
}

fun interface ProcessAccountEvent {

    context(_: InvocationContext<*>)
    suspend fun processAccountEvent(message: ReceivedMessage<AccountEvent>): EventProcessingResult

    companion object
}

context(generator: CoreDataGenerator, propertyNames: MessagePropertyNames)
fun Application.Companion.create(publisher: MessagePublisher<AccountEvent>): Application = ApplicationImplementation(publisher = publisher, coreDataGenerator = generator, messagePropertyNames = propertyNames)