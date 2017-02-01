package com.jcs.suadeome.types

import com.jcs.suadeome.generators.EntityType

class EntityConstructionFailed(val entityType: EntityType, val invalidValue: String) :
        Exception("Invalid value [$invalidValue] for ${entityType.toString().capitalize()}")

