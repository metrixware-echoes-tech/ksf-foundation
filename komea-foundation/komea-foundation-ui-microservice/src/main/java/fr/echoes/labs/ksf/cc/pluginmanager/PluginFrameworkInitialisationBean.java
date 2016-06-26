/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.labs.ksf.cc.pluginmanager;

import fr.echoes.labs.pluginfwk.api.plugin.PluginFramework;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author sleroy
 */
@Component
public class PluginFrameworkInitialisationBean {
    
    @Autowired
    private PluginFramework pluginManager;
    
    
    @PostConstruct
    public void init() {
        pluginManager.reloadPlugins();
    }
    
}
