package com.serialization;

import java.io.Serializable;

public class Person implements Serializable {
  private static final long serialVersionUID = 1L;
  private String fullname;

  public Person(String fullname) {
    this.fullname = fullname;
  }

  public String getFullName() {
    return this.fullname;
  }

  public void setFullName(String fullName) {
    this.fullname = fullName;
  }
}