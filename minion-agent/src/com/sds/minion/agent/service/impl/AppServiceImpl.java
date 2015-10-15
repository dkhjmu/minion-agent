package com.sds.minion.agent.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sds.common.FileLogger;
import com.sds.common.LogFactory;
import com.sds.common.PropertyUtils;
import com.sds.minion.agent.domain.AgentInfo;
import com.sds.minion.agent.domain.AppStatus;
import com.sds.minion.agent.run.ALMChecker;
import com.sds.minion.agent.run.DbChecker;
import com.sds.minion.agent.run.ProcessRunner;
import com.sds.minion.agent.run.WebChecker;
import com.sds.minion.agent.service.AppService;

@Repository("appService")
public class AppServiceImpl implements AppService {

  private AgentInfo agentInfo;
  private Date timestamp;
  private Properties agentProp;
  private String appRootPath;
  private ObjectMapper objectMapper = new ObjectMapper();
  private Map<String, Properties> appProp = new HashMap<String, Properties>();
  public static int PUSH_INTERVAL = 180;

  static {
    try {
      PUSH_INTERVAL = Integer.parseInt(PropertyUtils.getProperty("minion.push.interval"));
    } catch (Exception e) {
      PUSH_INTERVAL = 180;
    }
  }

  @Override
  public AgentInfo getAgentInfo() {
    String memory = HealInfo.getMemoryUsage();
    String cpu = HealInfo.getCpuUsage();
    String disk = HealInfo.getDiskUsage();
    if (agentInfo != null) {
      reload();
      agentInfo.setCpu(cpu);
      agentInfo.setMemory(memory);
      agentInfo.setDisk(disk);
      return agentInfo;
    }

    String agentRoot = System.getProperty("agent.home");
    if (agentRoot == null) {
      agentRoot = new File("").getAbsolutePath();
    }
    appRootPath = agentRoot + "/prop";
    agentProp = getProperity(new File(appRootPath + "/agent.properties"));

    agentInfo = new AgentInfo();
    agentInfo.setCpu(cpu);
    agentInfo.setMemory(memory);
    agentInfo.setDisk(disk);
    agentInfo.setPath(agentRoot);
    agentInfo.setName(agentProp.getProperty("agent.name"));
    agentInfo.setUrl(agentProp.getProperty("agent.url"));
    return agentInfo;
  }

  @Override
  public String runApp(String name, String run) {
    // run : start, stop, restart
    Properties prop = appProp.get(name);
    String cmd = "";
    String dir = prop.getProperty("app.run.dir", "./");
    if (run.equals("stop")) {
      if (ProcessRunner.isWindows()) {
        cmd = prop.get("app.run.window.stop").toString();
      } else {
        cmd = prop.get("app.run.stop").toString();
      }
    } else if (run.equals("start")) {
      if (ProcessRunner.isWindows()) {
        cmd = prop.get("app.run.window.start").toString();
      } else {
        cmd = prop.get("app.run.start").toString();
      }
    } else if (run.equals("restart")) {
      if (ProcessRunner.isWindows()) {
        cmd = prop.get("app.run.window.stop").toString();
      } else {
        cmd = prop.get("app.run.stop").toString();
      }
      ProcessRunner r = new ProcessRunner();
      r.runProcess(dir, cmd, true);
      if (ProcessRunner.isWindows()) {
        cmd = prop.get("app.run.window.start").toString();
      } else {
        cmd = prop.get("app.run.start").toString();
      }
    } else if (run.equals("preparedeploy")) {
      if (ProcessRunner.isWindows()) {
        cmd = prop.get("app.run.window.preparedeploy").toString();
      } else {
        cmd = prop.get("app.run.preparedeploy").toString();
      }
      dir = prop.getProperty("app.run.deploy.dir");
    } else if (run.equals("deploy")) {
      if (ProcessRunner.isWindows()) {
        cmd = prop.get("app.run.window.deploy").toString();
      } else {
        cmd = prop.get("app.run.deploy").toString();
      }
      dir = prop.getProperty("app.run.deploy.dir");
    } else if (run.equals("afterdeploy")) {
      if (ProcessRunner.isWindows()) {
        cmd = prop.get("app.run.window.afterdeploy").toString();
      } else {
        cmd = prop.get("app.run.afterdeploy").toString();
      }
      dir = prop.getProperty("app.run.deploy.dir");
    }
    ProcessRunner r = new ProcessRunner();
    return r.runProcess(dir, cmd);
  }

