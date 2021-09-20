# string contains function
function string_contains() {
  substr=$1
  p_str=$2
  IFS=$3
  local found
  read -r -a array <<<"$p_str"
  for element in "${array[@]}"; do
    if [[ "$element" == $substr ]]; then
      found=true
      break
    else
      found=false
    fi
  done

  if [[ "$found" == "true" ]]; then
    echo true
  else
    echo false
  fi
}

function count_char() {
  local c=$1
  local str=$2
  local count=$(grep -o "${c}" <<<"${str}" | wc -l)
  echo "$count"
}

function get_keys() {
  local keys=""
  local str="${1}"
  local brackt="${2}"
  local delim=$3
  case "${brackt}" in
  "{}")
    local n=$(count_char "{" "${str}")
    for ((i = 1; i <= n; i++)); do
      local k=$(index_of "{" "${str}")
      local l=$(index_of "}" "${str}")
      local key=${str:$k:$l-$k-1}
      local rest_str=${str:$l}
      str=$rest_str
      keys+=$key$delim
    done
    ;;
  esac
  echo $keys
}

function index_of() {
  local x=$1
  local str=$2
  # shellcheck disable=SC2003
  local i=$(expr index "${str}" "${x}")
  echo $i
}

function replace() {
  local test=$1
  local key=$2
  local replacement=$3
  echo ${test//$key/$replacement}
}

#function test() {
#  home="~"
#  cprime_home="~/cprime"
#  customer="ucare"
#  version_name="20_3"
#  workspace_dir="{home}/software/{customer}/{version_name}"
#  download_dir="{workspace.dir}/downloads"
#
#  keys=$(get_keys $workspace_dir "{}" ":")
#  echo $keys
#}
#
#test
