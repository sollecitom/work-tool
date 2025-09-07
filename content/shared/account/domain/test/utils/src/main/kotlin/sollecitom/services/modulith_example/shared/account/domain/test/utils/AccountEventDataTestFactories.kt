package sollecitom.services.modulith_example.shared.account.domain.test.utils

import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.core.test.utils.currency.create
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.services.modulith_example.shared.account.domain.model.event.Deposit
import sollecitom.services.modulith_example.shared.account.domain.model.event.InboundPayment
import sollecitom.services.modulith_example.shared.account.domain.model.event.OutboundPayment
import sollecitom.services.modulith_example.shared.account.domain.model.event.SendPaymentCommand
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference
import sollecitom.services.modulith_example.shared.account.domain.model.reference.InternalAccountNumber

context(_: RandomGenerator)
fun SendPaymentCommand.Companion.create(sourceAccount: AccountReference = InternalAccountNumber.create(), amount: CurrencyAmount = CurrencyAmount.create(), targetAccount: InternalAccountNumber = InternalAccountNumber.create()) = SendPaymentCommand(sourceAccount = sourceAccount, amount = amount, targetAccount = targetAccount)

context(_: RandomGenerator)
fun SendPaymentCommand.InsufficientBalanceError.Companion.create(attempt: SendPaymentCommand = SendPaymentCommand.create()) = SendPaymentCommand.InsufficientBalanceError(attempt)

context(_: RandomGenerator)
fun SendPaymentCommand.AccountNotFoundError.Companion.create(attempt: SendPaymentCommand = SendPaymentCommand.create()) = SendPaymentCommand.AccountNotFoundError(attempt)

context(_: RandomGenerator)
fun Deposit.Companion.create(targetAccount: AccountReference = InternalAccountNumber.create(), amount: CurrencyAmount = CurrencyAmount.create()) = Deposit(targetAccount = targetAccount, amount = amount)

context(_: RandomGenerator)
fun OutboundPayment.Companion.create(sourceAccount: AccountReference = InternalAccountNumber.create(), amount: CurrencyAmount = CurrencyAmount.create(), targetAccount: InternalAccountNumber = InternalAccountNumber.create()) = OutboundPayment(sourceAccount = sourceAccount, amount = amount, targetAccount = targetAccount)

context(_: RandomGenerator)
fun InboundPayment.Companion.create(sourceAccount: AccountReference = InternalAccountNumber.create(), amount: CurrencyAmount = CurrencyAmount.create(), targetAccount: InternalAccountNumber = InternalAccountNumber.create()) = InboundPayment(sourceAccount = sourceAccount, amount = amount, targetAccount = targetAccount)