  public AppServiceImpl() {
    agentInfo = getAgentInfo();
    reload();
    checkinStart();
  }

  private void checkinStart() {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(100);
    scheduler.scheduleAtFixedRate(new Runnable() {
      public void run() {
        agentInfo = getAgentInfo();
        push(agentInfo);
      }
    }, 0, PUSH_INTERVAL, TimeUnit.SECONDS);
  }

  public void push(AgentInfo agentInfo) {
    try {
      if (agentProp.getProperty("merlin.url") != null) {
        HttpPost post = new HttpPost(agentProp.getProperty("merlin.url").toString() + "/checkIn");
        post.setHeader(HTTP.CONTENT_TYPE, "application/json");

        String json = objectMapper.writeValueAsString(agentInfo);
        HttpEntity entity = new StringEntity(json, "UTF-8");
        post.setEntity(entity);

        HttpClient client = HttpClients.createDefault();
        HttpResponse resp = client.execute(post);
        System.out.println("push!!! = " + json);
        System.out.println("received!!! = " + EntityUtils.toString(resp.getEntity()));

      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void reload() {
    Date now = new Date();
    if (timestamp != null) {
      long gap = now.getTime() - timestamp.getTime();
      if (gap < 60000) { // 이전체크가 60 미만이면 안한다.
        return;
      } else {
        timestamp = now;
      }
    } else {
      timestamp = now;
    }
    List<AppStatus> appStatusList = new LinkedList<AppStatus>();
    File root = new File(appRootPath);
    if (!root.exists()) {
      root.mkdirs();
    }
    File[] list = root.listFiles();
    for (File f : list) {
      if (isApp(appStatusList, f) && f.isFile()) {
        appStatusList.add(loadApp(f));
        continue;
      }
    }
    agentInfo.setApps(appStatusList);
  }

  private boolean isApp(List<AppStatus> appStatusList, File f) {
    if (f.isDirectory() == true) {
      File[] list = f.listFiles();
      for (File f2 : list) {
        if (isApp(appStatusList, f2) && f2.isFile()) {
          appStatusList.add(loadApp(f2));
          continue;
        }
      }
      return false;
    }
    if (f.getName().equals("app.properties")) {
      return true;
    }
    return false;
  }

  private AppStatus loadApp(File f) {
    Properties prop = getProperity(f);
    String appName = prop.get("app.name").toString();
    appProp.put(appName, prop);
    FileLogger logger = LogFactory.getLogger(appName);
    if (logger == null) {
      logger = new FileLogger(f.getParentFile().getAbsolutePath() + "/log", appName);
      LogFactory.putLogger(appName, logger);
    }

    String type = prop.get("app.health.type").toString();
    String check = prop.get("app.health.check").toString();
    String result = "";
    logger.log(appName + "\t Check Start");
    if (type.equals("db")) {
      String userId = prop.get("app.health.check.userId").toString();
      String password = prop.get("app.health.check.password").toString();
      result = DbChecker.check(check, userId, password);
    } else if (type.equals("web")) {
      result = WebChecker.check(check);
    } else if (type.equals("ALM")) {
      result = ALMChecker.check(appName);
    }
    logger.log(appName + "\t Check Result:" + result);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    System.out.println(sdf.format(new Date()) + " Check[" + appName + "]:" + result);

    if (result.equals(AppStatus.DEAD) && prop.get("app.run.auto").toString().equals("true")) {
      logger.log(appName + "\t AutoRun:Restart");
      runApp(appName, "restart");
    }

    AppStatus appStatus = new AppStatus(appName, result);
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
