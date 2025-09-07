package sollecitom.services.modulith_example.service.starter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import sollecitom.libs.swissknife.configuration.utils.StandardEnvironment
import sollecitom.libs.swissknife.core.domain.lifecycle.stopOnJvmShutdown
import sollecitom.libs.swissknife.kotlin.extensions.concurrency.VirtualThreads
import sollecitom.libs.swissknife.logging.standard.configuration.configureLogging

fun main(args: Array<String>) = runBlocking(context = Dispatchers.VirtualThreads) { // no suspend coroutineScope {} or it won't work with Docker
    val environment = StandardEnvironment()
    configureLogging(environment)
    val service = Service(environment).stopOnJvmShutdown()
    service.start()
}