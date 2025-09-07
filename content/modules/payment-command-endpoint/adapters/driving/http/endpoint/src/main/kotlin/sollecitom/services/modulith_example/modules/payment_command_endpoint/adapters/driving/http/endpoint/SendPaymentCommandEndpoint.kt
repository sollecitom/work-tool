package sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.endpoint

import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import sollecitom.libs.pillar.web.api.utils.endpoint.toAuthenticated
import sollecitom.libs.swissknife.http4k.utils.body
import sollecitom.libs.swissknife.http4k.utils.lens.jsonObject
import sollecitom.libs.swissknife.http4k.utils.lens.map
import sollecitom.libs.swissknife.kotlin.extensions.async.await
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.web.api.domain.endpoint.CommandEndpoint
import sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.endpoint.serde.argument.jsonSerde
import sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driving.http.endpoint.serde.result.jsonSerde
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.send_payment.SendPayment
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class SendPaymentCommandEndpoint(private val application: SendPayment, private val processingTimeout: Duration = defaultProcessingTimeout) : CommandEndpoint {

    override val commandName = "send-payment"

    override val route = path bind Method.POST toAuthenticated { request ->

        logger.info { "Received a request on path '$path' with arguments $arguments" }
        val arguments = arguments(request)

        val result = application.sendPayment(arguments = arguments)
        if (result is ApplicationResult.Successful) {
            result.processingResult.await(processingTimeout)
        }

        logger.info { "Finished processing the request on path '$path' with result {$result}" }
        Response(Status.OK).body(result, ApplicationResult.jsonSerde)
    }

    private companion object : Loggable() {
        private val arguments = Body.Companion.jsonObject().map(SendPayment.Arguments.jsonSerde).toLens()
        private val defaultProcessingTimeout = 10.seconds
    }
}