package com.sds.minion.agent.service.impl;

import java.io.File;

import com.sds.minion.agent.domain.AgentInfo;
import com.sds.minion.agent.service.AppService;

public class AppServiceImpl implements AppService {

  @Override
  public AgentInfo getAgentInfo() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void runApp(String name, String run) {
    // TODO Auto-generated method stub
    
  }
  
  static AppService appService;
  
  //ArrayList<GroupInfo> groups;
 // ArrayList<AppInfo> apps;
  
//public static void main(String[] args) {
//    init();
//}
  
  //test
/*  private AppService(){
      groups = new ArrayList<GroupInfo>();
      apps = new ArrayList<AppInfo>();
      
      String tmpPath=System.getProperty("avalon.root");
      String appRootPath = "D:/ALM/APPS";
      if(tmpPath!=null){
          appRootPath = tmpPath;
      }
      GroupInfo rootGroup = new GroupInfo("root", appRootPath);
      File root = new File(appRootPath);
      if(!root.exists()){
          root.mkdirs();
      }
      File[] list = root.listFiles();
      for(File f : list){
          if(isApp(f)){
              apps.add(loadApp(rootGroup, f));
              continue;
          }
          if(isGroup(f)){
              groups.add(loadGroup(rootGroup, f));
          }
      }
      System.out.println("Loaded Group:"+groups.size());
      System.out.println("Loaded Apps:"+apps.size());
      System.out.println("load ok!");
  }*/
  
/*  private GroupInfo loadGroup(GroupInfo parent, File f) {
      GroupInfo group = new GroupInfo(parent.getName() + "/" + f.getName(), f.getAbsolutePath());
      group.setParent(parent);
      return group;
  }*/

/*  private AppInfo loadApp(GroupInfo group, File f) {
      AppInfo app = new AppInfo(group.getName()+"/"+f.getName(), f.getAbsolutePath());
      app.setParent(group);
      return app;
  }*/

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

  private boolean isGroup(File f) {
      if(f.isDirectory() == false){
          return false;
      }
      File[] list = f.listFiles();
      for(File fChild: list){
          if(fChild.getName().equals("group.properties")){
              return true;
          }
      }
      return false;
  }

/*  public static void init(){
      appService = new AppService();
  }*/
  
/*  public static AppService getInstance(){
      if(appService == null){
          init();
      }
      return appService;
  }*/

/*  public List<AppInfo> allApps() {
      return apps;
  }*/
  

}
