#!/bin/sh

APP_HOME=/home/octav/projects/shopping-list/server

cd $APP_HOME

#sbt clean stage

PID_PATH=$APP_HOME"/target/universal/stage/RUNNING_PID"

run()
{
 sbt -mem 250 start
}

cd $APP_HOME

if [ ! -f $PID_PATH ]; then
    echo "PID file not found at $PID_PATH!"
    run
    exit $?
fi

pid=`cat $PID_PATH`

if ps -p $pid > /dev/null
then
   echo "$pid is running"
else
   echo "$pid is not running"
   rm -f $PID_PATH
   run
fi
