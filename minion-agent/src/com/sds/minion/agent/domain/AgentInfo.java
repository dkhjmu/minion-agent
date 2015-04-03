package com.sds.minion.agent.domain;

import java.util.List;

public class AgentInfo {
  String url;
  String name;
  String path;
  String cpu;
  String disk;
  List<AppInfo> appInfoList;
  
  public List<AppInfo> getAppInfoList() {
    return appInfoList;
  }
  public void setAppInfoList(List<AppInfo> appInfoList) {
    this.appInfoList = appInfoList;
  }
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
  
}
