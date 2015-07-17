Solver Daemon
=============

License
-------
Refer to license.txt (Especially GAtech students!)

Pre-requisites
--------------

- Java 8
- Vert.x (if not using Maven. On OS X brew install vertx. I think.)
- Gurobi (with license, obviously)

Public Release Specific Notes
-----------------------------

- jni\_linux64 must contain the Gurobi native libraries.
- lib must contain the Gurobi jar package.

These have been omitted for licensing issues.

Running
-------

`vertx Server.java`

Debugging
---------

- Main Class: `org.vertx.java.platform.impl.cli.Starter`
- Program Args: `run edu.gatech.saadclass.project4.Server`
- Working Directory: `REPO/solver`

