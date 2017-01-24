package com.jcs.suadeome.generators;

public enum EntityType {
  PROFESSIONAL("01"),
  USER("02");

  private String idPrefix;

  EntityType(String idPrefix) {

    this.idPrefix = idPrefix;
  }

  public String getIdPrefix() {
    return idPrefix;
  }
}
