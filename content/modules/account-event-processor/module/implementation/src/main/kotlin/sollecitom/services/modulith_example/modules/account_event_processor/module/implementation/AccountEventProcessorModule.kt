package sollecitom.services.modulith_example.modules.account_event_processor.module.implementation

import org.apache.pulsar.client.api.PulsarClient
import org.http4k.config.Environment
import sollecitom.libs.pillar.messaging.domain.event.processing.withMessageConnector
import sollecitom.libs.swissknife.core.domain.locale.WithDefaultLocale
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.domain.EventProcessor
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.messaging.domain.message.connector.MessageConnector
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames
import sollecitom.libs.swissknife.service.domain.ServiceInfo
import sollecitom.libs.swissknife.service.domain.ServiceModule
import sollecitom.services.modulith_example.modules.account_event_processor.application.model.Application
import sollecitom.services.modulith_example.modules.account_event_processor.application.model.create
import sollecitom.services.modulith_example.shared.account.messaging.adapters.accountEvents

private class AccountEventProcessorModule(pulsarClient: PulsarClient, serviceInfo: ServiceInfo, environment: Environment, defaultLocaleSettings: WithDefaultLocale, coreDataGenerator: CoreDataGenerator, messagePropertyNames: MessagePropertyNames) : ServiceModule, WithDefaultLocale by defaultLocaleSettings, CoreDataGenerator by coreDataGenerator, MessagePropertyNames by messagePropertyNames {

    override val name get() = moduleName
    private val instanceInfo = serviceInfo.withModuleName(moduleName)
    private val accountEvents = MessageConnector.accountEvents(pulsarClient, instanceInfo)
    private val application = Application.create(publisher = accountEvents)
    private val processor = EventProcessor.withMessageConnector(accountEvents) { event -> application.processAccountEvent(event) }

    override suspend fun start() {
        accountEvents.start()
        processor.start()
    }

    override suspend fun stop() {
        processor.stop()
        accountEvents.stop()
    }

    companion object : Loggable() {

        private val moduleName = "account-event-processor".let(::Name)
    }
}

context(withLocale: WithDefaultLocale, generator: CoreDataGenerator, propertyNames: MessagePropertyNames)
fun ServiceModule.Companion.accountEventProcessor(pulsarClient: PulsarClient, serviceInfo: ServiceInfo, environment: Environment): ServiceModule = AccountEventProcessorModule(pulsarClient, serviceInfo, environment, withLocale, generator, propertyNames)