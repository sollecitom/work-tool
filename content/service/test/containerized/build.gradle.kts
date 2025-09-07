import sollecitom.plugins.conventions.task.container.test.ContainerBasedServiceTestConvention
import sollecitom.plugins.conventions.task.container.test.containerBasedServiceTestImplementation

apply<ContainerBasedServiceTestConvention>()
configure<ContainerBasedServiceTestConvention.Extension> {
    starterModuleName = projects.serviceStarter.name
}

dependencies {
    containerBasedServiceTestImplementation(projects.serviceTestSpecification)
    containerBasedServiceTestImplementation(projects.serviceStarter)
    containerBasedServiceTestImplementation(projects.configuration)
    containerBasedServiceTestImplementation(libs.swissknife.test.containers.utils)
    containerBasedServiceTestImplementation(libs.swissknife.core.test.utils)
    containerBasedServiceTestImplementation(libs.pillar.acme.business.domain)
    containerBasedServiceTestImplementation(libs.pillar.messaging.conventions)
    containerBasedServiceTestImplementation(libs.pillar.web.api.test.utils)
}