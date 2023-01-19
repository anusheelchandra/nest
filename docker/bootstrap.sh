#!/bin/bash

SPRING_PROFILE="${APP_PROFILE:default}"

java -Dspring.profiles.active=$SPRING_PROFILE -Dlogging.path=/logs -Djava.awt.headless=true  -jar /nest.jar
