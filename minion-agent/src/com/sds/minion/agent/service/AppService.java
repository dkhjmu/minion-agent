package com.sds.minion.agent.service;

import com.sds.minion.agent.domain.AgentInfo;

public interface AppService {
  
  public AgentInfo getAgentInfo();
  
  public void runApp(String name, String run);
  
}
