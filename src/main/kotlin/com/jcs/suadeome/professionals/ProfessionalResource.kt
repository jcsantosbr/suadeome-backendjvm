package com.jcs.suadeome.professionals

import com.google.gson.JsonParser
import com.jcs.suadeome.generators.EntityType
import com.jcs.suadeome.generators.IdGenerator
import com.jcs.suadeome.main.App
import com.jcs.suadeome.services.Service
import com.jcs.suadeome.services.ServiceRepository
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
                val serviceRepository = ServiceRepository(h, generator)

                val services = serviceRepository.servicesByPrefix(searchedService)
                val allProfessionals = repository.professionalsByService(services)

                h.close()

                allProfessionals
            } catch (e: Exception) {
                logger.severe({ "/professionals -> " + e.message })
                throw e
            }

        }, App.toJson)

        post("/professionals", { request, response ->
            val h: Handle = dbi.open()
            val body = request.body()
            val payload = JsonParser().parse(body).asJsonObject

            val serviceRepository = ServiceRepository(h, generator)

            val id = generator.generate(EntityType.PROFESSIONAL)
            val name = payload["name"].asString
            val phone = PhoneNumber(payload["phone"].asString)
            val serviceName = payload["service"].asString

            val service: Service = serviceRepository.findOrCreate(serviceName)

            logger.info { "Trying to create request for $name, $phone and $service" }
            val professional = Professional(id, name, phone, service.id)

            val query = """
                        INSERT INTO professionals (id, name, phone, service_id, created_by, created_at)
                        VALUES (:id, :name, :phone, :serviceId, :user, :now)
                    """

            h.createStatement(query)
                    .bind("id", id.value)
                    .bind("name", name)
                    .bind("phone", phone.number)
                    .bind("serviceId", service.id.value)
                    .bind("user", 1)
                    .bind("now", Date())
                    .execute()

            h.close()

            professional
        }, App.toJson)

    }
}

