package sollecitom.services.modulith_example.modules.payment_command_endpoint.module.implementation

import kotlinx.coroutines.delay
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
import sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driven.nats.StubbedSendPaymentProcessingResultSubscriber
import sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.endpoint.SendPaymentCommandEndpoint
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.Application
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.create
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult.ProcessedSuccessfully
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.SendPaymentProcessingResultSubscriber
import sollecitom.services.modulith_example.shared.account.messaging.adapters.accountEvent
import kotlin.time.Duration.Companion.seconds

private class PaymentCommandEndpointModule(pulsarClient: PulsarClient, serviceInfo: ServiceInfo, environment: Environment, defaultLocaleSettings: WithDefaultLocale, coreDataGenerator: CoreDataGenerator, messagePropertyNames: MessagePropertyNames, httpApiDefinition: HttpApiDefinition) : ServiceModuleWithHttpDrivingAdapter, WithDefaultLocale by defaultLocaleSettings, CoreDataGenerator by coreDataGenerator, MessagePropertyNames by messagePropertyNames, HttpApiDefinition by httpApiDefinition {

    override val name get() = moduleName
    private val instanceInfo = serviceInfo.withModuleName(moduleName)
    private val accountEventPublisher = MessagePublisher.Companion.accountEvent(pulsarClient, instanceInfo)
    private val sendPaymentProcessingResultSubscriber: SendPaymentProcessingResultSubscriber = StubbedSendPaymentProcessingResultSubscriber {
        delay(2.seconds)
        ProcessedSuccessfully
    }
    private val application = Application.create(publisher = accountEventPublisher, commandOutcomeSubscriber = sendPaymentProcessingResultSubscriber)
    override val httpEndpoints = setOf(
        SendPaymentCommandEndpoint(application),
    )

    override suspend fun start() = accountEventPublisher.start()

    override suspend fun stop() = accountEventPublisher.stop()

    companion object : Loggable() {

        private val moduleName = "payment-command-endpoint".let(::Name)
    }
}

context(withLocale: WithDefaultLocale, generator: CoreDataGenerator, propertyNames: MessagePropertyNames, definition: HttpApiDefinition)
fun ServiceModule.Companion.paymentCommandEndpoint(pulsarClient: PulsarClient, serviceInfo: ServiceInfo, environment: Environment): ServiceModule = PaymentCommandEndpointModule(pulsarClient, serviceInfo, environment, withLocale, generator, propertyNames, definition)