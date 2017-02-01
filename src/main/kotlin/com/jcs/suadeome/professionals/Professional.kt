package com.jcs.suadeome.professionals

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.jcs.suadeome.types.Id
import java.lang.reflect.Type

data class Professional(val id: Id, val name: String, val phone: PhoneNumber, val service: Service)

class ProfessionalJsonSerializer : JsonSerializer<Professional> {
    override fun serialize(src: Professional, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject();
        obj.addProperty("type", src.javaClass.simpleName)
        obj.addProperty("id", src.id.value)
        obj.addProperty("name", src.name)
        obj.addProperty("phone", src.phone.number)
        obj.addProperty("service", src.service.name)
        return obj
    }
}
