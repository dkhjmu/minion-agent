
package com.sds.minion.agent.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class AgentInfo implements Serializable{

  private static final long serialVersionUID = 1L;
  
  private String url;
   private String name;
   private String path;
   private String cpu;
   private String disk;
   private String memory;
   public String getMemory() {
	return memory;
}
public void setMemory(String memory) {
	this.memory = memory;
}
private List<AppStatus> apps = new LinkedList<>();
   
   public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getPath() {
    return path;
  }
  public void setPath(String path) {
    this.path = path;
  }
  public String getCpu() {
    return cpu;
  }
  public void setCpu(String cpu) {
    this.cpu = cpu;
  }
  public String getDisk() {
    return disk;
  }
  public void setDisk(String disk) {
    this.disk = disk;
  }
  public List<AppStatus> getApps() {
    return apps;
  }
  public void setApps(List<AppStatus> apps) {
    this.apps = apps;
  }

}

   