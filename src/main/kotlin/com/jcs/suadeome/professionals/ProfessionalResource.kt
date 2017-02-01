package com.jcs.suadeome.professionals

import com.google.gson.JsonParser
import com.jcs.suadeome.generators.EntityType
import com.jcs.suadeome.generators.IdGenerator
import com.jcs.suadeome.main.App
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import spark.Spark.get
import spark.Spark.post
import java.util.*
import java.util.logging.Logger

object ProfessionalResource {
    val logger: Logger = Logger.getLogger(this.javaClass.name)

    fun routesForProfessionals(dbi: DBI, generator: IdGenerator) {

        get("/professionals", { request, response ->

            try {
                val searchedService = request.queryParams("service").orEmpty()
                val h = dbi.open()
                val repository = ProfessionalRepository(h)

                val allProfessionals = repository.professionalsByService(searchedService)

                h.close()

                allProfessionals
            } catch (e: Exception) {
                logger.severe({ "/professionals -> " + e.message })
                throw e
            }

        }, App.toJson)

        post("/professionals", { request, response ->
            val h: Handle = dbi.open()
            val payload = JsonParser().parse(request.body()).asJsonObject

            val id = generator.generate(EntityType.PROFESSIONAL)
            val name = payload["name"].asString
            val phone = PhoneNumber(payload["phone"].asString)
            val service = Service(payload["service"].asString)

            logger.info { "Trying to create request for $name, $phone and $service" }
            val professional = Professional(id, name, phone, service)

            val query = """
                        INSERT INTO professionals (id, name, phone, service, created_by, created_at)
                        VALUES (:id, :name, :phone, :service, :user, :now)
                    """

            h.createStatement(query)
                    .bind("id", id.value)
                    .bind("name", name)
                    .bind("phone", phone.number)
                    .bind("service", service.id)
                    .bind("user", 1)
                    .bind("now", Date())
                    .execute()

            h.close()

            professional
        }, App.toJson)

        get("/services", { request, response ->
            try {
                val prefix = request.queryParams("prefix").orEmpty()
                val h = dbi.open()
                val repository = ServiceRepository(h)

                val services = repository.servicesByPrefix(prefix)

                h.close()

                services
            } catch (e: Exception) {
                logger.severe({ "/professionals -> " + e.message })
                throw e
            }

        }, App.toJson)
    }
}

