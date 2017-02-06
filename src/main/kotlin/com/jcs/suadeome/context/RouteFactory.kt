package com.jcs.suadeome.context

import com.jcs.suadeome.generators.IdGenerator
import com.jcs.suadeome.professionals.ProfessionalRepository
import com.jcs.suadeome.services.ServiceRepository
import org.skife.jdbi.v2.DBI
import spark.Request
import spark.Response
import spark.Route

class RouteFactory(val dbi: DBI) {

    fun inContext(function: (request: Request, response: Response, context: Context) -> Any): Route {
        return Route { request: Request, response: Response ->

            val openHandle = dbi.open()

            val context = Context(
                    generator = IdGenerator.default(),
                    serviceRepository = ServiceRepository(openHandle, IdGenerator.default()),
                    professionalRepository = ProfessionalRepository(openHandle)
            )

            try {

                openHandle.begin()

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



