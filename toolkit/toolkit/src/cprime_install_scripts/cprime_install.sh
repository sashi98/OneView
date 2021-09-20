source ./import_basic_lib.sh
source ./cprime_install_env.sh

CORE_DIST_TAR_PATH=""
CONNECTOR_DIST_TAR_PATH=""
CONNECTOR_CLASSIC_DIST_TAR_PATH=""
HR_CONNECTOR_SERVER_TAR_PATH=""
CONNECTOR_INSTALLER_PATCH_TAR_PATH=""

function download_all_distribution() {
  CORE_DIST_TAR_PATH=$(download "Enter URL for core-distribution [Press Enter to skip]:" "${DOWNLOAD_DIR}")
  info_log "${CORE_DIST_TAR_PATH}"

  CONNECTOR_DIST_TAR_PATH=$(download "Enter URL for connector-distribution [Press Enter to skip]:" "${DOWNLOAD_DIR}")
  info_log "${CONNECTOR_DIST_TAR_PATH}"

  CONNECTOR_CLASSIC_DIST_TAR_PATH=$(download "Enter URL for connector-classic-distribution [Press Enter to skip]:" "${DOWNLOAD_DIR}")
  info_log "${CONNECTOR_CLASSIC_DIST_TAR_PATH}"

  HR_CONNECTOR_SERVER_TAR_PATH=$(download "Enter URL for healthrules-connector-server [Press Enter to skip]:" "${DOWNLOAD_DIR}")
  info_log "${HR_CONNECTOR_SERVER_TAR_PATH}"

  CONNECTOR_INSTALLER_PATCH_TAR_PATH=$(download "Enter URL for connector-installer-patch [Press Enter to skip]:" "${DOWNLOAD_DIR}")
  info_log "${CONNECTOR_INSTALLER_PATCH_TAR_PATH}"
}

function install_core_distribution() {
  untar_file ${CORE_DIST_TAR_PATH} ${WORKSPACE_DIR}
}

test(){
  info_log $DOWNLOAD_DIR
  info_log $WORKSPACE_DIR
}
main() {
  mkdir -pv ${DOWNLOAD_DIR}

  download_all_distribution
  #install_core_distribution
}
main
#http://nexus:8081/content/repositories/releases/com/healthedge/core/core-distribution/20.3.0.2-202005200520-aafbe49/core-distribution-20.3.0.2-202005200520-aafbe49-pkg.tar.gz
#http://nexus:8081/content/repositories/releases-connector/com/healthedge/c4/connector-distribution/20.3.0.1-202005051859-ac1b4d2/connector-distribution-20.3.0.1-202005051859-ac1b4d2.tar.gz