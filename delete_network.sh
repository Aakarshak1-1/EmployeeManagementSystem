#!/bin/bash

# Get a list of all Docker networks
networks=$(docker network ls -q)

# Loop through each network and delete it
for network in $networks
do
    docker network rm $network
done


#step-1 create file
#step-2 chmod +x delete_networks.sh
#step-3 ./delete_networks.sh