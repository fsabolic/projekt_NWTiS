#!/bin/bash
NETWORK=fsabolic_mreza_1

docker run -it -d \
  -p 8070:8080 \
  --network=$NETWORK \
  --ip 200.20.0.4 \
  --name=fsabolic_payara_micro \
  --hostname=fsabolic_payara_micro \
  fsabolic_payara_micro:6.2023.4 \
  --deploy /opt/payara/deployments/fsabolic_aplikacija_2-1.0.0.war \
  --contextroot fsabolic_aplikacija_2 \
  --noCluster &

wait
