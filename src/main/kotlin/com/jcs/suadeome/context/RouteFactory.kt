package com.jcs.suadeome.context

import com.google.gson.JsonParser
import com.jcs.suadeome.generators.IdGenerator
import com.jcs.suadeome.professionals.ProfessionalRepository
import com.jcs.suadeome.services.ServiceRepository
import org.skife.jdbi.v2.DBI
import spark.Request
import spark.Response
import spark.Route

class RouteFactory(val dbi: DBI) {

    fun inContext(function: (request: SimpleRequest, context: Context) -> Any): Route {
        return Route { request: Request, response: Response ->
            val handle = dbi.open()

            val context = Context(
                    generator = IdGenerator.default(),
                    serviceRepository = ServiceRepository(handle, IdGenerator.default()),
                    professionalRepository = ProfessionalRepository(handle)
            )

            val simpleRequest = SparkSimpleRequest(request)

            try {
                handle.begin()

                val result = function(simpleRequest, context)

                handle.commit()

                result
            } catch (e: Exception) {
                handle.rollback()
                throw e
            } finally {
                handle.close()
            }
        }
    }

}

interface SimpleRequest {
    fun stringParam(name:String):String
    fun intParam(name:String):Int
}

private class SparkSimpleRequest(val request: Request) : SimpleRequest {

    var readParam: (param:String) -> String

    init {
        val body = request.body()
        try {
            val json = JsonParser().parse(body).asJsonObject
            readParam = { p -> json[p].asString  }
        } catch (e:Exception) {
            readParam = { p -> request.queryParams(p).orEmpty()  }
        }
    }

    override fun stringParam(name: String): String = readParam(name)

    override fun intParam(name: String): Int = readParam(name).toInt()
}


