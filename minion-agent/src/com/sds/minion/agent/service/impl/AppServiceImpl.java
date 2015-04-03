package com.sds.minion.agent.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Repository;

import com.sds.minion.agent.domain.AgentInfo;
import com.sds.minion.agent.domain.AppStatus;
import com.sds.minion.agent.run.DbChecker;
import com.sds.minion.agent.run.WebChecker;
import com.sds.minion.agent.service.AppService;

@Repository("appService")
public class AppServiceImpl implements AppService {

  private AgentInfo agentInfo;

  @Override
  public AgentInfo getAgentInfo() {
	String memory = HealInfo.getMemoryUsage();
	String cpu = HealInfo.getCpuUsage();
	String disk = HealInfo.getDiskUsage();
    if(agentInfo != null){
	  agentInfo.setCpu(cpu);
	  agentInfo.setMemory(memory);
      agentInfo.setDisk(disk);
      return agentInfo;
    }
    agentInfo = new AgentInfo();
    agentInfo.setCpu(cpu);
    agentInfo.setMemory(memory);
    agentInfo.setDisk(disk);
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
	  System.out.println("wowowowowwo");
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
    agentInfo.setApps(appStatusList);
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
	 Properties prop=getProperity(f);
	 String type = prop.get("app.health.type").toString();
	 String check = prop.get("app.health.check").toString();
	 String result = "";
	 if(type.equals("db")){
		 String userId = prop.get("app.health.check.userId").toString();
		 String password = prop.get("app.health.check.password").toString();
		 result=DbChecker.check(check, userId, password);
	 }else if(type.equals("web")){
		 result=WebChecker.check(check);
	 }
	 
	AppStatus appStatus = new AppStatus(prop.get("app.name").toString(), result);
	appStatus.setType(type);
    return appStatus;
  }

	private Properties getProperity(File f) {
		Properties prop = new Properties();
	    InputStream inputStream;
		try {
			inputStream = new FileInputStream(f);
			prop.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}
}
