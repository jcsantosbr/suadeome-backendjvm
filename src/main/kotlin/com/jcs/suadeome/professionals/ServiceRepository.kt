package com.jcs.suadeome.professionals

import org.skife.jdbi.v2.Handle

class ServiceRepository(val openHandle: Handle) {

    fun servicesByPrefix(prefix: String): List<String> {
        val rows = openHandle.createQuery("""
                SELECT DISTINCT service
                FROM professionals
                WHERE service like :prefix
                ORDER BY service
                """)
                .bind("prefix", prefix + "%")
                .list()

        val services = rows.map { it["service"].toString() }
        return services
    }

}
