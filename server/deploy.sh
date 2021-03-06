#!/bin/bash
echo -n Password:
read -s pass

echo "Building, please wait..."
sbt clean dist
echo "Copying, please wait..."


appname="shoppinglist-0.1-SNAPSHOT"
PID_PATH=/home/octav/$appname/RUNNING_PID
pid=`sshpass -p $pass ssh octav@zaky.ro cat $PID_PATH`
echo "Found PID: $pid"

echo "Removing remote zip"
sshpass -p $pass ssh octav@zaky.ro "rm -f /home/octav/$appname.zip"
echo "Backup" 
sshpass -p $pass ssh octav@zaky.ro "mv /home/octav/$appname /home/octav/${appname}_bak_$(date +%s)"
echo "Copying new version"
sshpass -p $pass scp target/universal/$appname.zip octav@zaky.ro:/home/octav
sshpass -p $pass ssh octav@zaky.ro "unzip /home/octav/$appname.zip -d /home/octav"

echo "Transfer ok"


echo "Killing PID: $pid"

sshpass -p $pass ssh octav@zaky.ro "chmod +x /home/octav/$appname/bin/shoppinglist"
sshpass -p $pass ssh octav@zaky.ro "kill -SIGTERM $pid"
sshpass -p $pass ssh octav@zaky.ro "cd /home/octav/$appname; nohup /home/octav/$appname/bin/shoppinglist -Dhttp.port=9000 -Dplay.evolutions.db.default.autoApply=true -Dplay.evolutions.db.default.autoApplyDowns=true &"

sshpass -p $pass ssh octav@zaky.ro "jps"

echo "Done"
