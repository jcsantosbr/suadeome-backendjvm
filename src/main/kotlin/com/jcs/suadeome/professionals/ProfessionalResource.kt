package com.jcs.suadeome.professionals

import com.jcs.suadeome.context.Context
import com.jcs.suadeome.context.RouteFactory
import com.jcs.suadeome.context.SimpleRequest
import com.jcs.suadeome.generators.EntityType
import com.jcs.suadeome.main.App
import com.jcs.suadeome.services.Service
import spark.Spark.get
import spark.Spark.post
import java.util.logging.Logger

object ProfessionalResource {
    val logger: Logger = Logger.getLogger(this.javaClass.name)

    fun routesForProfessionals(routeFactory: RouteFactory) {

        get("/professionals", routeFactory.inContext({ request, context ->
            listProfessionals(request, context)
        }), App.toJson)

        post("/professionals", routeFactory.inContext { request, context ->
            createProfessional(request, context)
        }, App.toJson)

    }

    fun listProfessionals(request: SimpleRequest, context: Context): List<Professional> {
        val searchedService = request.stringParam("service")
        val services = context.serviceRepository.servicesByPrefix(searchedService)
        val allProfessionals = context.professionalRepository.professionalsByService(services)
        return allProfessionals
    }

    fun createProfessional(request: SimpleRequest, context: Context): Professional {
        val id = context.generator.generate(EntityType.PROFESSIONAL)
        val name = request.stringParam("name")
        val phone = PhoneNumber(request.stringParam("phone"))
        val serviceName = request.stringParam("service")

        val service: Service = context.serviceRepository.findOrCreate(serviceName)

        val professional = Professional(id, name, phone, service.id)

        context.professionalRepository.createProfessional(professional)

        return professional
    }

}

