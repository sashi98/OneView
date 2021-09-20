script_exits(){
  info_log "${LINE_SEPARATOR_HYPHEN}"
  info_log "${SCRIPT_END_BANNER}"
  exit 0
}

call_usage() {
  info_log "${LINE_SEPARATOR_STAR}"
  info_log "${USAGE_BANNER}"
  echo "./deploy_to_karaf_server.sh <PROFILE> <OPARATION>"
  echo "PROFILE : [QA|qa] or [DEV, dev]"
  echo "OPARATION : [cleanup | start | stop | restart | review | install | build | deploy]."
  echo "Default oparation is build [ DEV ] and install [ QA ]"
  echo "cleanup : Used with [ DEV | QA ]. ${OPR_CLEANUP_DESCRIPTION}"
  echo "start   : Used with [ DEV | QA ]. ${OPR_START_DESCRIPTION}"
  echo "stop    : Used with [ DEV | QA ]. ${OPR_STOP_DESCRIPTION}"
  echo "restart : Used with [ DEV | QA ]. ${OPR_RESTART_DESCRIPTION}"
  echo "review  : Used with [ DEV | QA ]. ${OPR_REVIEW_DESCRIPTION}"
  echo "install : Used with [ DEV | QA ]. ${OPR_INSTALL_DESCRIPTION}"
  echo "deploy  : Used with [ DEV | QA ]. ${OPR_DEPLOY_DESCRIPTION}"
  echo "build   : Used with [ DEV ] profile. ${OPR_BUILD_DESCRIPTION}"
  script_exits

}