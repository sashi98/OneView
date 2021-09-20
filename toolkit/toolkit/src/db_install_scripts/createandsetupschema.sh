#!/bin/bash
VERSION="1.0"	# script version, used by "lib" functions below but placed up top for convenience

FILE_NAME=$1
DB_TYPE=$2
SCHEMA_NAME=$3
SCHEMA_PASSWORD=$4

echo "Start of importdbdump.sh for db dump file: $FILE_NAME and to be imported into Schema:$SCHEMA_NAME"

kill $(ps aux | grep 'impdp' | awk '{print $2}')

# First Drop the schema if it is already existing
echo "Drop Schema:$SCHEMA_NAME if it is already existing. It may need some time"
sqlplus hedba/$ORACLE_HEDBA_PASSWORD << EOF >$SCHEMA_NAME
whenever sqlerror exit sql.sqlcode;
set echo off
set heading off
drop user $SCHEMA_NAME cascade;
exit;
EOF
echo "Drop Schema:$SCHEMA_NAME if it is already existing. Completed"

# Import the schema now
echo "Import into Schema:$SCHEMA_NAME. It may need some time"
impdp hedba/n0hedge remap_tablespace=DATA:$OLTP_DATA_TABLESPACE remap_tablespace=INDX:OLTP_INDEX_TABLESPACE remap_schema=PAYOR_$DB_TYPE:$SCHEMA_NAME directory=HE_DUMP TRANSFORM=oid:n:type dumpfile=$FILE_NAME
echo "Import into Schema:$SCHEMA_NAME. Completed"

# Change the password for the schema
sqlplus hedba/$ORACLE_HEDBA_PASSWORD << EOF >$SCHEMA_NAME
whenever sqlerror exit sql.sqlcode;
set echo off
set heading off
alter user $SCHEMA_NAME identified by Schmedge1234;
exit;
EOF

# Change password of all users to "denovis10x" in HealthRule Tables.
sqlplus $SCHEMA_NAME/$SCHEMA_PASSWORD << EOF >$SCHEMA_NAME
whenever sqlerror exit sql.sqlcode;
set echo off
set heading off
update USER_ACCOUNT_PASSWORD set PASSWORD_TXT='mzeQQYB5NVLOs62Yik9XCbp5fWk=', SALT_TXT='n0k3JItL';
commit;
exit;
EOF
echo "Change Password for Schema:$SCHEMA_NAME. Completed"


echo "$SCHEMA_NAME created successfully with Password $SCHEMA_PASSWORD"
echo " #################Connection Details for Login#################"
echo " username=$SCHEMA_NAME"
echo " password=$SCHEMA_PASSWORD"
echo " url=jdbc:oracle:thin:@$HOSTNAME:1521/DB12C"
echo " All users (integration, hcc_admin etc.) have password as denovis10x in this schema."
echo " ###############################################################"
