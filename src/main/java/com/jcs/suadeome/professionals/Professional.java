package com.jcs.suadeome.professionals;

import com.jcs.suadeome.types.Id;

public class Professional {

  private final Id id;
  private final String name;
  private final String phone;
  private final String service;

  public Professional(Id id, String name, String phone, String service) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.service = service;
  }

  public Id getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPhone() {
    return phone;
  }

  public String getService() {
    return service;
  }
}
