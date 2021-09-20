#******************************************************************************
# *                                                                           *
# * Copyright © 2001-2017 HealthEdge Software, Inc. All Rights Reserved.      *
# *                                                                           *
# * This software is proprietary information of HealthEdge Software, Inc.     *
# * and may not be reproduced or redistributed for any purpose.               *
# * AUTHURS: Srikant RAO, Sashikant PRASAD                                    *
# *****************************************************************************

source ./import_basic_lib.sh
source ./controller.sh
source ./validation.sh
source ./constants.sh
source ./script_util.sh

# Runtime environment variables.
PROFILE=${EMPTY}
DEFAULT=${EMPTY}
POM_PATH=${EMPTY}
KAR_TARGET_DIR=${EMPTY}

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
  info_log "Profile is ${PROFILE}"
  info_log "${LINE_SEPARATOR_HYPHEN}"
  return 0
}

load_dev_settings() {
  POM_PATH=$(get_property "${PROFILE,,}.path.to.pom")
  KAR_TARGET_DIR=$(get_property "${PROFILE,,}.kar.target.dir")
}

review() {
  info_log "List of congfigured variables for manual review."
  local i=0
  local str=$((i = i + 1))") FEATURE_NAME=${FEATURE_NAME}\n"
  str=${str}$((i = i + 1))") REPOS_NAME=${REPOS_NAME}\n"
  str=${str}$((i = i + 1))") CFG_FILE_NAME=${CFG_FILE_NAME}\n"
  str=${str}$((i = i + 1))") LASTRUN_FILE_NAME=${LASTRUN_FILE_NAME}\n"
  str=${str}$((i = i + 1))") BLUEPRINT_FILE_NAME=${BLUEPRINT_FILE_NAME}\n"
  str=${str}$((i = i + 1))") INSTALLABLE_REPOS=${INSTALLABLE_REPOS}\n"

  if [ ${PROFILE^^} = "DEV" ]; then
    str=${str}$((i = i + 1))") POM_PATH=${POM_PATH}\n"
    str=${str}$((i = i + 1))") KAR_TARGET_DIR=${KAR_TARGET_DIR}\n"
  fi
  info_log "\n${str}"
}

call_install_dev() {
  info_log ""
  #call_build
}

call_dev() {
  load_dev_settings
  case ${1,,} in
  cleanup)
    info_log "[Oparation:${1}] ${OPR_CLEANUP_DESCRIPTION}"
    ;;

  start)
    info_log "[Oparation:${1}] ${OPR_START_DESCRIPTION}"
    ;;

  stop)
    info_log "[Oparation:${1}] ${OPR_STOP_DESCRIPTION}"
    ;;

  restart)
    info_log "[Oparation:${1}] ${OPR_RESTART_DESCRIPTION}"
    ;;

  review)
    info_log "[Oparation:${1}] ${OPR_REVIEW_DESCRIPTION}"
    review
    ;;

  install)
    info_log "[Oparation:${1}] ${OPR_INSTALL_DESCRIPTION}"
    call_install_dev
    ;;

  build | ${DEFAULT})
    info_log "[Oparation:build] ${OPR_BUILD_DESCRIPTION}"
    ;;
  *)
    info_log "OPARATION NOT SUPPORTED."
    ;;
  esac
  #cleanup | start | stop | restart | review | install | build | deploy
}

call_qa() {
  case ${1} in
  cleanup)
    info_log "[Oparation:${1}] ${OPR_CLEANUP_DESCRIPTION}"
    ;;

  start)
    info_log "[Oparation:${1}] ${OPR_START_DESCRIPTION}"
    ;;

  stop)
    info_log "[Oparation:${1}] ${OPR_STOP_DESCRIPTION}"
    ;;

  restart)
    info_log "[Oparation:${1}] ${OPR_RESTART_DESCRIPTION}"
    ;;

  review)
    info_log "[Oparation:${1}] ${OPR_REVIEW_DESCRIPTION}"
    ;;
  install | ${DEFAULT})
    info_log "[Oparation:install] ${OPR_INSTALL_DESCRIPTION}"
    ;;
  *)
    info_log "OPARATION NOT SUPPORTED."
    ;;
  esac
  #cleanup | start | stop | restart | review | install | build | deploy
}

call_main() {
  if [[ "$#" -gt 2 ]]; then
    error_log "Illegal number of parameters"
    script_exits
  fi
  if [[ "${1,,}" == "usage" ]] || [[ "${1,,}" == "help" ]] || [[ "$1" == "$EMPTY" ]]; then
    call_usage
  fi

  call_set_profile "${1}"

  case ${PROFILE} in
  DEV)
    call_dev "${2}"
    ;;
  QA)
    call_qa "${2}"
    ;;
  *)
    info_log "Profile is ${PROFILE}"
    call_usage
    ;;
  esac
}

###
# Main body of script starts here
###

call_main "$1" "$2"

script_exits
