package sollecitom.services.modulith_example.shared.account.messaing.converter

import sollecitom.libs.pillar.messaging.conventions.AcmeMessagePropertyNames
import sollecitom.libs.swissknife.messaging.domain.event.converter.EventMessageConverterTemplate
import sollecitom.libs.swissknife.messaging.domain.message.converter.MessageConverter
import sollecitom.services.modulith_example.shared.account.domain.model.event.AccountEvent
import sollecitom.services.modulith_example.shared.account.domain.model.event.AccountEventData

val AccountEventData.Companion.messageConverter: MessageConverter<AccountEvent> get() = AccountEventMessageConverter

private object AccountEventMessageConverter : EventMessageConverterTemplate<AccountEvent>(propertyNames = AcmeMessagePropertyNames) {

    override fun key(event: AccountEvent) = event.data.relevantAccount.value
}