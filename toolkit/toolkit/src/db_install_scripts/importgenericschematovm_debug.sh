#!/bin/bash
VERSION="1.0"	# script version, used by "lib" functions below but placed up top for convenience
#
######################################################################################################
#                                                                                                    #
# Script:     setupdb.sh                                                                             #
# Purpose:    Install JDK and Weblogic                                                               #
#                                                                                                    #
# Revision:   v1.0    2014-01-31,    created; smcilvenna      		                                 #
#                                                                                                    #
# Notes:      This file was created in vi w/ tabstops set to '4',                                    #
#             set tabs likewise if code-formatting seems off                                         #
#                                                                                                    #
######################################################################################################
#                                                                                                    #
# usage: see usage() and/or help() below                                                             #
#                                                                                                    #
######################################################################################################

printUsage() {
    echo "[importgenericschematovm.sh] NAME"
    echo "[importgenericschematovm.sh]     $0 - Sets up you generic DB on your local VM. Imports a new schema with the name you provided and password as Schmedge1234"
    echo "[importgenericschematovm.sh] "
    echo "[importgenericschematovm.sh] SYNOPSIS"
    echo "[importgenericschematovm.sh]     $0  [Payor Major Version For the DB] [Type of Schema db - Give db for oltp and dw for DW] [Desired Schema name. DB Type will be appended to this name] "
    echo "[importgenericschematovm.sh] "
    echo "[importgenericschematovm.sh] EXAMPLE"
    echo "[importgenericschematovm.sh]     $0 19.6 db PAYOR_19_6"
    echo "[importgenericschematovm.sh] "
}

# Checks if the Payor version is only a Major version.
function checkVersionValid {
    regExp='^[+-]?([0-9]+\.?|[0-9]*\.[0-9]+)$'
    VERSION_NUMBER=$1
    if [[ $VERSION_NUMBER =~ $regExp ]]
    then
        echo "[importgenericschematovm.sh] Will be importing DB for $VERSION_NUMBER Version"

    else
        echo "[importgenericschematovm.sh] $VERSION_NUMBER is not a valid Version. Only Major Versions are allowed."
        exit 1
    fi
}

if [ "$#" -ne 3 ]; then
    printUsage
    exit 1
fi

#Capture all input params and initialize default one's
DB_VERSION=$1
DB_TYPE=$2
SCHEMA_NAME=$3
USER_NAME=$USER
HOST_NAME=$HOSTNAME
ENG_FS_SERVER=eng-fs-01
ENF_FS_DB_DIR=/share/payor-db-dumps
SCHEMA_PASSWORD="Schmedge1234"
echo "[importgenericschematovm.sh] USER NAME=$USER, HOST NAME=$HOSTNAME"
echo "[importgenericschematovm.sh] # Find out if User provided any minor version for payor then stop the process."
# Find out if User provided any minor version for payor then stop the process.
checkVersionValid $1

# Check if DB Type if one of db or dw. db is For OLTP and dw for DW. If anything else, stop the process.
case $DB_TYPE in
    db|dw) echo "" ;;
    *) echo "[importgenericschematovm.sh] $DB_TYPE is not a valid value. Only db ( for oltp) and dw (for DW) is allowed"
    printUsage
    exit 1 ;;
esac

OLTP_OR_DW=""
if [[ $DB_TYPE = "db" ]]
then
    OLTP_OR_DW="OLTP"
else
    OLTP_OR_DW="DW"
fi

# Verify with user if they have access to eng-fs-01 server
read -p "Did you verify your login to $ENG_FS_SERVER. if not, in a different tab run 'ssh $ENG_FS_SERVER' and check you are able to login. If you could not login, raise an Engg Service Jira ticket for access.
Are you able to login? " -n 1 -r
if [[ $REPLY =~ ^[nN]$ ]]; then
   echo ""
   echo "[importgenericschematovm.sh] Please rerun once you have login access"
   exit 1
fi

# Generate the final Schema name and ask User for confirmation to delete the existing schema for same name.
SCHEMA_NAME=$SCHEMA_NAME"_"$OLTP_OR_DW

reset

read -p "$SCHEMA_NAME will be imported and Existing schema with same name will be dropped. If you don't want that , Kill this run and give a new Schema Name.
 Are you sure to proceed? " -n 1 -r
if [[ $REPLY =~ ^[nN]$ ]]; then
   echo "[importgenericschematovm.sh]  "
   echo "[importgenericschematovm.sh] Please rerun with corrected Parameters"
   exit 1
fi

reset

# Copy ssh keys for seamless login
echo "[importgenericschematovm.sh] ssh-copy-id $ENG_FS_SERVER"
ssh-copy-id $ENG_FS_SERVER

echo "[importgenericschematovm.sh] Login to the FS server and find the file name that matches the given input. Latest Dump file is found if there are multiple."
# Login to the FS server and find the file name that matches the given input. Latest Dump file is found if there are multiple.

FILE_NAME=$(ssh -o StrictHostKeyChecking=no $ENG_FS_SERVER 'bash -s' < finddbdumpfile.sh $ENF_FS_DB_DIR $DB_VERSION $DB_TYPE)

if [ -z "$FILE_NAME" ]
then
      echo "[importgenericschematovm.sh] There was no DB file found at $ENF_FS_DB_DIR on $ENG_FS_SERVER for $DB_VERSION Payor Version"
      exit 1
else
	 echo "[importgenericschematovm.sh]==> Found file $FILE_NAME at $ENF_FS_DB_DIR on $ENG_FS_SERVER for $DB_VERSION Payor Version"
fi

# Copy that file from FS Server to local VM's tmp folder.
echo "[importgenericschematovm.sh] scp $ENG_FS_SERVER:$ENF_FS_DB_DIR/$FILE_NAME /tmp"
scp $ENG_FS_SERVER:$ENF_FS_DB_DIR/$FILE_NAME /tmp

# Copy the file from /tmp/ folder to /u01/HE_DUMP
echo "[importgenericschematovm.sh] FILE_NAME=$FILE_NAME"
sudo ./copydbdumpfile.sh "$FILE_NAME"

# If all is set at this point, Drop existing schema, import new schema and change the password of newly import schema. All the users in the schema are set to have password as "denovis10"
./createandsetupschema.sh "$FILE_NAME" "$OLTP_OR_DW" "$SCHEMA_NAME" "$SCHEMA_PASSWORD"

# Remove file from /tmp/ to free up space.
echo "[importgenericschematovm.sh] Deleting /tmp/$FILE_NAME as import is complete and freeing up space"
rm -rf /tmp/$FILE_NAME

exit
