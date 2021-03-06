#!/usr/bin/env bash
# ------------------------------------
JVM_ARGS="-server -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Djava.io.tmpdir=/tmp -Djava.net.preferIPv6Addresses=false"
JVM_GC="-XX:+DisableExplicitGC -XX:+PrintGCDetails -XX:+PrintHeapAtGC -XX:+PrintTenuringDistribution -XX:+UseConcMarkSweepGC -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps"
JVM_GC=$JVM_GC" -XX:CMSFullGCsBeforeCompaction=0 -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=80"
JVM_HEAP="-XX:SurvivorRatio=8 -XX:PermSize=256m -XX:MaxPermSize=256m -XX:+HeapDumpOnOutOfMemoryError -XX:ReservedCodeCacheSize=128m -XX:InitialCodeCacheSize=128m"
JVM_SIZE="-Xmx4g -Xms4g"

# ------------------------------------
# do not edit
# ------------------------------------
function init() {
if [ -z "$LOG_PATH" ]; then
LOG_PATH="/opt/logs/$MODULE"
fi

if [ -z "$WORK_PATH" ]; then
WORK_PATH="/opt/meituan/mobile/$MODULE"
fi
mkdir -p $WORK_PATH/db
mkdir -p $LOG_PATH

#定时清理日志
cleanpath="$WORK_PATH/clean.sh"
echo "#!/bin/bash" > $cleanpath
echo "find $LOG_PATH -mtime +1 -exec /bin/gzip {} \;" >> $cleanpath
echo "find $LOG_PATH -mtime +3 -exec rm -fr {} \;" >> $cleanpath
chmod +x $cleanpath
(crontab -l|grep -v $cleanpath ; echo "58 05 * * * /bin/bash $cleanpath > /dev/null 2>&1" ) | crontab
}

function run() {
#根据java版本,决定java命令的位置
JAVA_CMD=$JAVA_VERSION
if [ -z "$JAVA_VERSION" ]; then
JAVA_CMD="java" #系统默认的java命令
else
JAVA_CMD="/usr/local/$JAVA_VERSION/bin/java"
fi

EXEC="exec"
CONTEXT=/
#cd $WEB_ROOT
if [ -e "WEB-INF/classes/release" ]; then
cp -rf WEB-INF/classes/release/* WEB-INF/classes
fi


JAVA_ARGS="-Djava.awt.headless=false"
EXEC_JAVA="$EXEC $JAVA_CMD $JVM_ARGS $JVM_SIZE $JVM_HEAP $JVM_JIT $JVM_GC"
EXEC_JAVA=$EXEC_JAVA" -Xloggc:$LOG_PATH/$MODULE.gc.log -XX:ErrorFile=$LOG_PATH/$MODULE.vmerr.log -XX:HeapDumpPath=$LOG_PATH/$MODULE.heaperr.log"
EXEC_JAVA=$EXEC_JAVA" $JAVA_ARGS"
if [ "$UID" = "0" ]; then
ulimit -n 1024000
umask 000
else
echo $EXEC_JAVA
fi
$EXEC_JAVA -jar $WORK_PATH/datalink.jar --log $LOG_PATH/request.yyyy_mm_dd --out $LOG_PATH/log.yyyy_mm_dd 2>&1
}

# ------------------------------------
# actually work
# ------------------------------------
init
run