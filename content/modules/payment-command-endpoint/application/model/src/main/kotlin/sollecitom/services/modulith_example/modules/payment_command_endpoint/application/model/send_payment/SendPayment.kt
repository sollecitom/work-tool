package sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.send_payment

import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.services.modulith_example.modules.payment_command_endpoint.application.model.result.ApplicationResult
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference

fun interface SendPayment {

    context(_: InvocationContext<*>)
    suspend fun sendPayment(arguments: Arguments): ApplicationResult

    data class Arguments(val sourceAccount: AccountReference, val amount: CurrencyAmount, val targetAccount: AccountReference) {

        constructor(command: SendPaymentCommand) : this(sourceAccount = command.sourceAccount, amount = command.amount, targetAccount = command.targetAccount)

        companion object
    }
}