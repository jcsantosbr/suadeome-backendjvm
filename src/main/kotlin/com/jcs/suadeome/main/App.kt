package com.jcs.suadeome.main

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jcs.suadeome.generators.EntityType
import com.jcs.suadeome.generators.IdGenerator
import com.jcs.suadeome.professionals.ProfessionalRepository
import com.jcs.suadeome.professionals.ServiceRepository
import org.postgresql.ds.PGPoolingDataSource
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import spark.Spark
import spark.Spark.get
import spark.Spark.post
import java.lang.Integer.parseInt
import java.util.*
import java.util.Optional.ofNullable
import java.util.function.Supplier
import java.util.logging.Logger

object App {

    val logger: Logger = Logger.getLogger(this.javaClass.name)

    
    private val toJson = { model: Any -> createGson().toJson(model) }

    private fun createGson() : Gson {
        val builder = GsonBuilder()
        return builder.create()
    }

    @JvmStatic fun main(args: Array<String>) {
        configureSpark()

        val dbi = configureDB()

        defineRoutes(dbi)
    }

    private fun configureSpark() {
        val ipAddress = getEnv("OPENSHIFT_DIY_IP", "localhost")
        val port = getIntEnv("OPENSHIFT_DIY_PORT", 4567)

        Spark.ipAddress(ipAddress)
        Spark.port(port)
    }

    private fun defineRoutes(dbi: DBI) {

        val generator = IdGenerator(Supplier { "" + System.nanoTime() })

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

        }, toJson)

        post("/professionals") { request, response ->
            try {
                val h: Handle = dbi.open()
                val id = generator.generate(EntityType.PROFESSIONAL)
                val name = request.queryParams("name").orEmpty()
                val phone = request.queryParams("phone").orEmpty()
                val service = request.queryParams("service").orEmpty()

                h.createStatement("INSERT INTO professionals (id, name, phone, service, created_by, created_at) " + "VALUES (:id, :name, :phone, :service, :user, :now)")
                        .bind("id", id.getValue())
                        .bind("name", name)
                        .bind("phone", phone)
                        .bind("service", service)
                        .bind("user", 1)
                        .bind("now", Date())
                        .execute()

                h.close()
            } catch (e: Exception) {
                logger.severe({ "/professionals -> " + e.message })
                e.printStackTrace()
            }

            response
        }

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

        }, toJson)
    }

    private fun configureDB(): DBI {
        val connectionPool = PGPoolingDataSource()
        connectionPool.applicationName = "suadeome"
        connectionPool.serverName = getEnv("OPENSHIFT_POSTGRESQL_DB_HOST", "localhost")
        connectionPool.portNumber = getIntEnv("OPENSHIFT_POSTGRESQL_DB_PORT", 5432)
        connectionPool.databaseName = "suadeome"
        connectionPool.user = getEnv("OPENSHIFT_POSTGRESQL_DB_APP_USER", "suadeome")
        connectionPool.password = getEnv("OPENSHIFT_POSTGRESQL_DB_APP_PASSWORD", "suadeome")
        connectionPool.maxConnections = 10

        return DBI(connectionPool)
    }

    private fun getIntEnv(property: String, defaultValue: Int): Int {
        return parseInt(ofNullable(System.getenv(property)).orElse(Integer.toString(defaultValue)))
    }

    private fun getEnv(property: String, defaultValue: String): String {
        return ofNullable(System.getenv(property)).orElse(defaultValue)
    }

}

