package com.jcs.suadeome.context

import com.jcs.suadeome.generators.IdGenerator
import com.jcs.suadeome.professionals.ProfessionalRepository
import com.jcs.suadeome.services.ServiceRepository

data class Context(val generator: IdGenerator,
                   val serviceRepository: ServiceRepository,
                   val professionalRepository: ProfessionalRepository)

