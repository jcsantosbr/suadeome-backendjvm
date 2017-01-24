package com.jcs.suadeome.types;

import java.math.BigDecimal;

public final class Id {
  private final String value;

  private Id(String value) {
    this.value = value;
  }

  public static Id id(String value) {
    return new Id(value);
  }

  public BigDecimal getValue() {
    return new BigDecimal(value);
  }

}
