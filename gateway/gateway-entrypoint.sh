#!/bin/sh

echo 'Starting the custom script'


while ! nc -z auth-server 9101
do
    echo "Waiting for upcoming Authentication Server"
    sleep 2
done


echo 'Pre-requisite done now moving on the execution of current container service'

nohup java -jar gateway.jar