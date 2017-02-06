package com.jcs.suadeome.context

import com.jcs.suadeome.generators.IdGenerator
import com.jcs.suadeome.professionals.ProfessionalRepository
import com.jcs.suadeome.services.ServiceRepository
import org.skife.jdbi.v2.DBI
import spark.Request
import spark.Response
import spark.Route

class RouteFactory(val dbi: DBI) {

    fun inContext(function: (request: Request, response: Response, context: Context) -> Any ): Route {
        return Route { request: Request, response: Response ->
            val generator = IdGenerator(IdGenerator.DEFAULT_GENERATOR)

            val openHandle = dbi.open()
            openHandle.begin()

            try {

                val serviceRepository = ServiceRepository(openHandle, generator)

                val professionalRepository = ProfessionalRepository(openHandle)

                val context = Context(generator, serviceRepository, professionalRepository)

                val result = function(request, response, context)

                openHandle.commit()

                result
            } catch (e: Exception) {
                openHandle.rollback()
                throw e
            } finally {
                openHandle.close()
            }
        }
    }



}



