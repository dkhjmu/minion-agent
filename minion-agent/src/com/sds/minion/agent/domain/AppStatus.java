package com.sds.minion.agent.domain;

import java.io.Serializable;

public class AppStatus implements Serializable{
  
  public static final String DEAD = "DEAD";
  public static final String LIVED = "LIVE";

  private static final long serialVersionUID = 1L;
  
  String name;
  String status;
  String type;

   public String getType() {
	return type;
  }

  public void setType(String type) {
	this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public AppStatus(String name, String status) {
       this.name = name;
       this.status = status;
   }
}