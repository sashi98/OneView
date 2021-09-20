#will be assigned when this file is sourced

proprty_files=("$@")

#This function should not be called from outside.
function read_property() {
  local key=$1
  local value=""
  for proprty_file in "${proprty_files[@]}"; do
    if [ -f "$proprty_file" ]; then
      while IFS='=' read -r K V; do
        if [[ "${key}" == "${K}" ]]; then
          value=${V}
          break
        fi
      done <"$proprty_file"
    else
      echo "$proprty_file not found."
    fi
    if [ "${value}" != "" ]; then
      break
    fi
  done
  echo "${value}"
  unset IFS
}

#This function should not be called from outside.
function expand_property_value() {
  local value=$1
  keys=$(get_keys "${value}" "{}" ":")
  IFS=":"
  local arr=(${keys})
  for key in "${arr[@]}"; do
    local replacement=""
    replacement=$(normal_property "${key}")
    value=$(replace "${value}" "{${key}}" "${replacement}")
  done
  unset IFS
  echo "${value}"
}

#This function should not be called from outside.
function normal_property() {
  local getval=$(read_property $1)
  local val=$(echo "${getval}" | sed -e 's/\r//g')
  echo "${val}"
}

#This function should not be called from outside.
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

#This function should not be called from outside.
function get_total_keys() {
  local keys=""
  keys=$(get_keys "${1}" "{}" ":")
  IFS=":"
  local array=(${keys})
  local n=${#array[@]}
  local sum=$n
  for key in "${array[@]}"; do
    local value=$(normal_property "${key}")
    local k=0
    k=$(get_total_keys "$value")
    sum=$(($sum + $k))
  done
  echo $sum

}

#Call this fuction to get value of any propety
function get_property() {
  local value=""
  value=$(normal_property "$1")
  local n=0
  n=$(get_total_keys "${value}")
  if [ $n -eq 0 ]; then
    echo "${value}"
  else
    for ((i = 0; i <= n; i++)); do
      value=$(expand_property_value "${value}")
    done
    echo "${value}"
  fi
}

#test() {
#  local x=$(get_total_keys "a.a")
#  echo $x
#}
#
#test
