# Raft_Implementation

## Introduction

This project implemented a distributed system based on Raft algorithm. We developed this project using Java (Maven). Specifically, the RPC communcation protocal is gRPC,
while Google's protobuf is leveraged for serializing structured data.

## Implementation

We implemented the crucial parts of Raft (not fully). They are leader election, log replication, and safety. See table below for more details.

|Chapter   |Name   | Status   |
|----------|-------|----------|
| 0.1 | Cluster building-Consensus service will be not available until all servers have already started | :heavy_check_mark: |
| 0.2 | Persistent state on all servers | :x: |
| 1 | Leader election | :heavy_check_mark: |
| 2.1 | Log replication-leader issues Heartbeat in parallel to each of the other servers in the cluster | :heavy_check_mark: |
| 2.2 | Log replication-remote client connect to cluster (if the server connected to is not leader, redirect then) | :heavy_check_mark: |
| 2.3 | Log replication-remote client send a command to cluster | :heavy_check_mark: |
| 2.4 | Log replication-leader receive the command from client, which will be then stored as Entry in its Log | :heavy_check_mark: |
| 2.5 | Log replication-leader issues AppendEntriesRPCs in parallel to each of the other servers in the cluster | :heavy_check_mark: |
| 2.6 | Log replication-followers receive AppendEntriesRPCs and add them into their respective Log | :heavy_check_mark: |
| 2.7 | Log replication-followers acknowledge this RPC from leader | :heavy_check_mark: |
| 2.8 | Log replication-leader commit this entry | :x: |
| 2.9 | Log replication-leader apply this entry | :x: |
| 3 | Cluster membership changes  | :x: |
| 4 | Log compaction | :x: |
| 5 | Snapshots installation | :x: |
| 6 | Committing entries from previous terms | :heavy_check_mark: |
| 7.1 | Leader crashes | :x: |
| 7.2 | Follower crashes | :x: |
| 7.3 | Candidate crashes | :x: |

## How to run

1. Use __git clone__ command that clones our project to local file
2. (Recommended)
2. (Not recommended) Deploy, say three, RaftServers in three separate cloud nodes by using the command below
> mvn compile exec:java -Dexec.mainClass="com.raft.Main.ServerTest" -Dexec.args="CLUSTER LOCALSERVER"
For example:
> mvn compile exec:java -Dexec.mainClass="com.raft.Main.ServerTest" -Dexec.args="node102:1234,node103:1234,node105:1234 node102:1234"
 mvn compile exec:java -Dexec.mainClass="com.raft.Main.ServerTest" -Dexec.args="node102:1234,node103:1234,node105:1234 node103:1234"
 mvn compile exec:java -Dexec.mainClass="com.raft.Main.ServerTest" -Dexec.args="node102:1234,node103:1234,node105:1234 node105:1234"
