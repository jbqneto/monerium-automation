package com.jbqneto.monerium_api.monerium.controller;

import com.jbqneto.monerium_api.monerium.dto.response.MoneriumOAuthCallbackResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumOAuthStartResponse;
import com.jbqneto.monerium_api.monerium.service.MoneriumOAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monerium/oauth")
public class MoneriumOAuthController {

    private final MoneriumOAuthService moneriumOAuthService;

    public MoneriumOAuthController(MoneriumOAuthService moneriumOAuthService) {
        this.moneriumOAuthService = moneriumOAuthService;
    }

    @GetMapping("/test")
    public String test() {
        return "monerium";
    }

    @GetMapping("/start")
    public MoneriumOAuthStartResponse startAuthorization() {
        return moneriumOAuthService.startAuthorization();
    }

    @GetMapping("/callback")
    public MoneriumOAuthCallbackResponse handleCallback(
        @RequestParam String code,
        @RequestParam String state
    ) {
        return moneriumOAuthService.handleCallback(code, state);
    }
}
