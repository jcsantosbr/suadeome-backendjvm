package com.jcs.suadeome.main

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jcs.suadeome.generators.IdGenerator
import com.jcs.suadeome.professionals.Professional
import com.jcs.suadeome.professionals.ProfessionalJsonSerializer
import com.jcs.suadeome.professionals.ProfessionalResource
import com.jcs.suadeome.services.Service
import com.jcs.suadeome.services.ServiceResource
import com.jcs.suadeome.types.EntityConstructionFailed
import org.flywaydb.core.Flyway
import org.postgresql.ds.PGPoolingDataSource
import org.skife.jdbi.v2.DBI
import spark.Filter
import spark.Spark
import spark.Spark.*
import java.lang.Integer.parseInt
import java.util.Optional.ofNullable
import java.util.function.Supplier
import java.util.logging.Logger

object App {

    private val logger: Logger = Logger.getLogger(this.javaClass.name)

    val toJson = { model: Any -> createGson().toJson(model) }

    private fun createGson(): Gson {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Professional::class.java, ProfessionalJsonSerializer())
        builder.registerTypeAdapter(Service::class.java, Service.ServiceJsonSerializer())
        return builder.create()
    }

    @JvmStatic fun main(args: Array<String>) {

        upgradeDB()

        configureSpark()

        val dbi = configureDB()

        defineRoutes(dbi)
    }

    private fun upgradeDB() {
        val flyway = Flyway()
        flyway.setDataSource(DbConfig.url(), DbConfig.username(), DbConfig.password())
        flyway.migrate()
    }

    private fun configureSpark() {
        val ipAddress = getEnv("OPENSHIFT_DIY_IP", "localhost")
        val port = getIntEnv("OPENSHIFT_DIY_PORT", 4567)

        Spark.ipAddress(ipAddress)
        Spark.port(port)
        Spark.threadPool(14);
    }

    private fun defineRoutes(dbi: DBI) {

        val generator = IdGenerator(Supplier { "" + System.nanoTime() })

        enableCors()

        get("/", { request, response ->
            //TODO: list of endpoints, needs to learn how to fetch from Spark
            listOf("professionals", "services")
        }, toJson)

        ProfessionalResource.routesForProfessionals(dbi, generator)
        ServiceResource.routesForResource(dbi, generator)

        exception(EntityConstructionFailed::class.java, { exception, request, response ->
            response.status(400)
            response.body(exception.message)
        })

        exception(Exception::class.java, { exception, request, response ->
            response.status(500)
            response.body("Something bad happened! Sorry")

            exception.printStackTrace()
            logger.severe { exception.message }
        })

    }

    private fun enableCors() {
        options("/*", { request, response ->

            val accessControlRequestHeaders = request.headers("Access-Control-Request-Headers")

            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            val accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            response;
        })

        before(Filter { request, response ->
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "POST");
            response.header("Access-Control-Request-Method", "GET");
            response.header("Access-Control-Request-Method", "OPTIONS");
            response.type("application/json");
        })
    }

    private fun configureDB(): DBI {
        val connectionPool = PGPoolingDataSource()

        connectionPool.applicationName = "suadeome"
        connectionPool.url = DbConfig.url()
        connectionPool.user = DbConfig.username()
        connectionPool.password = DbConfig.password()
        connectionPool.maxConnections = 10

        return DBI(connectionPool)
    }

    private fun getIntEnv(property: String, defaultValue: Int): Int {
        return parseInt(ofNullable(System.getenv(property)).orElse(Integer.toString(defaultValue)))
    }

    private fun getEnv(property: String, defaultValue: String): String {
        return ofNullable(System.getenv(property)).orElse(defaultValue)
    }

    object DbConfig {
        fun url(): String {
            val host = getEnv("OPENSHIFT_POSTGRESQL_DB_HOST", "localhost")
            val port = getEnv("OPENSHIFT_POSTGRESQL_DB_PORT", "5432")

            return "jdbc:postgresql://$host:$port/suadeome"
        }

        fun username(): String = getEnv("OPENSHIFT_POSTGRESQL_DB_APP_USER", "suadeome")
        fun password(): String = getEnv("OPENSHIFT_POSTGRESQL_DB_APP_PASSWORD", "suadeome")
    }

}

