source ./../lib/property_reader.sh "./cprime_install.properties"

DOWNLOAD_DIR=$(get_property "download.dir")
WORKSPACE_DIR=$(get_property "workspace.dir")
