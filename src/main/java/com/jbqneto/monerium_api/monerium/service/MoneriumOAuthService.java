package com.jbqneto.monerium_api.monerium.service;

import com.jbqneto.monerium_api.monerium.dto.response.MoneriumOAuthCallbackResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumOAuthStartResponse;

public interface MoneriumOAuthService {

    MoneriumOAuthStartResponse startAuthorization();

    MoneriumOAuthCallbackResponse handleCallback(String code, String state);
}
