package com.tocea.corolla.backend.support.api;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkInterfaceDto implements Serializable {

    private String name;
    private String displayName;
    private String addresses;
    private boolean up;
    private boolean virtual;
    private boolean multicast;
    private boolean loopback;
    private boolean ptp;
    private int mtu;

    public NetworkInterfaceDto() {
    }

    public NetworkInterfaceDto(final NetworkInterface networkInterface) throws SocketException {
        this.displayName = networkInterface.getDisplayName();
        this.loopback = networkInterface.isLoopback();
        this.mtu = networkInterface.getMTU();
        this.multicast = networkInterface.supportsMulticast();
        this.name = networkInterface.getName();
        this.ptp = networkInterface.isPointToPoint();
        this.up = networkInterface.isUp();
        this.virtual = networkInterface.isVirtual();
        final StringBuilder sb = new StringBuilder();
        final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
            sb.append(inetAddresses.nextElement().toString()).append(", ");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
        }
        this.addresses = sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public boolean isMulticast() {
        return multicast;
    }

    public void setMulticast(boolean multicast) {
        this.multicast = multicast;
    }

    public boolean isLoopback() {
        return loopback;
    }

    public void setLoopback(boolean loopback) {
        this.loopback = loopback;
    }

    public boolean isPtp() {
        return ptp;
    }

    public void setPtp(boolean ptp) {
        this.ptp = ptp;
    }

    public int getMtu() {
        return mtu;
    }

    public void setMtu(int mtu) {
        this.mtu = mtu;
    }

    @Override
    public String toString() {
        return "name = " + name + "\n"
                + "displayName = " + displayName + "\n"
                + "addresses = " + addresses + "\n"
                + "up = " + up + "\n"
                + "virtual = " + virtual + "\n"
                + "multicast = " + multicast + "\n"
                + "loopback = " + loopback + "\n"
                + "ptp = " + ptp + "\n"
                + "mtu = " + mtu + "\n";
    }

}
