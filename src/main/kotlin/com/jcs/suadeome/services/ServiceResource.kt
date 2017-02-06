package com.jcs.suadeome.services

import com.jcs.suadeome.generators.IdGenerator
import com.jcs.suadeome.main.App
import org.skife.jdbi.v2.DBI
import spark.Spark
import java.util.logging.Logger

object ServiceResource {
    val logger: Logger = Logger.getLogger(this.javaClass.name)

    fun routesForResource(dbi: DBI, generator: IdGenerator) {

        Spark.get("/services", { request, response ->
            try {
                val prefix = request.queryParams("prefix").orEmpty()
                val h = dbi.open()
                val repository = ServiceRepository(h, generator)

                val services = repository.servicesByPrefix(prefix)

                h.close()

                services
            } catch (e: Exception) {
                logger.severe({ "/services -> " + e.message })
                throw e
            }

        }, App.toJson)
    }
}

