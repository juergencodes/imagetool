@echo off
java -jar %~dp0target\imagerenamer-1.0.0-SNAPSHOT.jar "%CD%" print
pause
java -jar %~dp0target\imagerenamer-1.0.0-SNAPSHOT.jar "%CD%" rename
cd ..
