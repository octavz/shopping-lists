#!/bin/sh

PLANNER_HOME=/home/octav/Planner

PID_PATH=$PLANNER_HOME"/target/universal/stage/RUNNING_PID"

run()
{
 activator -mem 150 start
}

cd $PLANNER_HOME

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
