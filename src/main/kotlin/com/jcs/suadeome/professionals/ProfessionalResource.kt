package com.jcs.suadeome.professionals

import com.google.gson.JsonParser
import com.jcs.suadeome.context.Context
import com.jcs.suadeome.context.RouteFactory
import com.jcs.suadeome.generators.EntityType
import com.jcs.suadeome.generators.IdGenerator
import com.jcs.suadeome.main.App
import com.jcs.suadeome.services.Service
import com.jcs.suadeome.services.ServiceRepository
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import spark.Request
import spark.Response
import spark.Route
import spark.Spark.get
import spark.Spark.post
import java.util.*
import java.util.logging.Logger

object ProfessionalResource {
    val logger: Logger = Logger.getLogger(this.javaClass.name)

    fun routesForProfessionals(routeFactory: RouteFactory) {

        get("/professionals", routeFactory.inContext { request, response, context ->
            val searchedService = request.queryParams("service").orEmpty()
            val services = context.serviceRepository.servicesByPrefix(searchedService)
            val allProfessionals = context.professionalRepository.professionalsByService(services)
            allProfessionals
        }, App.toJson)


        post("/professionals", routeFactory.inContext { request, response, context ->
            val body = request.body()
            val payload = JsonParser().parse(body).asJsonObject

            val id = context.generator.generate(EntityType.PROFESSIONAL)
            val name = payload["name"].asString
            val phone = PhoneNumber(payload["phone"].asString)
            val serviceName = payload["service"].asString

            val service: Service = context.serviceRepository.findOrCreate(serviceName)

            logger.info { "Trying to create request for $name, $phone and $service" }
            val professional = Professional(id, name, phone, service.id)

            context.professionalRepository.createProfessional(professional)

            professional
        }, App.toJson)

    }

}

