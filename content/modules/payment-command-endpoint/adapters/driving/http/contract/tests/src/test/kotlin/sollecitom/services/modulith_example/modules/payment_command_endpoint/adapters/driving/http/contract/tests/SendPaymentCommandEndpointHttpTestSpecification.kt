package sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.contract.tests

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineStart.UNDISPATCHED
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import sollecitom.libs.pillar.correlation.logging.test.utils.haveContextForkedFrom
import sollecitom.libs.pillar.web.api.test.utils.ErrorsEndpointHttpTestSpecification
import sollecitom.libs.pillar.web.api.utils.api.EndpointHttpDrivingAdapter
import sollecitom.libs.pillar.web.api.utils.api.create
import sollecitom.libs.pillar.web.api.utils.api.withInvocationContext
import sollecitom.libs.swissknife.core.domain.networking.RequestedPort
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.domain.toggles.standard.invocation.visibility.InvocationVisibility
import sollecitom.libs.swissknife.correlation.core.domain.toggles.withToggle
import sollecitom.libs.swissknife.correlation.core.test.utils.context.authenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.context.unauthenticated
import sollecitom.libs.swissknife.http4k.utils.body
import sollecitom.libs.swissknife.test.utils.execution.utils.test
import sollecitom.libs.swissknife.test.utils.standard.output.withCapturedStandardOutput
import sollecitom.libs.swissknife.web.api.test.utils.CommandEndpointHttpTestSpecification
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.api.HttpDrivingAdapter
import sollecitom.libs.swissknife.web.api.utils.micrometer.http.httpServerRequestLatencyMeasurements
import sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.endpoint.SendPaymentCommandEndpoint
import sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.endpoint.serde.argument.jsonSerde
import sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.endpoint.serde.result.jsonSerde
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.Application
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult.Successful
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.send_payment.SendPayment
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult.ProcessedSuccessfully
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult.Rejected.InsufficientBalanceOnSourceAccount
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult.Rejected.NonexistentAccount
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand
import sollecitom.services.modulith_example.shared.account.domain.test.utils.create
import kotlin.time.Duration.Companion.seconds

interface SendPaymentCommandEndpointHttpTestSpecification : ErrorsEndpointHttpTestSpecification, CommandEndpointHttpTestSpecification {

    override val commandName get() = "send-payment"
    override val version get() = "1"
    override fun validRequestPayload() = requestPayload()
    override fun apiWithValidResponse() = api()
    override fun invalidInvocationContext() = InvocationContext.unauthenticated()
    override fun validInvocationContext() = InvocationContext.authenticated()

    @Test
    fun `publishing payment commands over HTTP`() {

        val meterRegistry = SimpleMeterRegistry()
        val command = SendPaymentCommand.create()
        var receivedCommand: SendPaymentCommand? = null
        val processingResult = ProcessedSuccessfully
        val applicationResult = Successful(CompletableDeferred(processingResult))
        val api = api(meterRegistry) { args ->
            receivedCommand = SendPaymentCommand(args.sourceAccount, args.amount, args.targetAccount)
            applicationResult
        }
        val json = requestPayload(command = command)
        val invocationContext = InvocationContext.authenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        val request = Request(Method.POST, path(path)).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val (response, logs) = withCapturedStandardOutput { api(request) }

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.OK)
        val result = response.body(ApplicationResult.Companion.jsonSerde)
        assertThat(result).matches(applicationResult)
        assertThat(receivedCommand).isNotNull().isEqualTo(command)
        assertThat(logs).haveContextForkedFrom(invocationContext)
        assertThat(meterRegistry.httpServerRequestLatencyMeasurements.count).isEqualTo(1)
    }

    @Test
    fun `handling delayed processing results`() = test {

        val meterRegistry = SimpleMeterRegistry()
        val command = SendPaymentCommand.create()
        var receivedCommand: SendPaymentCommand? = null
        val delayedProcessingResult = CompletableDeferred<PaymentProcessingResult>()
        val outcome = setOf(ProcessedSuccessfully, NonexistentAccount(account = command.sourceAccount), InsufficientBalanceOnSourceAccount).random(random)
        val applicationResult = Successful(delayedProcessingResult)
        val api = api(meterRegistry) { args ->
            receivedCommand = SendPaymentCommand(args.sourceAccount, args.amount, args.targetAccount)
            applicationResult
        }
        val json = requestPayload(command = command)
        val invocationContext = InvocationContext.authenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        async(start = UNDISPATCHED) {
            delay(1.seconds)
            delayedProcessingResult.complete(outcome)
        }
        val request = Request(Method.POST, path(path)).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val (response, logs) = withCapturedStandardOutput { api(request) }

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.OK)
        val result = response.body(ApplicationResult.Companion.jsonSerde)
        assertThat(result).matches(applicationResult)
        assertThat(receivedCommand).isNotNull().isEqualTo(command)
        assertThat(logs).haveContextForkedFrom(invocationContext)
        assertThat(meterRegistry.httpServerRequestLatencyMeasurements.count).isEqualTo(1)
    }

    @Test
    fun `handling application errors`() {

        val meterRegistry = SimpleMeterRegistry()
        val command = SendPaymentCommand.create()
        var receivedCommand: SendPaymentCommand? = null
        val applicationResult = ApplicationResult.Failed.CannotTargetSourceAccount
        val api = api(meterRegistry) { args ->
            receivedCommand = SendPaymentCommand(args.sourceAccount, args.amount, args.targetAccount)
            applicationResult
        }
        val json = requestPayload(command = command)
        val invocationContext = InvocationContext.authenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        val request = Request(Method.POST, path(path)).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val (response, logs) = withCapturedStandardOutput { api(request) }

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.OK)
        val result = response.body(ApplicationResult.Companion.jsonSerde)
        assertThat(result).matches(applicationResult)
        assertThat(receivedCommand).isNotNull().isEqualTo(command)
        assertThat(logs).haveContextForkedFrom(invocationContext)
        assertThat(meterRegistry.httpServerRequestLatencyMeasurements.count).isEqualTo(1)
    }

    private fun requestPayload(command: SendPaymentCommand = SendPaymentCommand.create()) = SendPayment.Arguments.jsonSerde.serialize(SendPayment.Arguments(command))

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Assert<ApplicationResult>.matches(expected: ApplicationResult) = given { actual ->

        when(actual) {
            is ApplicationResult.Failed -> assertThat(actual).isEqualTo(expected)
            is Successful -> {
                assertThat(expected).isInstanceOf<Successful>()
                expected as Successful
                assertThat(actual.processingResult.getCompleted()).isEqualTo(expected.processingResult.getCompleted())
            }
        }
    }

    context(time: TimeGenerator, definition: HttpApiDefinition)
    private fun api(
        meterRegistry: MeterRegistry = SimpleMeterRegistry(), application: SendPayment = SendPayment { Successful(processingResult = CompletableDeferred(ProcessedSuccessfully)) }
    ) = httpDrivingAdapter(meterRegistry, object : Application {

        context(_: InvocationContext<*>)
        override suspend fun sendPayment(arguments: SendPayment.Arguments): ApplicationResult = application.sendPayment(arguments)
    })

    private fun httpDrivingAdapter(meterRegistry: MeterRegistry, application: Application) = EndpointHttpDrivingAdapter.create(endpoints = setOf(SendPaymentCommandEndpoint(application)), configuration = HttpDrivingAdapter.Configuration(requestedPort = RequestedPort.randomAvailable), meterRegistry = meterRegistry)
}