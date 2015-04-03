package com.sds.minion.agent.domain;

import java.io.Serializable;

public class AppStatus implements Serializable{

  private static final long serialVersionUID = 1L;
  
  String name;
  String status;

   public AppStatus(String name, String status) {
       this.name = name;
       this.status = status;
   }
}