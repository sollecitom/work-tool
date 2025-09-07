package sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.publish_deposit_event

import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.result.ApplicationResult
import sollecitom.services.modulith_example.shared.account.domain.model.event.DepositEvent

fun interface PublishDepositEvent {

    context(_: InvocationContext<*>)
    suspend fun publishDepositEvent(arguments: Arguments): ApplicationResult

    data class Arguments(val event: DepositEvent) {

        companion object
    }
}