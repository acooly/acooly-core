#!/bin/bash
cd `dirname $0`

JAVA_HOME=/opt/jdk1.8.0_121
APP_NAME="$1"
JAVA_HEAP_SIZE="2048"
JAR_FILE="${APP_NAME}-assemble/target/${APP_NAME}.jar"

LOGS_DIR="/var/log/webapps/${APP_NAME}"
STDOUT_FILE=${LOGS_DIR}/stdout.log
START_DATE=`date +%Y%m%d%H%M%S`

echoRed() { echo $'\e[0;31m'$1$'\e[0m'; }
echoGreen() { echo $'\e[0;32m'$1$'\e[0m'; }
echoYellow() { echo $'\e[0;33m'$1$'\e[0m'; }
log(){
    now=`date "+%Y-%m-%d %H:%M:%S"`
    echo "${now}    $@" >> CONSOLE_LOG
    echoGreen "$@"
}

# 检查java版本
checkJavaVersion(){
    JAVA="$JAVA_HOME/bin/java"
    if [[ ! -r "$JAVA" ]]; then
        JAVA='java'
    fi

    JAVA_EXIST=`${JAVA} -version 2>&1 |grep 1.8`
    if [ ! -n "$JAVA_EXIST" ]; then
            log "JDK version is not 8"
            ${JAVA} -version
            exit 0
    fi
}


isRunning() {
  ps -p $1 &> /dev/null
}

checkIsRunning(){
    if [ ! -z "`ps -ef|grep -w "name=${APP_NAME} "|grep -v grep|awk '{print $2}'`" ]; then
        pid=`ps -ef|grep -w "name=$1 "|grep -v grep|awk '{print $2}'`
        log "$APP_NAME Already running pid=$pid";
        exit 0;
    fi
}

checkLogDir(){
    if [ ! -d ${LOGS_DIR} ]; then
        mkdir ${LOGS_DIR}
    fi
}

checkOptionvlue () {
	if [ "x$1" == "x" ];then
		log "$2 Option vlue is empty"
		exit 0;
	fi
}

checkBasepath () {
    log $#
	if [ $# -ne 1 ];then
		log "check option APP_NAME"
		exit 0
	fi
	checkOptionvlue "${APP_NAME}" "APP_NAME"

}



checkBasepath $*
checkJavaVersion
checkIsRunning
checkLogDir

log "Starting the $APP_NAME ..."

log "git pull"
cd $APP_NAME

pwd
git pull

log "maven package"

mvn -T 1C clean package -Dmaven.test.skip=true

JAVA_OPTS="-server -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -Xms${JAVA_HEAP_SIZE}m -Xmx${JAVA_HEAP_SIZE}m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:LargePageSizeInBytes=128m -XX:+ParallelRefProcEnabled -XX:+PrintAdaptiveSizePolicy -XX:+UseFastAccessorMethods -XX:+TieredCompilation -XX:+ExplicitGCInvokesConcurrent -XX:AutoBoxCacheMax=20000 $JAVA_OPTS"
JAVA_GC_LOG="-verbosegc  -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:${LOGS_DIR}/gc.log"
JAVA_OOM_DUMP="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${LOGS_DIR}/oom-${START_DATE}.hprof"
JAVA_OPTS="$JAVA_OPTS $JAVA_GC_LOG $JAVA_OOM_DUMP  -Dsys.name=$APP_NAME -Dspring.profiles.active=sdev"



log "JAVA_OPTS=${JAVA_OPTS}"
nohup ${JAVA} ${JAVA_OPTS} -jar ${JAR_FILE}  > ${STDOUT_FILE} 2>&1 &
sleep 2

tail -100f ${STDOUT_FILE}
