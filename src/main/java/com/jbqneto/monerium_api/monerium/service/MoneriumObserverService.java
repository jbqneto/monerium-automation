package com.jbqneto.monerium_api.monerium.service;

import com.jbqneto.monerium_api.monerium.config.MoneriumProperties;
import com.jbqneto.monerium_api.monerium.dto.internal.MoneriumAuthContext;

public interface MoneriumObserverService {

    MoneriumAuthContext getInitialDataAndWatch(MoneriumProperties properties);
}
