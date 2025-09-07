package sollecitom.services.modulith_example.shared.account.domain.model.event

import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference

data class SendPaymentCommand(val sourceAccount: AccountReference, val amount: CurrencyAmount, val targetAccount: AccountReference) : AccountEventData {

    init {
        if (sourceAccount == targetAccount) {
            throw CannotTargetSourceAccountException(sourceAccount)
        }
    }

    override val relevantAccount get() = sourceAccount

    override val type: Happening.Type get() = Companion.type

    data class CannotTargetSourceAccountException(val account: AccountReference) : IllegalArgumentException("Cannot target the source account for a payment")

    data class InsufficientBalanceError(val attempt: SendPaymentCommand) : AccountEventData {

        override val relevantAccount get() = attempt.sourceAccount

        override val type: Happening.Type get() = Companion.type

        companion object {
            val type = Happening.Type(name = "send-payment-command-received-insufficient-balance-error".let(::Name), version = 1.let(::IntVersion))
        }
    }

    data class AccountNotFoundError(val attempt: SendPaymentCommand) : AccountEventData {

        override val relevantAccount get() = attempt.sourceAccount

        override val type: Happening.Type get() = Companion.type

        companion object {
            val type = Happening.Type(name = "account-not-found-error".let(::Name), version = 1.let(::IntVersion))
        }
    }

    companion object {
        val type = Happening.Type(name = "send-payment-command-received".let(::Name), version = 1.let(::IntVersion))
    }
}

typealias SendPaymentCommandReceived = Event.Composite<SendPaymentCommand>