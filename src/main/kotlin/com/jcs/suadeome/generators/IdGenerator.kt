package com.jcs.suadeome.generators

import com.jcs.suadeome.types.Id

import java.util.function.Supplier

class IdGenerator(private val value: Supplier<String>) {

    fun generate(type: EntityType): Id {
        return Id.id(type.idPrefix + value.get())
    }
}




