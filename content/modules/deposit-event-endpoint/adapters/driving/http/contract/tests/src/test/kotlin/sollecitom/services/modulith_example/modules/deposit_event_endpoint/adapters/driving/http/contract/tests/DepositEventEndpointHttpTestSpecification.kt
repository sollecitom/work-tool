package sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.contract.tests

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import jsonSerde
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
import sollecitom.libs.swissknife.ddd.test.utils.asEvent
import sollecitom.libs.swissknife.http4k.utils.body
import sollecitom.libs.swissknife.test.utils.standard.output.withCapturedStandardOutput
import sollecitom.libs.swissknife.web.api.test.utils.EventEndpointHttpTestSpecification
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.api.HttpDrivingAdapter
import sollecitom.libs.swissknife.web.api.utils.micrometer.http.httpServerRequestLatencyMeasurements
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.endpoint.DepositEventEndpoint
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.endpoint.serde.arguments.jsonSerde
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.Application
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.publish_deposit_event.PublishDepositEvent
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.result.ApplicationResult
import sollecitom.services.modulith_example.shared.account.domain.model.event.Deposit
import sollecitom.services.modulith_example.shared.account.domain.model.event.DepositEvent
import sollecitom.services.modulith_example.shared.account.domain.test.utils.create

interface DepositEventEndpointHttpTestSpecification : ErrorsEndpointHttpTestSpecification, EventEndpointHttpTestSpecification {

    override val eventName get() = "deposit"
    override val version get() = "1"
    override fun validRequestPayload() = requestPayload()
    override fun apiWithValidResponse() = api()
    override fun invalidInvocationContext() = InvocationContext.unauthenticated()
    override fun validInvocationContext() = InvocationContext.authenticated()

    @Test
    fun `publishing deposit events over HTTP`() {

        val meterRegistry = SimpleMeterRegistry()
        val event = Deposit.create().asEvent()
        var receivedEvent: DepositEvent? = null
        val api = api(meterRegistry) { args ->
            receivedEvent = args.event
            ApplicationResult.Successful
        }
        val json = requestPayload(event = event)
        val invocationContext = InvocationContext.authenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        val request = Request(Method.POST, path(path)).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val (response, logs) = withCapturedStandardOutput { api(request) }

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.OK)
        val result = response.body(ApplicationResult.Companion.jsonSerde)
        assertThat(result).isEqualTo(ApplicationResult.Successful)
        assertThat(receivedEvent).isNotNull().isEqualTo(event)
        assertThat(logs).haveContextForkedFrom(invocationContext)
        assertThat(meterRegistry.httpServerRequestLatencyMeasurements.count).isEqualTo(1)
    }

    @Test
    fun `handling application errors`() {

        val meterRegistry = SimpleMeterRegistry()
        val event = Deposit.create().asEvent()
        var receivedEvent: DepositEvent? = null
        val api = api(meterRegistry) { args ->
            receivedEvent = args.event
            ApplicationResult.Error
        }
        val json = requestPayload(event = event)
        val invocationContext = InvocationContext.authenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        val request = Request(Method.POST, path(path)).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val (response, logs) = withCapturedStandardOutput { api(request) }

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.OK)
        val result = response.body(ApplicationResult.Companion.jsonSerde)
        assertThat(result).isEqualTo(ApplicationResult.Error)
        assertThat(receivedEvent).isNotNull().isEqualTo(event)
        assertThat(logs).haveContextForkedFrom(invocationContext)
        assertThat(meterRegistry.httpServerRequestLatencyMeasurements.count).isEqualTo(1)
    }

    private fun requestPayload(event: DepositEvent = Deposit.create().asEvent()) = PublishDepositEvent.Arguments.jsonSerde.serialize(PublishDepositEvent.Arguments(event))

    context(time: TimeGenerator, definition: HttpApiDefinition)
    private fun api(
        meterRegistry: MeterRegistry = SimpleMeterRegistry(), application: PublishDepositEvent = PublishDepositEvent { ApplicationResult.Successful }
    ) = httpDrivingAdapter(meterRegistry, object : Application, PublishDepositEvent by application {

        context(_: InvocationContext<*>)
        override suspend fun publishDepositEvent(arguments: PublishDepositEvent.Arguments): ApplicationResult = application.publishDepositEvent(arguments)
    })

    private fun httpDrivingAdapter(meterRegistry: MeterRegistry, application: Application) = EndpointHttpDrivingAdapter.create(endpoints = setOf(DepositEventEndpoint(application)), configuration = HttpDrivingAdapter.Configuration(requestedPort = RequestedPort.randomAvailable), meterRegistry = meterRegistry)
}