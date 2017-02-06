package com.jcs.suadeome.services

import com.jcs.suadeome.generators.EntityType
import com.jcs.suadeome.generators.IdGenerator
import com.jcs.suadeome.services.Service
import com.jcs.suadeome.types.Id
import org.skife.jdbi.v2.Handle
import java.util.*

class ServiceRepository(val openHandle: Handle, val generator: IdGenerator) {

    fun servicesByPrefix(prefix: String): List<Service> {

        val normalizedPrefix = Service.normalizeName(prefix)

        val rows = openHandle.createQuery("""
                SELECT id, original_name, normalized_name
                FROM services
                WHERE normalized_name like :prefix
                ORDER BY original_name
                """)
                .bind("prefix", normalizedPrefix + "%")
                .list()

        return rows.map { rowToService(it) }
    }

    fun  findOrCreate(serviceName: String): Service {

        val normalizedName = Service.normalizeName(serviceName)

        val result = openHandle.createQuery("""
                SELECT id, original_name, normalized_name
                FROM services
                WHERE normalized_name = :normalizedName
                """)
                .bind("normalizedName", normalizedName)
                .list()

        if (result.isEmpty()) {
            val id =  generator.generate(EntityType.SERVICE)
            val newService = Service.service(id, serviceName, normalizedName)
            val insert = """
             INSERT INTO services (id, normalized_name, original_name, created_by, created_at)
                VALUES (:id, :normalizedName, :originalName, :createdBy, :createdAt)
            """

            openHandle.createStatement(insert)
                    .bind("id", newService.id.value)
                    .bind("normalizedName", newService.normalizedName)
                    .bind("originalName", newService.originalName)
                    .bind("createdBy", 1)
                    .bind("createdAt", Date())
                    .execute()
            return newService
        } else {
            return rowToService(result.first())
        }
    }

    fun rowToService(row: Map<String, Any>): Service {
        val id = Id(row["id"].toString())
        val originalName = row["original_name"].toString()
        val normalizedName = row["normalized_name"].toString()

        return Service.service(id, originalName,normalizedName)
    }

}
