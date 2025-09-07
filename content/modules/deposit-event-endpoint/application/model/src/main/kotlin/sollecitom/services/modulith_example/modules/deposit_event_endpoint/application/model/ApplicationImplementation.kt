package sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model

import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames
import sollecitom.libs.swissknife.messaging.domain.message.publisher.MessagePublisher
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.publish_deposit_event.PublishDepositEvent
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.result.ApplicationResult
import sollecitom.services.modulith_example.shared.account.domain.model.event.AccountEvent

private class ApplicationImplementation(private val publisher: MessagePublisher<AccountEvent>, coreDataGenerator: CoreDataGenerator, messagePropertyNames: MessagePropertyNames) : Application, MessagePropertyNames by messagePropertyNames, CoreDataGenerator by coreDataGenerator {

    context(_: InvocationContext<*>)
    override suspend fun publishDepositEvent(arguments: PublishDepositEvent.Arguments): ApplicationResult {

        publisher.publish(arguments.event)
        return ApplicationResult.Successful
    }
}

fun Application.Companion.create(publisher: MessagePublisher<AccountEvent>, coreDataGenerator: CoreDataGenerator, messagePropertyNames: MessagePropertyNames): Application = ApplicationImplementation(publisher = publisher, coreDataGenerator = coreDataGenerator, messagePropertyNames = messagePropertyNames)

context(generator: CoreDataGenerator, propertyNames: MessagePropertyNames)
fun Application.Companion.create(publisher: MessagePublisher<AccountEvent>): Application = ApplicationImplementation(publisher, generator, propertyNames)