package sollecitom.services.modulith_example.modules.deposit_event_endpoint.adapters.driving.http.endpoint.serde.result

import jsonSerde
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.services.modulith_example.modules.deposit_event_endpoint.application.model.result.ApplicationResult

@TestInstance(PER_CLASS)
class ResultJsonSerializationTests : AcmeJsonSerdeTestSpecification<ApplicationResult>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = ApplicationResult.jsonSerde

    override fun parameterizedArguments() = listOf(
        "application-result-success" to ApplicationResult.Successful,
        "application-result-error" to ApplicationResult.Error,
    )
}