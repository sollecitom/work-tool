package sollecitom.services.modulith_example.shared.account.domain.model.event

fun SendPaymentCommand.rejectedDueToInsufficientBalance(): SendPaymentCommand.InsufficientBalanceError = SendPaymentCommand.InsufficientBalanceError(attempt = this)

fun SendPaymentCommand.rejectedDueToAccountNotFound(): SendPaymentCommand.AccountNotFoundError = SendPaymentCommand.AccountNotFoundError(attempt = this)

fun SendPaymentCommand.accepted(): OutboundPayment = OutboundPayment(sourceAccount = sourceAccount, amount = amount, targetAccount = targetAccount)

fun OutboundPayment.inbound(): InboundPayment = InboundPayment(targetAccount = sourceAccount, sourceAccount = targetAccount, amount = amount)