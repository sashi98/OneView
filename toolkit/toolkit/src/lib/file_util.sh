# Download function
function download() {
  local target_dir=""
  target_dir=$2
  read -rp "${1}" url
  if [[ "${url}" == "" ]]; then
    return 0
  fi
  local PWD_DIR=""
  PWD_DIR=$(pwd)
  mkdir -p "${target_dir}"
  # shellcheck disable=SC2164
  cd "${2}"
  wget "${url}"
  ds=$?
  if [[ $ds -eq 8 ]]; then
    echo "[FAILURE] Downloading is failed. Download Status Returned=${ds}"
    exit 1
  fi
  local filename=$(get_file_name_from_path "${url}")
  echo "${target_dir}/${filename}"
  # shellcheck disable=SC2164
  cd "${PWD_DIR}"
  return 0
}

function get_file_name_from_path() {
  local filepath=$1
  echo "${filepath##*/}"
}

function untar_file() {
  local tar_file=$1
  local target_dir=$2
  tar -xzvf ${tar_file} --directory ${target_dir}
}
