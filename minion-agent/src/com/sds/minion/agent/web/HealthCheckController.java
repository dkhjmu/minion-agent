package com.sds.minion.agent.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
	@RequestMapping("/healthCheck")
    public String index() {
        return "{ \"result\":\"OK\" }";
    }
}
