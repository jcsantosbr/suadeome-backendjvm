package com.jcs.suadeome.services

import com.jcs.suadeome.context.Context
import com.jcs.suadeome.context.RouteFactory
import com.jcs.suadeome.context.SimpleRequest
import com.jcs.suadeome.main.App
import spark.Spark.get

object ServiceResource {

    fun routesForResource(routeFactory: RouteFactory) {
        get("/services", routeFactory.inContext { request, context ->
            servicesByPrefix(request, context)
        }, App.toJson)
    }

    fun servicesByPrefix(request: SimpleRequest, context: Context): List<Service> {
        val prefix = request.stringParam("prefix")
        return context.serviceRepository.servicesByPrefix(prefix)
    }
}

