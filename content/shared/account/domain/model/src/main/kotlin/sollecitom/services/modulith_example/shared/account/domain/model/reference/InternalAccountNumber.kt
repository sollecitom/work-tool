package sollecitom.services.modulith_example.shared.account.domain.model.reference

@JvmInline
value class InternalAccountNumber(override val value: String) : AccountReference {

    init {
        require(value.isNotBlank())
    }

    companion object
}