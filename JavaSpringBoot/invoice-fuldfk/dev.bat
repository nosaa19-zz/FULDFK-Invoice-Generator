@echo off

cls

call mvn clean package -Dmaven.test.skip=true