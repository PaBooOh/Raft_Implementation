#!/bin/bash
cd /local/ddps2211/module/Raft_Implementation
pwd
kill -9 `lsof -ti:2144`;
mvn clean
mvn compile exec:java -Dexec.mainClass="com.raft.Main.ServerTest" -Dexec.args="node102:2144,node103:2144,node105:2144,node106:2144,node107:2144 $1:2144"