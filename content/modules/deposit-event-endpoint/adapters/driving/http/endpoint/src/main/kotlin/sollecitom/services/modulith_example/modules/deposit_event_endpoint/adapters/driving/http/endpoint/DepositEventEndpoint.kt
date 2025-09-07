package sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.endpoint

import jsonSerde
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import sollecitom.libs.pillar.web.api.utils.endpoint.toAuthenticated
import sollecitom.libs.swissknife.http4k.utils.body
import sollecitom.libs.swissknife.http4k.utils.lens.jsonObject
import sollecitom.libs.swissknife.http4k.utils.lens.map
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.web.api.domain.endpoint.EventEndpoint
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.endpoint.serde.arguments.jsonSerde
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.publish_deposit_event.PublishDepositEvent
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.result.ApplicationResult

class DepositEventEndpoint(private val application: PublishDepositEvent) : EventEndpoint {

    override val eventName = "deposit"

    override val route = path bind Method.POST toAuthenticated { request ->

        logger.info { "Received a request on path '$path' with arguments $arguments" }
        val arguments = arguments(request)

        val result = application.publishDepositEvent(arguments = arguments)

        logger.info { "Finished processing the request on path '$path' with result {$result}" }
        Response.Companion(Status.Companion.OK).body(result, ApplicationResult.Companion.jsonSerde)
    }

    private companion object : Loggable() {
        private val arguments = Body.Companion.jsonObject().map(PublishDepositEvent.Arguments.jsonSerde).toLens()
    }
}