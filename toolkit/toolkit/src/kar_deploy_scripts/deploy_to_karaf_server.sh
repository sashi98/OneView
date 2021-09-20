#!/bin/bash

#******************************************************************************
# *
# * Copyright © 2001-2017 HealthEdge Software, Inc. All Rights Reserved.
# *
# * This software is proprietary information of HealthEdge Software, Inc.
# * and may not be reproduced or redistributed for any purpose.
# * PRIMARY AUTHUR: Srikant RAO
# * SECONDARY AUTHUR: Sashikant PRASAD
# *****************************************************************************

#Default environment variables.
VERSION="20.3-SNAPSHOT"
KNOWN_HOSTS=~/.ssh/known_hosts
LOCAL_HOST="\[localhost\]:8101"
FEATURE_NAME="ucare-ifp-welcome-letter"
REPOS_NAME_SUFFIX="-repos"
KARAF_SERVER_NAME="healthrules-connector-server-4.0.4"
CFG_FILE_NAME="com.healthedge.customer.ucare.ifp.welcome.letter.cfg"
LAST_RUN_ENTRY_FILE_NAME="set-last-run-ucare-corr-ucare-ifp-welcome-letter.json"
BLUE_PRINT_FILE_NAME="ucare-ifp-welcome-letter.xml"
CPRIME_HOME=~/cprime
PWD_DIR=$(pwd)
NEXUS="http://nexus:8081/content/repositories/staging/com/healthedge/customer"
CUSTOMER_NAME="ucare"
CORRESPONDENCE_HOME=$CPRIME_HOME/"correspondences/Ifp-welcome-letter"

CONNECTOR_HOME=$CPRIME_HOME/$KARAF_SERVER_NAME
LOG_DIR=$CONNECTOR_HOME/log
CFG_FILE=$CONNECTOR_HOME/etc/$CFG_FILE_NAME
LAST_RUN_ENTRY_FILE=$CONNECTOR_HOME/last-run-json/$LAST_RUN_ENTRY_FILE_NAME
BLUE_PRINT_FILE=$CONNECTOR_HOME/custom-blueprints-deploy/$BLUE_PRINT_FILE_NAME
OUTPUT_DIR=${CORRESPONDENCE_HOME}/output
INSTALL_DIR=$CONNECTOR_HOME/install
ALL_DISTRIBUTIONS=$INSTALL_DIR/$FEATURE_NAME"*.kar"

# Runtime environment variables.
TARGET_DIR=""
DISTRIBUTION_FILE_PATH=""
PROFILE=""

# Constants
EMPTY=""
RUNNING="Running ..."
REDIRECT_OUTPUT="REDIRECT_OUTPUT"
DEFAULT_OPTION=$EMPTY #No optional command is install
NEW_LINE=$EMPTY
DEFAULT_MVN_COMMAND="mvn clean install -U"

call_usage() {
  echo "**************************************************************************************************************"
  echo "----------------------------------------------- USAGE --------------------------------------------------------"
  echo "./deploy_to_karaf_server.sh <PROFILE> <OPTIONS>"
  echo "PROFILE : As of now [QA|qa] or [DEV, dev] profiles are allowed."
  echo "OPTIONS : [cleanup | start | stop | review | restart | install]. Default is install"
  echo ""
  echo "cleanup : Removes kar file, cfg file, blueprint, log, last run json file before deploying to karaf server"
  echo "start   : Only starts the server."
  echo "stop    : Only stops the server."
  echo "review  : Review for deployment settings."
  echo "restart : Only restarts server."
  echo "install : It clean up directories, build/download kar file, install the kar file and starts karaf server and sets last run entry."
  echo "deploy  : It clean up directories, install already generated/downloaded kar file and starts karaf server and sets last run entry."
  echo "--------------------------------------------------------------------------------------------------------------"
  exit 0
}

call_set_profile() {
  case ${@,,} in
  dev)
    PROFILE="DEV"
    ;;
  qa)
    PROFILE="QA"
    ;;
  *)
    PROFILE="WRONG PROFILE"
    ;;
  esac
  echo "[INFO] Profile is set to $PROFILE"
  echo "------------------------------------------------------------------"
  return 0
}

