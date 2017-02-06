package com.jcs.suadeome.generators

import com.jcs.suadeome.types.Id

import java.util.function.Supplier

class IdGenerator(private val value: Supplier<String>) {

    fun generate(type: EntityType): Id {
        return Id(type.idPrefix + value.get())
    }

    companion object {
        val DEFAULT_GENERATOR = Supplier { "" + System.nanoTime() }
    }

}




