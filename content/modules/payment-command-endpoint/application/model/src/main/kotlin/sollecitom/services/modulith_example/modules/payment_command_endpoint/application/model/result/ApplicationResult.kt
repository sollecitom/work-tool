package sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult.Successful
import sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model.PaymentProcessingResult

sealed interface ApplicationResult {

    data class Successful(val processingResult: Deferred<PaymentProcessingResult>) : ApplicationResult

    sealed interface Failed : ApplicationResult {

        data object CannotTargetSourceAccount : Failed
    }

    companion object
}

fun PaymentProcessingResult.asResult(): ApplicationResult = Successful(CompletableDeferred(this))