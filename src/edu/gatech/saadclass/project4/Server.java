package edu.gatech.saadclass.project4;

import edu.gatech.saadclass.project4.serverimpl.Handlers;
import edu.gatech.saadclass.project4.serverimpl.Heartbeat;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.platform.Verticle;

import java.util.ArrayList;

public class Server extends Verticle {

    public static Server getInstance() {
        return (mInstance != null) ? mInstance : null;
    }

    public void start() {
        mInstance = this;
        mSolverThreads = new ArrayList<Thread>();

        mHeartBeatThread = Heartbeat.create();
        mHeartBeatThread.start();

        HttpServer check = vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {

                // FIXME: For now, use text plain until we have defined a JSON protocol.
                req.response().putHeader("Content-Type", "text/plain");

                switch (req.path()) {
                    case "/solve": {
                        Handlers.handlerSolve(req);
                        break;
                    }
                    case "/cleanup": {
                        Handlers.handlerCleanup(req);
                        break;
                    }
                    case "/pong": {
                        Handlers.handlerPong(req);
                        break;
                    }
                    default: {
                        Handlers.handlerNotFound(req);
                    }
                }
            }
        }).listen(kPort);
    }

    public void stop() {
        mInstance = null;
        mHeartBeatThread.interrupt();
        System.out.println("Good bye. You are the weakest link.");
    }

    public void cleanupThreads() {
        for (int i = 0; i < mSolverThreads.size(); i++) {
            if (!mSolverThreads.get(i).isAlive()) {
                mSolverThreads.remove(i);
            }
        }
    }

    public int appendTaskThread(Thread t) {
        mSolverThreads.add(t);
        int threadID = mSolverThreads.size() - 1; // Get the last added Thread
        mSolverThreads.get(threadID).start();

        return threadID;
    }

    public boolean canAcceptMoreTasks() {
        cleanupThreads();
        return (mSolverThreads.size() < kMaxThreadCount);
    }

    /**
     * @return
     */
    public int currentTaskCount() {
        cleanupThreads();
        return mSolverThreads.size();
    }

    private static volatile Server mInstance = null;
    private volatile ArrayList<Thread> mSolverThreads;
    private Thread mHeartBeatThread;

//    public static final String kServerBase = "http://localhost/"; // For debugging locally
    public static final String kServerBase = "https://v-project4.shib.al/";
    public static final int kPort = 8080;
    public static final int kMaxThreadCount = Runtime.getRuntime().availableProcessors() - 1;
    public static final int kHearbeatCycle = 2000; // 2 seconds.

    // FIXME: This markup is taken from some ancient web server. Not pretty.
    public static final String kNotFoundHTML = "<HTML><HEAD><TITLE>404 Not Found</TITLE></HEAD>" +
            "<BODY><H1>Not Found</H1>The requested document was not found on this server.<P><HR>\n"+
            "<ADDRESS>Solver/1.0.0</ADDRESS></BODY></HTML>";

}