package com.jcs.suadeome.services

import com.jcs.suadeome.context.RouteFactory
import com.jcs.suadeome.main.App
import spark.Spark

object ServiceResource {

    fun routesForResource(routeFactory: RouteFactory) {
        Spark.get("/services", routeFactory.inContext { request, context ->
            val prefix = request.stringParam("prefix")
            context.serviceRepository.servicesByPrefix(prefix)
        }, App.toJson)
    }
}

