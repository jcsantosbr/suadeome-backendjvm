package com.jcs.suadeome.types

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.math.BigDecimal

class Id private constructor(private val value: String) {

    fun getValue(): BigDecimal {
        return BigDecimal(value)
    }

    override fun toString(): String {
        return value
    }

    companion object {

        fun id(value: String): Id {
            return Id(value)
        }
    }

    class IdToJson : JsonSerializer<Id> {
        override fun serialize(src: Id?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return Gson().toJsonTree(src)
        }
    }

}
