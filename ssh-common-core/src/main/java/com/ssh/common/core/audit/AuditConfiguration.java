package com.ssh.common.core.audit;

import org.hibernate.cfg.Configuration;

import java.util.Map;
import java.util.WeakHashMap;

public class AuditConfiguration {

    private static Map<Configuration, AuditConfiguration> auditConfigurationMap = new WeakHashMap<>();

    private final AuditProcessManager auditProcessManager;

    public AuditConfiguration() {
        auditProcessManager = new AuditProcessManager();
    }

    public AuditProcessManager getAuditProcessManager() {
        return auditProcessManager;
    }

    public synchronized static AuditConfiguration get(Configuration configuration) {
        AuditConfiguration auditConfiguration = auditConfigurationMap.get(configuration);
        if (auditConfiguration == null) {
            auditConfiguration = new AuditConfiguration();
            auditConfigurationMap.put(configuration, auditConfiguration);
            configuration.buildMappings();
        }
        return auditConfiguration;
    }

}
