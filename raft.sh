#!/bin/bash
for i in node102 node103 node105 node106 node107
do
 echo ---------- Raft $i start ------------
 ssh $i "source /local/ddps2211/module/Raft_Implementation/raft_start.sh $i" &
done