@file:Suppress("UnstableApiUsage")

rootProject.name = "work-tool"

fun resource(vararg pathSegments: String) = subProject(rootFolder = "resources", pathSegments = pathSegments)

fun library(vararg pathSegments: String) = subProject(rootFolder = "libs", pathSegments = pathSegments)

fun content(vararg pathSegments: String) = subProject(rootFolder = "content", pathSegments = pathSegments)

fun example(vararg pathSegments: String) = subProject(rootFolder = "example", pathSegments = pathSegments, excludeRootFolderFromGroupName = false)

fun subProject(rootFolder: String, vararg pathSegments: String, excludeRootFolderFromGroupName: Boolean = true) {

    val projectName = pathSegments.last()
    val path = listOf(rootFolder) + pathSegments.dropLast(1)
    val group = if (excludeRootFolderFromGroupName) path.minus(rootFolder).joinToString(separator = "-") else path.joinToString(separator = "-", prefix = "${rootProject.name}-")
    val directory = path.joinToString(separator = "/", prefix = "./")
    val fullProjectName = "${if (group.isEmpty()) "" else "$group-"}$projectName"

    include(fullProjectName)
    project(":$fullProjectName").projectDir = mkdir("$directory/$projectName")
}

fun includeProject(name: String) {

    apply("$name/settings.gradle.kts")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

content("configuration")
content("contracts", "http")
content("migrator", "postgres", "domain")
content("migrator", "postgres", "starter")
content("service", "starter")
content("service", "test", "specification")
content("service", "test", "in-process")
content("service", "test", "containerized")
content("shared", "serialization", "avro")
content("shared", "serialization", "json")
content("shared", "account", "domain", "model")
content("shared", "account", "domain", "test", "utils")
content("shared", "account", "serialization", "avro")
content("shared", "account", "messaging", "adapters")
content("shared", "account", "messaging", "converter")
content("shared", "messaging", "topics")
content("shared", "nats", "adapter")
content("modules", "deposit-event-endpoint", "domain", "model")
content("modules", "deposit-event-endpoint", "domain", "test", "utils")
content("modules", "deposit-event-endpoint", "application", "model")
content("modules", "deposit-event-endpoint", "adapters", "driving", "http", "contract", "tests")
content("modules", "deposit-event-endpoint", "adapters", "driving", "http", "serialization", "json")
content("modules", "deposit-event-endpoint", "adapters", "driving", "http", "endpoint")
content("modules", "deposit-event-endpoint", "adapters", "driven", "pulsar")
content("modules", "deposit-event-endpoint", "module", "implementation")
content("modules", "deposit-event-endpoint", "module", "test", "specification")
content("modules", "payment-command-endpoint", "domain", "model")
content("modules", "payment-command-endpoint", "domain", "test", "utils")
content("modules", "payment-command-endpoint", "application", "model")
content("modules", "payment-command-endpoint", "adapters", "driving", "http", "contract", "tests")
content("modules", "payment-command-endpoint", "adapters", "driving", "http", "serialization", "json")
content("modules", "payment-command-endpoint", "adapters", "driving", "http", "endpoint")
content("modules", "payment-command-endpoint", "adapters", "driven", "pulsar")
content("modules", "payment-command-endpoint", "adapters", "driven", "nats")
content("modules", "payment-command-endpoint", "module", "implementation")
content("modules", "payment-command-endpoint", "module", "test", "specification")
content("modules", "account-event-processor", "domain", "model")
content("modules", "account-event-processor", "domain", "test", "utils")
content("modules", "account-event-processor", "application", "model")
content("modules", "account-event-processor", "adapters", "driving", "pulsar")
content("modules", "account-event-processor", "adapters", "driven", "pulsar")
content("modules", "account-event-processor", "module", "implementation")
content("modules", "account-event-processor", "module", "test", "specification")
content("configuration")
content("service", "starter")
content("service", "test", "specification")