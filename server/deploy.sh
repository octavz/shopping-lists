#!/bin/bash
echo -n Password:
read -s pass
#gzname=stage_$(date +%s).tar.gz

echo "Building, please wait..."
sbt clean dist
echo "Copying, please wait..."


appname="main-0.1-SNAPSHOT"

sshpass -p $pass ssh octav@zaky.ro "rm -f /home/octav/$appname.zip"
sshpass -p $pass scp target/universal/$appname.zip octav@zaky.ro:/home/octav
sshpass -p $pass ssh octav@zaky.ro "rm -rf /home/octav/$appname/bin | rm -rf /home/octav/$appname/conf | rm -rf /home/octav/$appname/lib | rm -f /home/octav/$appname/README | unzip /home/octav/$appname.zip -d /home/octav"

echo "Transfer ok"

PID_PATH=/home/octav/$appname/RUNNING_PID

pid=`sshpass -p $pass ssh octav@zaky.ro cat $PID_PATH`
echo "Killing PID: $pid"

sshpass -p $pass ssh octav@zaky.ro "kill -9 $pid | rm -f $PID_PATH"
sshpass -p $pass ssh octav@zaky.ro "nohup /home/octav/$appname/bin/main -Dhttp.port=9000 -Dplay.evolutions.db.default.autoApply=true -Dplay.evolutions.db.default.autoApplyDowns=true &"

sshpass -p $pass ssh octav@zaky.ro "jps"

echo "Done"