call_settings() {
  echo "[SETTINGS] Initializing run time deployment variables."

  case $1 in
  DEV)
    REPOS_NAME="$FEATURE_NAME$REPOS_NAME_SUFFIX"
    TARGET_DIR=$PWD_DIR/$REPOS_NAME/target
    if [[ ! -d "$TARGET_DIR" ]]; then
      echo "[WARNING] Directory $TARGET_DIR does not exists."
    fi

    DISTRIBUTION_FILE_NAME=$REPOS_NAME'-'$VERSION'.kar'
    DISTRIBUTION_FILE_PATH=$TARGET_DIR/$DISTRIBUTION_FILE_NAME
    ALL_DISTRIBUTIONS=$INSTALL_DIR/$FEATURE_NAME'*.kar'
    ;;
  QA)
    REPOS_NAME="$FEATURE_NAME$REPOS_NAME_SUFFIX"
    TARGET_DIR=$PWD_DIR/$REPOS_NAME/target
    if [[ ! -d "$TARGET_DIR" ]]; then
      echo "[WARNING] Directory $TARGET_DIR does not exists."
      echo "[INFO]Creating the directory."
      mkdir -p "$TARGET_DIR"

    fi

    echo "Enter the distribution version:"
    read -r VERSION
    DISTRIBUTION_FILE_NAME="$REPOS_NAME-$VERSION.kar"
    DISTRIBUTION_FILE_PATH=$TARGET_DIR/$DISTRIBUTION_FILE_NAME
    ALL_DISTRIBUTIONS="$INSTALL_DIR/$FEATURE_NAME*.kar"
    ;;
  esac

  echo "[SUGGESTION] Do you want to review deployment settings."
  echo "Press enter to skip; Y for a manual review of deployment settings."
  read -r YES
  if [[ "${YES,,}" == "y" ]]; then
    call_review_deployment_settings
  fi
  echo "------------------------------------------------------------------"
  return 0
}
call_build_kar() {
  echo "[INFO] Building kar file"
  echo "[INFO] Below are the choices to build kar."
  echo "1 for 'mvn clean install' -- [DEFAULT]"
  echo "2 for 'mvn clean install -DskipTests=true'"
  echo "Press enter for default or enter the choice:"
  build_command="$DEFAULT_MVN_COMMAND"
  read -r choice
  if [[ "$choice" == "2" ]]; then
    build_command="$build_command -DskipTests=true"
  fi
  echo "[INFO] Excecuting $build_command"
  $build_command
  bs=$?
  if [[ $bs -ne 0 ]]; then
    echo "[FAILURE] REASON: Build Failed. Build Status=$bs"
    exit 1
  fi
  echo "------------------------------------------------------------------"
  return 0
}

call_download_kar() {
  echo "[DOWNLOAD] Download kar file"
  #http://nexus:8081/content/repositories/staging/com/healthedge/customer/ucare/
  # ucare-ifp-welcome-letter-repos/19.6.0.3-202004280416-6f78777/ucare-ifp-welcome-letter-repos-19.6.0.3-202004280416-6f78777.kar
  echo "CD to $TARGET_DIR"
  cd -e "$TARGET_DIR"
  echo "[INFO] Take backup of old kar file(s) of target directory."
  call_take_backup "$TARGET_DIR"

  kar_url=$NEXUS/$CUSTOMER_NAME/$REPOS_NAME/$VERSION/$DISTRIBUTION_FILE_NAME
  wget -v "$kar_url"
  ds=$?
  if [[ $ds -eq 8 ]]; then
    echo "[FAILURE] Downloading is failed. Download Status Returned=$ds"
    exit 1
  fi
  echo "------------------------------------------------------------------"
  cd -e "$PWD_DIR"
  return 0
}

