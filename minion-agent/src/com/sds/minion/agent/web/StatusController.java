package com.sds.minion.agent.web;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sds.minion.agent.domain.AgentInfo;
import com.sds.minion.agent.service.AppService;

@RestController
public class StatusController {
	
	@Resource
	AppService appService;
	
	@RequestMapping("/status")
    public AgentInfo status() {
        return appService.getAgentInfo();
    }
}
