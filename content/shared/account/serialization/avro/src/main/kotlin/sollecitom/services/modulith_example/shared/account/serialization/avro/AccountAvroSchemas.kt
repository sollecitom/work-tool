package sollecitom.services.modulith_example.shared.account.serialization.avro

import org.apache.avro.Schema
import sollecitom.libs.pillar.avro.serialization.core.currency.avroSchema
import sollecitom.libs.pillar.avro.serialization.ddd.event.avroSchema
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.ddd.domain.Event.Metadata as EventMetadata

object AccountAvroSchemas : AvroSchemaCatalogueTemplate(AccountAvroNamespace.NAME) {

    val internalAccountNumber by lazy { getSchema(name = "InternalAccountNumber") }
    val accountReference by lazy { getSchema(name = "AccountReference", dependencies = setOf(internalAccountNumber)) }

    override val nestedContainers: Set<AvroSchemaContainer> = setOf(Event)
    override val all: Sequence<Schema> = sequenceOf(internalAccountNumber, accountReference)

    object Event : AvroSchemaCatalogueTemplate("${AccountAvroNamespace.NAME}.event") {

        val sendPaymentCommandReceived by lazy { getSchema(name = "SendPaymentCommandReceived", dependencies = setOf(accountReference, CurrencyAmount.avroSchema)) }
        val insufficientBalanceErrorForSendPaymentCommandReceived by lazy { getSchema(name = "SendPaymentCommandReceived_InsufficientBalanceError", dependencies = setOf(sendPaymentCommandReceived)) }
        val accountNotFoundErrorForSendPaymentCommandReceived by lazy { getSchema(name = "SendPaymentCommandReceived_AccountNotFoundError", dependencies = setOf(sendPaymentCommandReceived)) }
        val deposit by lazy { getSchema(name = "Deposit", dependencies = setOf(accountReference, CurrencyAmount.avroSchema)) }
        val outboundPayment by lazy { getSchema(name = "OutboundPayment", dependencies = setOf(accountReference, CurrencyAmount.avroSchema)) }
        val inboundPayment by lazy { getSchema(name = "InboundPayment", dependencies = setOf(accountReference, CurrencyAmount.avroSchema)) }
        val eventData by lazy {
            getSchema(
                name = "AccountEventData", dependencies = setOf(
                    sendPaymentCommandReceived,
                    insufficientBalanceErrorForSendPaymentCommandReceived,
                    accountNotFoundErrorForSendPaymentCommandReceived,
                    deposit,
                    outboundPayment,
                    inboundPayment
                )
            )
        }
        val event by lazy { getSchema(name = "AccountEvent", dependencies = setOf(eventData, EventMetadata.avroSchema)) }

        override val all: Sequence<Schema> = sequenceOf(
            sendPaymentCommandReceived,
            insufficientBalanceErrorForSendPaymentCommandReceived,
            accountNotFoundErrorForSendPaymentCommandReceived,
            deposit,
            outboundPayment,
            inboundPayment,
            eventData,
            event
        )
    }
}