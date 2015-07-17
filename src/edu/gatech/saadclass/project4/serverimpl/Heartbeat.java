package edu.gatech.saadclass.project4.serverimpl;

import com.google.gson.JsonSyntaxException;
import edu.gatech.saadclass.project4.Server;

/**
 * Copyright (c) 2015 Sangwhan Moon <sangwhan@iki.fi>
 */
public class Heartbeat {
    public static Thread create() {
        return new Thread(() -> {
            try {
                while (true) {
                    try {
                        Synchronizer.updateServerStatus();
                        Thread.sleep(Server.kHearbeatCycle);
                    } catch (JsonSyntaxException jse) {
                        System.out.println("Error when parsing server response.");
                        Thread.sleep(Server.kHearbeatCycle * 2);
                    }
                }
            } catch (InterruptedException ie) {
                Synchronizer.notifyShutdown();
                System.out.println("Shutting down. Stopping heartbeat thread.");
            }
        });
    }
}
