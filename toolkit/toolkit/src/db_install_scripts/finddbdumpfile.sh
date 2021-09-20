#!/bin/bash
VERSION="1.0"	# script version, used by "lib" functions below but placed up top for convenience

ENF_FS_DB_DIR=$1
DB_VERSION=$2
DB_TYPE=$3

cd $ENF_FS_DB_DIR

# Find all files matching the name, Sort by last modified and then pick the top one.
FILE_NAME=$(find . -maxdepth 1 -name "*$DB_TYPE*$DB_VERSION*" -printf "%f\n" | sort -n -r | head -n 1)

if [ -z "$FILE_NAME" ]
then
      echo ""
      exit 1
else
	 echo "$FILE_NAME"
fi

