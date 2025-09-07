package sollecitom.services.modulith_example.shared.account.domain.model.event

import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.services.modulith_example.shared.account.domain.model.reference.AccountReference

sealed interface AccountEventData : Event.Data {

    val relevantAccount: AccountReference

    companion object
}

typealias AccountEvent = Event.Composite<AccountEventData>