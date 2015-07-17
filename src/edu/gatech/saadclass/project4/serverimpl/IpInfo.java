package edu.gatech.saadclass.project4.serverimpl;

/**
 * Copyright (c) 2015 Sangwhan Moon <sangwhan@iki.fi>
 */
public class IpInfo {
    public String getIp() {
        return ip;
    }

    public String getHostname() {
        return hostname;
    }

    public String getLoc() {
        return loc;
    }

    public String getOrg() {
        return org;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    private String ip;
    private String hostname;
    private String loc;
    private String org;
    private String city;
    private String region;
    private String country;
    private String phone;
}
