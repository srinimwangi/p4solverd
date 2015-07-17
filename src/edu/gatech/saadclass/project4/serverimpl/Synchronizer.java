package edu.gatech.saadclass.project4.serverimpl;

import com.google.gson.Gson;
import edu.gatech.saadclass.project4.Server;

/**
 * Copyright (c) 2015 Sangwhan Moon <sangwhan@iki.fi>
 */
public class Synchronizer {
    private static void init() {
        if (gson == null) {
            gson = new Gson();
        }
        if (serverInfo == null) {
            serverInfo = new ServerInfo(Server.kPort);
        }

        System.out.println("Solver is not registered to the registry. Registering.");
        ServerInfo res = null;
        // TODO: Hostname is a pretty weak unique identifier.
        String buf = Fetch.get(Server.kServerBase + "solvers?host_name=" + serverInfo.getHostName());

        // FIXME: This is a pretty naive check.
        if (buf.length() > 0) {
            System.out.println("Entry exists, updating.");
            res = gson.fromJson(buf, ServerInfo.class);

            // Update info on the server, and re-populate res based on the PUT return.
            System.out.println("Updating current solver info to server: " +
                    Server.kServerBase + "solvers/" + res.getId());

            probeExternalAddress();
            serverInfo.setExternalAddress(externalAddress);
            serverInfo.setExternalHostName(externalHostname);
            serverInfo.setReachableVia(Reachability.VIA_NOTHING);
            serverInfo.setTaskAssigned(res.isTaskAssigned());
            buf = Fetch.put(Server.kServerBase + "solvers/" + res.getId(), serverInfo.toString());
            res = gson.fromJson(buf, ServerInfo.class);
        } else {
            System.out.println("Solver entry does not exist, creating");
            String r2 = Fetch.post(Server.kServerBase + "solvers", serverInfo.toString());
            res = gson.fromJson(r2, ServerInfo.class);
        }

        if (res != null) {
            registered = true;
            serverInfo = res;
        }
    }

    public static void updateServerStatus() {
        if (!registered) {
            init();
        } else {
            ServerInfo res = null;

            if (++probeCount == kProbeFrequency) {
                probeReachability();
                probeExternalAddress();
                serverInfo.setExternalAddress(externalAddress);
                serverInfo.setExternalHostName(externalHostname);
                probeCount = 0;
            }

            // Periodically update to the server. This allows the server to know when the solver IP changed.
            // System.out.println("Updating solver information to the registry.");

            res = gson.fromJson(Fetch.get(Server.kServerBase + "solvers?id=" + serverInfo.getId()), ServerInfo.class);
            serverInfo.setTaskAssigned(res.isTaskAssigned());
            serverInfo.setTasksRunning(Server.getInstance().currentTaskCount());

            String buf = Fetch.put(Server.kServerBase + "solvers/" + serverInfo.getId(), serverInfo.toString());

            // FIXME: This is a pretty naive check.
            if (buf.length() > 0) {
                res = gson.fromJson(buf, ServerInfo.class);
            }

            if (res != null) {
                serverInfo = res;
            }
        }

        // We kickstart the payload indirectly in case the solver is behind
        // some form of firewall and not reachable at all from the server.

        if (serverInfo.isTaskAssigned() == 1) {
            Fetch.get("http://localhost:" + Server.kPort + "/solve");
        }

        setCanAcceptMoreTasks(Server.getInstance().canAcceptMoreTasks());
    }

    public static void updateTaskState() {
        serverInfo.setTaskAssigned(0);
        System.out.println("Task count: " + Server.getInstance().currentTaskCount());
        serverInfo.setTasksRunning(Server.getInstance().currentTaskCount());
        Fetch.put(Server.kServerBase + "solvers/" + serverInfo.getId(), serverInfo.toString());
    }

    public static void setCanAcceptMoreTasks(boolean flag) {
        // System.out.println("Able to accept more tasks. Updating availability in registry.");
        int availability = flag ? 1 : 0;
        Fetch.put(Server.kServerBase + "solvers/" + serverInfo.getId(), "{\"available\": " + availability + "}");
    }

    public static void notifyShutdown() {
        System.out.println("Notifying shutdown.");
        serverInfo.setAvailable(0);
        serverInfo.setReachableVia(Reachability.VIA_NOTHING);
        serverInfo.setTasksRunning(0);

        System.out.println(Fetch.put(Server.kServerBase + "solvers/" + serverInfo.getId(),
                gson.toJson(serverInfo)));
    }

    public static void probeReachability() {
        String pingURL = Server.kServerBase + "ping?url=" + serverInfo.getPingUrl()
                + "&alt_url=" + serverInfo.getExternalPingUrl();

        Reachability rc = gson.fromJson(Fetch.get(pingURL), Reachability.class);
        serverInfo.setReachableVia(rc.reachableVia());
    }

    private static void probeExternalAddress() {
        Gson gson = new Gson();

        IpInfo ipInfo = gson.fromJson(Fetch.get("http://ipinfo.io/json"), IpInfo.class);

        if (ipInfo != null) {
            externalAddress = ipInfo.getIp();
            externalHostname = ipInfo.getHostname();
        } else {
            // TODO: Figure out what to do here. It should still work as a local-only service.
        }
    }

    private static final int kProbeFrequency = 9;
    private static String externalAddress;
    private static String externalHostname;
    private static int probeCount = 0;
    private static Gson gson = null;
    private static boolean registered = false;
    private static ServerInfo serverInfo = null;
}
