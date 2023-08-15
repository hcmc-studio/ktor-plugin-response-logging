package studio.hcmc.ktor.plugin

import io.ktor.server.application.*
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

class ResponseLoggingConfiguration {
    var level = Level.TRACE
    var logger = LoggerFactory.getLogger("ResponseLogger")
    var format: suspend (call: ApplicationCall, body: Any) -> String = { _, _ -> throw NotImplementedError() }
}

val ResponseLogging = createApplicationPlugin("ResponseLogging", ::ResponseLoggingConfiguration) {
    val print: (msg: String) -> Unit = when (pluginConfig.level) {
        Level.ERROR -> pluginConfig.logger::error
        Level.WARN -> pluginConfig.logger::warn
        Level.INFO -> pluginConfig.logger::info
        Level.DEBUG -> pluginConfig.logger::debug
        Level.TRACE -> pluginConfig.logger::trace
    }

    onCallRespond { call, body ->
        print(pluginConfig.format(call, body))
    }
}