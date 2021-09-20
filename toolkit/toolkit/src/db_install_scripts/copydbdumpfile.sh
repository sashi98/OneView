#!/bin/bash
VERSION="1.0"	# script version, used by "lib" functions below but placed up top for convenience

FILE_NAME=$1
TARGET_LOCATION=/u10/oradata/HE_DUMP

echo "Start of copydbdumpfile.sh for FileName : $FILE_NAME and to be copied to location:/u01/HE_DUMP"


if [[ -f "$FILE" ]]; then
    cp /tmp/$FILE_NAME $TARGET_LOCATION
fi

TMP_FILE=/tmp/$FILE_NAME
if [[ -f "$TMP_FILE" ]]; then
    cp /tmp/$FILE_NAME $TARGET_LOCATION
else
	echo "scp from Engg fs server was not successful and File at $TMP_FILE is not found. Check for any error."
	exit 1
fi

