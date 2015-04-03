package com.sds.minion.agent.web;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sds.minion.agent.service.AppService;

@RestController
public class RunController {
	
	@Resource
	AppService appService;
	
	@RequestMapping("/run/{appName}/{runType}")
    public String index(@PathVariable String appName, @PathVariable String runType) {
		System.out.println("appName:"+appName);
		System.out.println("runType:"+runType);
		appService.runApp(appName, runType);
        return "{ \"result\":\"OK\" }";
    }
}
