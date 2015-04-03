package com.sds.minion.agent.service.impl;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.sds.minion.agent.domain.AgentInfo;
import com.sds.minion.agent.domain.AppStatus;
import com.sds.minion.agent.service.AppService;

public class AppServiceImpl implements AppService {

  private AgentInfo agentInfo;

  @Override
  public AgentInfo getAgentInfo() {
    if(agentInfo != null){
      return agentInfo;
    }
    agentInfo = new AgentInfo();
    agentInfo.setCpu("20%");
    agentInfo.setDisk("100");
    agentInfo.setName("agent");
    agentInfo.setPath("path");
    agentInfo.setUrl("url");
    return agentInfo;
  }

  @Override
  public void runApp(String name, String run) {
    // TODO Auto-generated method stub
  }

  public AppServiceImpl(){
    reload();
  }

  public void reload(){
    agentInfo = getAgentInfo();
    List<AppStatus> appStatusList = new LinkedList<AppStatus>();

    String tmpPath=System.getProperty("avalon.root");
    String appRootPath = "C:/apps/alm/minion";
    if(tmpPath!=null){
      appRootPath = tmpPath;
    }
    File root = new File(appRootPath);
    if(!root.exists()){
      root.mkdirs();
    }
    File[] list = root.listFiles();
    for(File f : list){
      if(isApp(f)){
        appStatusList.add(loadApp(f));
        continue;
      }
    }
    System.out.println("load ok!");
  }

  private boolean isApp(File f) {
    if(f.isDirectory() == false){
      return false;
    }
    File[] list = f.listFiles();
    for(File fChild: list){
      if(fChild.getName().equals("app.properties")){
        return true;
      }
    }
    return false;
  }

  private AppStatus loadApp(File f) {
    // app 상태 check, file 정보 읽어오기
    AppStatus appStatus = new AppStatus("name", "DEAD");
    return appStatus;
  }
}
