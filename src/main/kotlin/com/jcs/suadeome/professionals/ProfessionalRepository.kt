package com.jcs.suadeome.professionals

import com.jcs.suadeome.services.Service
import com.jcs.suadeome.types.Id
import org.skife.jdbi.v2.Handle
import java.util.*

class ProfessionalRepository(val openHandle: Handle) {

    fun professionalsByService(services: List<Service>): List<Professional> {

        if (services.isEmpty()) return Collections.emptyList()

        val placeHolders = services.mapIndexed { i, service -> ":id$i" }.joinToString(",")
        val whereClause = if (placeHolders.isBlank()) "" else "WHERE service_id IN ( $placeHolders )"

        val query = openHandle.createQuery("""
                SELECT id, name, phone, service_id
                FROM professionals
                $whereClause
                ORDER BY name
                """)

        services.forEachIndexed { i, service ->
            query.bind("id$i", service.id.value)
        }

        val rows = query.list()

        val professionals = rows.map { rowToProfessional(it) }

        return professionals
    }

    private fun rowToProfessional(row: Map<String, Any>): Professional {
        val id = Id(row["id"].toString())
        val name = row["name"].toString()
        val phone = PhoneNumber(row["phone"].toString())
        val serviceId = Id(row["service_id"].toString())

        return Professional(id, name, phone, serviceId)
    }

    fun createProfessional(professional: Professional) {
        val query = """
            INSERT INTO professionals (id, name, phone, service_id, created_by, created_at)
            VALUES (:id, :name, :phone, :serviceId, :user, :now)
        """

        openHandle.createStatement(query)
                .bind("id", professional.id.value)
                .bind("name", professional.name)
                .bind("phone", professional.phone.number)
                .bind("serviceId", professional.serviceId.value)
                .bind("user", 1)
                .bind("now", Date())
                .execute()
    }

}