call_stop_server() {
  echo "[INFO] Stopping server...Please be waiting.."
  status=$($CONNECTOR_HOME/bin/status)
  while [ "$status" = "$RUNNING" ]; do
    $CONNECTOR_HOME/bin/stop
    sleep 5
    status=$($CONNECTOR_HOME/bin/status)
  done
  echo "[INFO] Server $status"
  echo "------------------------------------------------------------------"
  return 0
}
call_cleanup() {
  echo "[INFO] Cleanup work is started..."
  i=0
  echo $((i = i + 1))") REMOVING LOG"
  rm -vf $LOG_DIR/*

  echo $((i = i + 1))") TAKING BACKUP OF EXTRACTS"
  call_take_backup $OUTPUT_DIR

  echo $((i = i + 1))") REMOVING CFG FILE"
  rm -vf $CFG_FILE

  echo $((i = i + 1))") REMOVING LAST RUN ENTRY FILE"
  rm -vf $LAST_RUN_ENTRY_FILE

  echo $((i = i + 1))") REMOVING BLUEPRINT FILE"
  rm -vf $BLUE_PRINT_FILE

  echo $((i = i + 1))") REMOVING KAR FILE(s):$ALL_DISTRIBUTIONS"
  rm -vf $ALL_DISTRIBUTIONS

  echo "------------------------------------------------------------------"
  return 0
}

call_take_backup() {
  NOW=$(date +'%d-%m-%y')
  BACKUP_DIR_NM="BKUP_"$NOW
  retval=$(mkdir -p "$1/$BACKUP_DIR_NM")
  echo "[INFO] $retval"
  mv -v "$1"/*.* "$1/$BACKUP_DIR_NM"
  echo "[INFO] Backup is DONE"
}

call_curl_delete() {
  echo "[INFO] Excecuting curl delete"
  curl -XDELETE localhost:9200/*
  sleep 5
  echo ""
  return 0
}

call_sshpass_to_run_command() {
  ss=0
  if [[ "${2^^}" == "$REDIRECT_OUTPUT" ]]; then
    sshpass -p Connector123 ssh -p 8101 connector@localhost $1 >>temp.txt
    ss=$?
    #cat temp.txt
  else
    sshpass -p Connector123 ssh -p 8101 connector@localhost $1
    ss=$?
  fi

  if [[ $ss -eq 255 ]]; then
    echo "[FAILURE] Server Satatus Returned=$ss. Check the server, it might be stoped."
    exit 1
  elif [[ $ss -eq 1 ]]; then
    echo "[WARNING] Server Satatus Returned=$ss. Server encountered some issue while running $1 command."
    echo "[SUGGESTION] You may ignore/ manually verify the issue. Press enter key to ignore, 1 to manual veryfication."
    read -r input
    if [[ "$input" == "1" ]]; then
      echo "[SUGGESTION] Please put some manual effort to verify why the command failed. Exiting.."
      exit 1
    fi
  else
    echo "[INFO] Server Satatus Returned=$ss. Server executed the $1 command"
  fi
  sleep 5
  return 0
}

call_list_feature() {
  echo "[list] Following $FEATURE_NAME feature(s) are installed"
  call_sshpass_to_run_command "list|grep -i $FEATURE_NAME"
  return 0
}

call_feature_uninstall() {
  echo "[feature:uninstall] Uninstalling $REPOS_NAME repo"
  call_sshpass_to_run_command "feature:uninstall $REPOS_NAME"
  return 0
}

call_kar_uninstall() {
  echo "[kar:uninstall] Uninstalling kars of $REPOS_NAME feature to clear chache."
  call_sshpass_to_run_command "kar:list|grep $REPOS_NAME" $REDIRECT_OUTPUT

  IFS=$'\n'
  repos_array=($(cat temp.txt))

  for repo in "${repos_array[@]}"; do
    repo=${repo//"[43;30m"/} #removing junk characters
    repo=${repo//"[m"/}      #removing junk characters
    call_sshpass_to_run_command "kar:uninstall $repo"
    sleep 5
  done
  unset IFS
  rm -vf temp.txt
}

call_add_distribution() {
  echo "[add-distribution] $DISTRIBUTION_FILE_PATH"
  call_sshpass_to_run_command "hrc:add-distribution --force $DISTRIBUTION_FILE_PATH"

  return 0
}

call_feature_install() {
  echo "[feature:install] Installing the $REPOS_NAME feature"
  call_sshpass_to_run_command "feature:install $REPOS_NAME"
  return 0
}

call_set_last_run_entry() {
  echo "[last-run-entry] Sets the last run entry for elastic search."
  call_sshpass_to_run_command "last-run-entry file:///$LAST_RUN_ENTRY_FILE"

  return 0
}
call_install() {
  echo "[INFO] Deploying bundle to karaf server..."
  echo "$NEW_LINE"
  call_curl_delete

  echo "$NEW_LINE"
  call_sshpass_check

  echo "$NEW_LINE"
  echo "[INFO] Listing of feature status before uninstall"
  call_list_feature

  echo "$NEW_LINE"
  call_feature_uninstall

  echo "$NEW_LINE"
  call_kar_uninstall

  echo "$NEW_LINE"
  call_add_distribution

  echo "$NEW_LINE"
  call_feature_install

  echo "$NEW_LINE"
  echo "[INFO] Listing of featur status after feature install"
  call_list_feature

  echo "$NEW_LINE"
  call_set_last_run_entry

  echo "------------------------------------------------------------------"
  return 0
}

call_start_server() {
  echo "[INFO] Starting server..Please be waiting..."
  status=$($CONNECTOR_HOME/bin/status)
  while [ "$status" != "$RUNNING" ]; do
    echo "[WARNING] Trying to reach. Got the status of server as $status"
    $CONNECTOR_HOME/bin/start
    sleep 10
    status=$($CONNECTOR_HOME/bin/status)
  done
  echo "[INFO] Server $status"
  echo "------------------------------------------------------------------"
  return 0
}

call_test_directory() {
  if [[ ! -d "$1" ]]; then
    echo "[FAILURE] Directory $1 does not exists."
    exit 1
  fi
  return 0
}

call_validate_directories() {
  echo "[INFO] Validating dev profile config"
  call_test_directory "$CPRIME_HOME"
  call_test_directory "$TARGET_DIR"
  call_test_directory "$CONNECTOR_HOME"
  call_test_directory "$OUTPUT_DIR"
  echo "[INFO] Configured directories are all good."
  echo "------------------------------------------------------------------"
  return 0
}

call_sshpass_check() {
  status=$(cat "$KNOWN_HOSTS | grep $LOCAL_HOST | wc -l")

  if [[ "$status" -eq 0 ]]; then
    echo "[INFO] Opening ssh connection to localhost:8101 for the first time"
    echo "[INFO] So, running commands to add localhost:8101 TO ~/.ssh/known_hosts"
    ssh-keyscan -p 8101 localhost >>~/.ssh/known_hosts
    echo "[INFO] Now we are good to run our sshpass"
  else
    echo "[INFO] VERIFIED ~/.ssh/known_hosts. Good to run our sshpass commands"
  fi
  return 0
}

call_review_deployment_settings() {
  echo "[INFO] List of congfigured variables for manual review.."
  i=0
  echo $((i = i + 1))") VERSION=$VERSION"
  echo $((i = i + 1))") KNOWN_HOSTS=$KNOWN_HOSTS"
  echo $((i = i + 1))") LOCAL_HOST=$LOCAL_HOST"
  echo $((i = i + 1))") FEATURE_NAME=$FEATURE_NAME"
  echo $((i = i + 1))") REPOS_NAME=$REPOS_NAME"
  echo $((i = i + 1))") KARAF_SERVER_NAME=$KARAF_SERVER_NAME"
  echo $((i = i + 1))") CFG_FILE_NAME=$CFG_FILE_NAME"
  echo $((i = i + 1))") DISTRIBUTION_FILE_NAME=$DISTRIBUTION_FILE_NAME"
  echo $((i = i + 1))") LAST_RUN_ENTRY_FILE_NAME=$LAST_RUN_ENTRY_FILE_NAME"
  echo $((i = i + 1))") BLUE_PRINT_FILE_NAME=$BLUE_PRINT_FILE_NAME"

  echo $((i = i + 1))") CPRIME_HOME=$CPRIME_HOME"
  echo $((i = i + 1))") TARGET_DIR=$TARGET_DIR"
  echo $((i = i + 1))") CONNECTOR_HOME=$CONNECTOR_HOME"
  echo $((i = i + 1))") LOG_DIR=$LOG_DIR"
  echo $((i = i + 1))") CFG_FILE=$CFG_FILE"
  echo $((i = i + 1))") LAST_RUN_ENTRY_FILE=$LAST_RUN_ENTRY_FILE"
  echo $((i = i + 1))") BLUE_PRINT_FILE=$BLUE_PRINT_FILE"
  echo $((i = i + 1))") OUTPUT_DIR=$OUTPUT_DIR"
  echo $((i = i + 1))") DISTRIBUTION_FILE_PATH=$DISTRIBUTION_FILE_PATH"
  echo $((i = i + 1))") INSTALL_DIR=$INSTALL_DIR"
  echo $((i = i + 1))") ALL_DISTRIBUTIONS=$ALL_DISTRIBUTIONS"
  echo ""
  echo "[WARNING] As per your review, are the all configurations good?"
  echo "(Press enter to continue; 'N' for correction)"
  read -r NO
  if [[ "${NO,,}" == "n" ]]; then
    echo "[ERROR] Please do the needful correction of your deployment variables befor you start; Exiting.."
    exit 1
  fi
  echo "[INFO] Everything looks good to start."

  return 0
}

call_dev() {
  case ${1,,} in
  stop)
    echo "[stop]: Only stops the server."
    call_stop_server
    ;;
  start)
    echo "[start]: Only starts the server."
    call_start_server
    ;;
  restart)
    echo "[restart]: Only restarts the server."
    call_stop_server
    call_start_server
    ;;
  cleanup)
    echo "[cleanup]: Removes kar file, cfg file, blueprint, log, last run json file before deploying to karaf server"
    call_settings "$PROFILE"
    call_stop_server
    call_cleanup
    ;;
  install | $DEFAULT_OPTION)
    echo "[install]: It clean up directories, build/download kar file, install the kar file and starts karaf server and sets last run entry."
    call_settings "$PROFILE"
    call_build_kar
    call_validate_directories
    call_stop_server
    call_cleanup
    call_start_server
    call_install
    ;;
  deploy)
    echo "[deploy]: It clean up directories, install already generated/downloaded kar file and starts karaf server and sets last run entry."
    call_settings "$PROFILE"
    #   call_validate_directories
    call_stop_server
    call_cleanup
    call_start_server
    call_install
    ;;
  review)
    echo "[review]: Review for deployment settings."
    call_settings "$PROFILE"
    call_review_deployment_settings
    ;;
  lastrun)
    echo "[lastrun]: Sets the last run entry."
    call_settings "$PROFILE"
    call_set_last_run_entry
    ;;
  test)
    call_build_kar
    ;;

  *)
    echo "[FAILURE] Invalid Arguments"
    ;;
  esac
  return 0
}

call_qa() {
  case ${1,,} in
  stop)
    echo "[stop]: Only stops the server."
    call_stop_server
    ;;
  start)
    echo "[start]: Only starts the server."
    call_start_server
    ;;
  restart)
    echo "[restart]: Only restarts server."
    call_stop_server
    call_start_server
    ;;
  cleanup)
    echo "[cleanup]: Removes kar file, cfg file, blueprint, log, last run json file before deploying to karaf server"
    call_settings "$PROFILE"
    call_stop_server
    call_cleanup
    ;;
  install | $DEFAULT_OPTION)
    echo "[install]: It clean up directories, build/download kar file, install the kar file and starts karaf server and sets last run entry."
    call_settings "$PROFILE"
    call_download_kar
    call_stop_server
    call_cleanup
    call_start_server
    call_install
    ;;
  deploy)
    echo "[deploy]: It clean up directories, install already generated/downloaded kar file and starts karaf server and sets last run entry."
    call_settings "$PROFILE"
    call_stop_server
    call_cleanup
    call_start_server
    call_install
    ;;
  review)
    echo "[review]: Review for deployment settings"
    call_settings "$PROFILE"
    call_review_deployment_settings
    ;;
  lastrun)
    echo "[lastrun]: Sets the last run entry."
    call_settings "$PROFILE"
    call_set_last_run_entry
    ;;
  esac
  return 0
}
###
# Main body of script starts here
###
echo "[INFO] START OF THE SCRIPT..."

if [[ "$#" -gt 2 ]]; then
  echo "[FAILURE] Illegal number of parameters"
  exit 1
fi

if [[ "${1,,}" == "usage" ]] || [[ "${1,,}" == "help" ]] || [[ "$1" == "$EMPTY" ]]; then
  call_usage
fi

call_set_profile "$1"

OPTION_COMMAND=$2

case $PROFILE in
DEV)
  call_dev "$OPTION_COMMAND"
  ;;
QA)
  call_qa "$OPTION_COMMAND"
  ;;
*)
  echo "[INFO] Profile is $PROFILE"
  call_usage
  ;;
esac
echo "********************* ENDED SUCCESSFULLY **********************************"
echo "Type reset and press enter"
echo -e "\033[0m"
exit 0
