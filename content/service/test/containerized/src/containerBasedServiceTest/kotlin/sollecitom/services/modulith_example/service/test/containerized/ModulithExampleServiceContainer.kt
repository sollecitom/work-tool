package sollecitom.services.modulith_example.service.test.containerized

import com.github.dockerjava.api.model.AuthConfig
import org.apache.http.auth.UsernamePasswordCredentials
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PulsarContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.RegistryAuthLocator
import sollecitom.libs.pillar.acme.conventions.CompanyConventions
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.domain.networking.Port
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.lens.core.extensions.utils.environmentName
import sollecitom.libs.swissknife.logging.standard.configuration.StandardLoggingConfiguration.Properties.FORMAT_ENV_VARIABLE
import sollecitom.libs.swissknife.logging.standard.configuration.StandardLoggingConfiguration.Properties.FORMAT_JSON
import sollecitom.libs.swissknife.logging.standard.configuration.StandardLoggingConfiguration.Properties.LOGGING_LEVEL_ENV_VARIABLE
import sollecitom.libs.swissknife.logging.standard.configuration.StandardLoggingConfiguration.Properties.LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE
import sollecitom.libs.swissknife.pulsar.test.utils.networkAlias
import sollecitom.libs.swissknife.test.containers.utils.waitingForSuccessfulReadinessCheck
import sollecitom.libs.swissknife.web.service.domain.WebInterface
import sollecitom.libs.swissknife.web.service.domain.WithWebInterface
import sollecitom.services.modulith_example.configuration.ServiceProperties
import sollecitom.services.modulith_example.service.test.containerized.ModulithExampleServiceContainer.Companion.DEFAULT_PROJECT_PATH
import sollecitom.services.modulith_example.service.test.containerized.ModulithExampleServiceContainer.Companion.IMAGE_NAME


context(ids: UniqueIdGenerator, random: RandomGenerator, conventions: CompanyConventions)
fun newModulithExampleServiceContainer(pulsar: PulsarContainer, servicePort: Int = 8090, healthPort: Int = 8091, instanceNodeName: Name = ids.newId().stringValue.let(::Name)): ModulithExampleServiceContainer {
//fun newModulithExampleServiceContainer(postgres: PostgresDockerContainer, pulsar: PulsarContainer, servicePort: Int = 8090, healthPort: Int = 8091, instanceNodeName: Name = newId().stringValue.let(::Name)): ModulithExampleServiceContainer {

    val loggingEnv = mapOf(LOGGING_LEVEL_ENV_VARIABLE to "INFO", LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE to "org.eclipse.jetty=WARN,org.apache.hc=WARN", FORMAT_ENV_VARIABLE to FORMAT_JSON)
    val httpServerEnv = mapOf(ServiceProperties.servicePort.environmentName to "$servicePort", ServiceProperties.healthPort.environmentName to "$healthPort")
//    val openTelemetryEnv = mapOf(ServiceProperties.openTelemetryEndpointUrl.environmentName to "${openTelemetryCollector.networkAlias}:${GrafanaOpenTelemetryStack.OTLP_GRPC_PORT}")
//    val postgresEnv = mapOf(
//        ServiceProperties.postgresConnectionURI.environmentName to "postgres://${postgres.networkAlias}:${PostgresDockerContainer.PORT}",
//        ServiceProperties.postgresUsername.environmentName to postgres.username,
//        ServiceProperties.postgresPassword.environmentName to postgres.password
//    )
    val pulsarEnv = mapOf(
        ServiceProperties.pulsarBrokerUrl.environmentName to "pulsar://${pulsar.networkAlias}:${PulsarContainer.BROKER_PORT}",
    )
    val serviceEnv = mapOf(
        ServiceProperties.instanceGroupName.environmentName to "modulith-example-service", // this is passed because there might be more than 1 deployment
        ServiceProperties.instanceNodeName.environmentName to instanceNodeName.value,
    )
    val env = (httpServerEnv + pulsarEnv + loggingEnv + serviceEnv)
    val imageTag = imageTag()
    val imageRegistry = imageRegistry()
    val credentials = registryCredentials()
    val projectPath = System.getenv("OCI_PROJECT_PATH") ?: DEFAULT_PROJECT_PATH

    return ModulithExampleServiceContainer(
        servicePort = servicePort,
        healthPort = healthPort,
        tag = imageTag,
        repository = imageRegistry,
        projectPath
    ).withExposedPorts(servicePort, healthPort)
        .withEnv(env)
        .waitingForSuccessfulReadinessCheck(healthPort)
        .withRegistryAuthentication(credentials)
}

private fun ModulithExampleServiceContainer.withRegistryAuthentication(credentials: UsernamePasswordCredentials): ModulithExampleServiceContainer {
    val instance = RegistryAuthLocator.instance()
    val imageName = DockerImageName.parse("$repository/$projectPath/$IMAGE_NAME:$tag")
    instance.lookupAuthConfig(imageName, AuthConfig().withRegistryAddress(repository).withUsername(credentials.userName).withPassword(credentials.password))
    return this
}

private fun registryCredentials(): UsernamePasswordCredentials {
    val username = System.getenv("CI_REGISTRY_USER") ?: ""
    val password = System.getenv("CI_REGISTRY_PASSWORD") ?: ""
    return UsernamePasswordCredentials(username, password)
}

private fun imageTag(): String = System.getenv("OCI_IMAGE_TAG") ?: ModulithExampleServiceContainer.DEFAULT_IMAGE_TAG

private fun imageRegistry(): String = System.getenv("OCI_IMAGE_REGISTRY") ?: ModulithExampleServiceContainer.DEFAULT_IMAGE_REGISTRY

class ModulithExampleServiceContainer(private val servicePort: Int, private val healthPort: Int, val tag: String = DEFAULT_IMAGE_TAG, val repository: String = DEFAULT_IMAGE_REGISTRY, val projectPath: String = DEFAULT_PROJECT_PATH) : GenericContainer<ModulithExampleServiceContainer>(DockerImageName.parse("$repository/$projectPath/$IMAGE_NAME:$tag")), WithWebInterface {

    override val webInterface by lazy { WebInterface.create(host, getMappedPort(servicePort).let(::Port), getMappedPort(healthPort).let(::Port)) }

    companion object {

        const val IMAGE_NAME = "modulith-example-service"
        internal const val DEFAULT_IMAGE_TAG = "latest"
        internal const val DEFAULT_PROJECT_PATH = "sollecitom"
        internal const val DEFAULT_IMAGE_REGISTRY = "ghcr.io"
    }
}