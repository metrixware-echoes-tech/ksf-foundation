package com.tocea.corolla.backend.support.api;

import java.io.Serializable;

public class FileStoreDto implements Serializable {

    private String name;
    private String type;
    private long totalSpace;
    private long usableSpace;
    private long unallocatedSpace;
    private boolean readOnly;

    public FileStoreDto() {
    }

    public FileStoreDto(String name, String type, long totalSpace, long usableSpace, long unallocatedSpace, boolean readOnly) {
        this.name = name;
        this.type = type;
        this.totalSpace = totalSpace;
        this.usableSpace = usableSpace;
        this.unallocatedSpace = unallocatedSpace;
        this.readOnly = readOnly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }

    public long getUsableSpace() {
        return usableSpace;
    }

    public void setUsableSpace(long usableSpace) {
        this.usableSpace = usableSpace;
    }

    public long getUnallocatedSpace() {
        return unallocatedSpace;
    }

    public void setUnallocatedSpace(long unallocatedSpace) {
        this.unallocatedSpace = unallocatedSpace;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public String toString() {
        return "name = " + name + "\n"
                + "type = " + type + "\n"
                + "totalSpace = " + totalSpace + "\n"
                + "usableSpace = " + usableSpace + "\n"
                + "unallocatedSpace = " + unallocatedSpace + "\n"
                + "readOnly = " + readOnly + "\n";
    }

}
