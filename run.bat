@echo off
cd /d %1
java -jar target\imagerenamer-1.0.0-SNAPSHOT.jar %2 rename
pause
