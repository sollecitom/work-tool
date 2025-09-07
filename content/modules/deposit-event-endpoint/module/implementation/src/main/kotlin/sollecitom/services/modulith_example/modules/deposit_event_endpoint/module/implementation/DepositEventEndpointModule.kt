package sollecitom.services.modulith_example.modules.deposit_event_endpoint.module.implementation

import org.apache.pulsar.client.api.PulsarClient
import org.http4k.config.Environment
import sollecitom.libs.swissknife.core.domain.locale.WithDefaultLocale
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames
import sollecitom.libs.swissknife.messaging.domain.message.publisher.MessagePublisher
import sollecitom.libs.swissknife.service.domain.ServiceInfo
import sollecitom.libs.swissknife.service.domain.ServiceModule
import sollecitom.libs.swissknife.service.domain.ServiceModuleWithHttpDrivingAdapter
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.endpoint.DepositEventEndpoint
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.Application
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.create
import sollecitom.services.modulith_example.shared.account.messaging.adapters.accountEvent

private class DepositEventEndpointModule(pulsarClient: PulsarClient, serviceInfo: ServiceInfo, environment: Environment, defaultLocaleSettings: WithDefaultLocale, coreDataGenerator: CoreDataGenerator, messagePropertyNames: MessagePropertyNames, httpApiDefinition: HttpApiDefinition) : ServiceModuleWithHttpDrivingAdapter, WithDefaultLocale by defaultLocaleSettings, CoreDataGenerator by coreDataGenerator, MessagePropertyNames by messagePropertyNames, HttpApiDefinition by httpApiDefinition {

    override val name get() = moduleName
    private val instanceInfo = serviceInfo.withModuleName(moduleName)
    private val accountEventPublisher = MessagePublisher.Companion.accountEvent(pulsarClient, instanceInfo)
    private val application = Application.create(publisher = accountEventPublisher)
    override val httpEndpoints = setOf(
        DepositEventEndpoint(application),
    )

    override suspend fun start() = accountEventPublisher.start()

    override suspend fun stop() = accountEventPublisher.stop()

    companion object : Loggable() {

        private val moduleName = "deposit-event-endpoint".let(::Name)
    }
}

context(withLocale: WithDefaultLocale, generator: CoreDataGenerator, propertyNames: MessagePropertyNames, definition: HttpApiDefinition)
fun ServiceModule.Companion.depositEventEndpoint(pulsarClient: PulsarClient, serviceInfo: ServiceInfo, environment: Environment): ServiceModule = DepositEventEndpointModule(pulsarClient, serviceInfo, environment, withLocale, generator, propertyNames, definition)