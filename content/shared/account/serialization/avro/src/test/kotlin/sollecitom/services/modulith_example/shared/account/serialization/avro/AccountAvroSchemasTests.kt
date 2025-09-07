package sollecitom.services.modulith_example.shared.account.serialization.avro

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.avro.schema.catalogue.test.utils.SchemaContainerTestSpecification

@TestInstance(PER_CLASS)
class AccountAvroSchemasTests : SchemaContainerTestSpecification {

    override val candidate = AccountAvroSchemas
}