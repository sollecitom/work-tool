package sollecitom.services.modulith_example.shared.account.domain.test.utils

import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.core.utils.nextInt
import sollecitom.services.modulith_example.shared.account.domain.model.reference.InternalAccountNumber

context(random: RandomGenerator)
fun InternalAccountNumber.Companion.create(value: String = generateSequence { random.nextInt(0, 9) }.take(15).joinToString(separator = "")): InternalAccountNumber = InternalAccountNumber(value = value)