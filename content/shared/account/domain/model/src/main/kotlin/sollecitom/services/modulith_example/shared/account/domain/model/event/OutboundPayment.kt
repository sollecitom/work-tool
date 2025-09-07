package sollecitom.services.modulith_example.shared.account.domain.model.event

import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference

data class OutboundPayment(val sourceAccount: AccountReference, val amount: CurrencyAmount, val targetAccount: AccountReference) : AccountEventData {

    override val relevantAccount get() = sourceAccount

    override val type: Happening.Type get() = Companion.type

    companion object {
        val type = Happening.Type(name = "outbound-payment".let(::Name), version = 1.let(::IntVersion))
    }
}

typealias OutboundPaymentEvent = Event.Composite<OutboundPayment>