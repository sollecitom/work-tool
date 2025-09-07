package sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.result

sealed interface ApplicationResult {

    data object Successful : ApplicationResult

    data object Error : ApplicationResult

    companion object
}