package sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model

import kotlinx.coroutines.Deferred
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommandReceived

interface SendPaymentProcessingResultSubscriber {

    fun SendPaymentCommandReceived.processingResult(): Deferred<PaymentProcessingResult>
}