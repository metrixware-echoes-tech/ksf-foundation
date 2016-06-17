package fr.echoes.labs.ksf.cc.ui.views.main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service(value = "versionService")
public class VersionService {

    @Value("${buildVersion}")
    private String buildVersion;

    @Value("${buildTimestamp}")
    private String buildTimestamp;

    public String getBuildVersion() {
        return this.buildVersion;
    }

    public String getBuildTimestamp() {
        return this.buildTimestamp;
    }

}
