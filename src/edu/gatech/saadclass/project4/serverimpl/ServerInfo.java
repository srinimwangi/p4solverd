package edu.gatech.saadclass.project4.serverimpl;

import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Copyright (c) 2015 Sangwhan Moon <sangwhan@iki.fi>
 */
public class ServerInfo {
    public ServerInfo() {
        try {
            init();
        } catch (UnknownHostException uhe) {
            host_name = kDefaultHostName;
        }
    }

    public ServerInfo(int portNumber) {
        try {
            init();
            port_number = portNumber;
        } catch (UnknownHostException uhe) {
            host_name = kDefaultHostName;
        }
    }

    private void init() throws UnknownHostException {
        updateAddress();
        updateHostName();

        this.cores = Runtime.getRuntime().availableProcessors();
        this.available = 1;
    }

    public void updateHostName() throws UnknownHostException {
        host_name = InetAddress.getLocalHost().getHostName();
    }

    public void updateAddress() throws UnknownHostException {
        address = InetAddress.getLocalHost().getHostAddress();
        this.ping_url = "http://" + this.address + ":" + this.port_number + "/pong";
    }

    public void overrideHostName(String host_name) {
        this.host_name = host_name;
    }

    public void overrideExternalHostName(String host_name) {
        this.external_host_name = host_name;
    }

    public void overrideExternalAddress(String address) {
        this.external_address = address;
        this.external_ping_url = "http://" + this.external_address + ":" + this.port_number + "/pong";
    }

    public void overridePingUrl(String url) {
        this.ping_url = url;
    }

    public String getHostName() {
        return host_name;
    }

    public String getAddress() {
        return address;
    }

    public String getPingUrl() {
        return ping_url;
    }

    public String getExternalPingUrl() {
        return external_ping_url;
    }

    public void overrideExternalPingUrl(String url) {
        this.external_ping_url = url;
    }

    public void setAvailable(int flag) {
        this.available = flag;
    }

    public int getReachableVia() {
        return reachable_via;
    }

    public void setReachableVia(int reachable_via) {
        this.reachable_via = reachable_via;
    }

    public void updateExternalAddressAndHostname() {
        Gson gson = new Gson();

        IpInfo ipInfo = gson.fromJson(Fetch.get("http://ipinfo.io/json"), IpInfo.class);

        if (ipInfo != null) {
            external_address = ipInfo.getIp();
            external_host_name = ipInfo.getHostname();
        } else {
            // TODO: Figure out what to do here. It should still work as a local-only service.
        }
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public int getId() {
        return id;
    }

    public int isTaskAssigned() {
        return task_assigned;
    }

    public void setTaskAssigned(int task_assigned) {
        this.task_assigned = task_assigned;
    }

    public int getTasksRunning() {
        return tasks_running;
    }

    public void setTasksRunning(int tasks_running) {
        this.tasks_running = tasks_running;
    }

    public String getExternalAddress() {
        return external_address;
    }

    public void setExternalAddress(String external_address) {
        this.external_address = external_address;
        this.external_ping_url = "http://" + this.external_address + ":" + this.port_number + "/pong";
    }

    public String getExternalHostName() {
        return external_host_name;
    }

    public void setExternalHostName(String external_host_name) {
        this.external_host_name = external_host_name;
    }

    private int id;
    private int cores;
    private String host_name;
    private String external_host_name;
    private String address;
    private String external_address;
    private String ping_url;
    private String external_ping_url;
    private int port_number = 8080;
    private int available = 0;
    private int reachable_via = 0;
    private int task_assigned = 0;
    private int tasks_running = 0;

    private static final String kDefaultHostName = "localhost";
}
