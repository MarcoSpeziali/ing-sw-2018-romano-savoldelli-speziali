#!/bin/bash

rmiregistry \
    -J-Djava.rmi.server.logCalls=true \
    -J-Djava.rmi.server.useCodebaseOnly=false \
    -J-Djava.rmi.server.codebase="file:///`pwd`/target/classes/ file:///`pwd`/../client/target/classes/ file:///`pwd`/../../lib/target/classes/" &