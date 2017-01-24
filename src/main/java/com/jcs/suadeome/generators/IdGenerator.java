package com.jcs.suadeome.generators;

import com.jcs.suadeome.types.Id;

import java.util.function.Supplier;

import static com.jcs.suadeome.types.Id.id;

public class IdGenerator {

  private final Supplier<String> value;

  public IdGenerator(Supplier<String> value) {
    this.value = value;
  }

  public Id generate(EntityType type) {
    return id(type.getIdPrefix() + value.get());
  }
}




