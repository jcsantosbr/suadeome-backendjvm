package com.jcs.suadeome.professionals

import com.jcs.suadeome.types.Id
import org.skife.jdbi.v2.Handle

class ProfessionalRepository(val openHandle: Handle) {

    fun professionalsByService(service: String): List<Professional> {
        val rows = openHandle.createQuery("""
                SELECT id, name, phone, service
                FROM professionals
                WHERE service = :service
                ORDER BY name
                """)
                .bind("service", service)
                .list()

        val professionals = rows.map { rowToProfessional(it) }

        return professionals
    }

    fun rowToProfessional(row: Map<String, Any>): Professional {
        val id = Id(row["id"].toString())
        val name = row["name"].toString()
        val phone = PhoneNumber(row["phone"].toString())
        val service = Service(row["service"].toString())

        return Professional(id, name, phone, service)
    }

}

