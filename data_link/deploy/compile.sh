#!/bin/sh
mvn clean -U package -Denv_config=$ENV  -Dmaven.test.skip=true