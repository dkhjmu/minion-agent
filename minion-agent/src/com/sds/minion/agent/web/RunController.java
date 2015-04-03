package com.sds.minion.agent.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunController {
	@RequestMapping("/run/{appName}/{runType}")
    public String index(@PathVariable String appName, @PathVariable String runType) {
		System.out.println("appName:"+appName);
		System.out.println("runType:"+runType);
        return "{ \"result\":\"OK\" }";
    }
}
