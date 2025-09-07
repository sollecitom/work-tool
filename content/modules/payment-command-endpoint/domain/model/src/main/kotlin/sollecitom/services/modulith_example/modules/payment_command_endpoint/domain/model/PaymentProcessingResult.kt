package sollecitom.services.modulith_example.modules.payment_command_endpoint.domain.model

import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference

sealed interface PaymentProcessingResult {

    data object ProcessedSuccessfully : PaymentProcessingResult

    sealed interface Rejected : PaymentProcessingResult {

        data object InsufficientBalanceOnSourceAccount : Rejected

        data class NonexistentAccount(val account: AccountReference) : Rejected
    }
}