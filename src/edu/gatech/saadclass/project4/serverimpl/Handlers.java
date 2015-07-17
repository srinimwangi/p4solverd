package edu.gatech.saadclass.project4.serverimpl;

import com.google.gson.Gson;
import edu.gatech.saadclass.project4.Server;
import edu.gatech.saadclass.project4.solver.GurobiSolver;
import edu.gatech.saadclass.project4.solver.SolverResult;

import org.vertx.java.core.http.HttpServerRequest;

/**
 * Copyright (c) 2015 Sangwhan Moon <sangwhan@iki.fi>
 */
public class Handlers {

    public static synchronized void handlerCleanup(HttpServerRequest req) {
        Gson gson = new Gson();
        Server.getInstance().cleanupThreads();
        req.response().setStatusCode(200).end(
                gson.toJson(new ResponseMessage("Clean-up complete."))
        );
    }

    public static synchronized void handlerSolve(HttpServerRequest req) {
        Gson gson = new Gson();
        int threadID = -1;
        Server.getInstance().cleanupThreads();

        if (!Server.getInstance().canAcceptMoreTasks()) {
            req.response().setStatusCode(503).end(
                    gson.toJson(new ResponseMessage("Too many tasks."))
            );
            return;
        } else {
            threadID = Server.getInstance().appendTaskThread(new Thread(() -> {
                SolverResult solverResult = new GurobiSolver().solve();
                String jsonResult = gson.toJson(solverResult);
                Fetch.post(Server.kServerBase + "solved", jsonResult);

                Synchronizer.updateTaskState();
            }));

            // Do not remove. The one above sits in a separate thread, so two
            // updates need to happen here.

            Synchronizer.updateTaskState();

            req.response().setStatusCode(200).end(
                    gson.toJson(new ResponseMessage("Solver invoked as " + threadID))
            );
        }
    }

    public static void handlerNotFound(HttpServerRequest req) {
        req.response().headers().remove("Content-Type");
        req.response().putHeader("Content-Type", "text/html");
        req.response().setStatusCode(404).end(Server.kNotFoundHTML);
    }

    public static void handlerPong(HttpServerRequest req) {
        req.response().setStatusCode(200).end("pong");
    }
}
