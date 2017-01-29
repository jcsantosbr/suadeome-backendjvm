package com.jcs.suadeome.main

import com.google.gson.Gson
import com.jcs.suadeome.generators.EntityType
import com.jcs.suadeome.generators.IdGenerator
import org.postgresql.ds.PGPoolingDataSource
import org.skife.jdbi.v2.DBI
import spark.Spark
import spark.Spark.get
import spark.Spark.post
import java.lang.Integer.parseInt
import java.util.*
import java.util.Optional.ofNullable
import java.util.function.Supplier

object App {

    private val toJson = { model: Any -> Gson().toJson(model) }

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
            val h = dbi.open()
            val allProfessionals = h.createQuery("SELECT * FROM professionals ORDER BY name").list()
            h.close()

            allProfessionals
        }, toJson)

        post("/professionals") { request, response ->

            try {

                val h = dbi.open()

                val id = generator.generate(EntityType.PROFESSIONAL)
                val name = request.queryParams("name")
                val phone = request.queryParams("phone")
                val service = request.queryParams("service")

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
                e.printStackTrace()
            }

            response
        }
    }

    private fun configureDB(): DBI {
        val connectionPool = PGPoolingDataSource()
        connectionPool.applicationName = "com/jcs/suadeome"
        connectionPool.serverName = getEnv("OPENSHIFT_POSTGRESQL_DB_HOST", "localhost")
        connectionPool.portNumber = getIntEnv("OPENSHIFT_POSTGRESQL_DB_PORT", 5432)
        connectionPool.databaseName = "com/jcs/suadeome"
        connectionPool.user = getEnv("OPENSHIFT_POSTGRESQL_DB_APP_USER", "com/jcs/suadeome")
        connectionPool.password = getEnv("OPENSHIFT_POSTGRESQL_DB_APP_PASSWORD", "com/jcs/suadeome")
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

