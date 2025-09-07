package sollecitom.services.modulith_example.modules.payment_command_endpoint.adapters.driven.nats

import kotlinx.coroutines.*
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult.ProcessedSuccessfully
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.SendPaymentProcessingResultSubscriber
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommandReceived

// TODO delete after the NATS round-trip will be implemented
class StubbedSendPaymentProcessingResultSubscriber(private val awaitResult: suspend CoroutineScope.() -> PaymentProcessingResult = { ProcessedSuccessfully }) : SendPaymentProcessingResultSubscriber {

    private val scope = CoroutineScope(SupervisorJob())

    override fun SendPaymentCommandReceived.processingResult(): Deferred<PaymentProcessingResult> = with(scope) {

        val deferred = CompletableDeferred<PaymentProcessingResult>()
        async(start = CoroutineStart.UNDISPATCHED) {
            val result = awaitResult()
            deferred.complete(result)
        }
        return deferred
    }
}