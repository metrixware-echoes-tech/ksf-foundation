package com.tocea.corolla.backend.support.api;

import java.io.Serializable;
import java.util.List;

public class SupportDto implements Serializable {

    private List<PropertyDto> systemProperties;
    private List<PropertyDto> systemEnv;
    private List<PropertyDto> systemRuntime;
    private List<PropertyDto> systemTime;
    private List<NetworkInterfaceDto> systemNetwork;
    private List<FileStoreDto> systemFileStores;
    private String techmintMonitor;

    public SupportDto() {
    }

    public List<PropertyDto> getSystemEnv() {
        return systemEnv;
    }

    public List<FileStoreDto> getSystemFileStores() {
        return systemFileStores;
    }

    public List<NetworkInterfaceDto> getSystemNetwork() {
        return systemNetwork;
    }

    public List<PropertyDto> getSystemProperties() {
        return systemProperties;
    }

    public List<PropertyDto> getSystemRuntime() {
        return systemRuntime;
    }

    public List<PropertyDto> getSystemTime() {
        return systemTime;
    }

    public String getTechmintMonitor() {
        return techmintMonitor;
    }

    public void setSystemEnv(final List<PropertyDto> systemEnv) {
        this.systemEnv = systemEnv;
    }

    public void setSystemFileStores(final List<FileStoreDto> systemFileStores) {
        this.systemFileStores = systemFileStores;
    }

    public void setSystemNetwork(final List<NetworkInterfaceDto> systemNetwork) {
        this.systemNetwork = systemNetwork;
    }

    public void setSystemProperties(final List<PropertyDto> systemProperties) {
        this.systemProperties = systemProperties;
    }

    public void setSystemRuntime(final List<PropertyDto> systemRuntime) {
        this.systemRuntime = systemRuntime;
    }

    public void setSystemTime(final List<PropertyDto> systemTime) {
        this.systemTime = systemTime;
    }

    public void setTechmintMonitor(final String techmintMonitor) {
        this.techmintMonitor = techmintMonitor;
    }

}
