Steps to install Local DB and Upgrade to 19.6.0.3

===================================================

1. set the following alias in ~/.bashrc_common

	

	1.1 export ORACLE_BASE=/u01/app/oracle

	1.2 export ORACLE_HOME=$ORACLE_BASE/12.2.0.1/db1

	1.3 export weblogic_ver=12cR2  //this is the version of weblogic you are using. There are multiple lines of script that refer this constant in .bashrc_common. Define this before the first usage.

	// Alternatively, you can just set weblogic_ver = $BEA_VER which is set in .bashrc



2. Make sure your todomain and toserver aliases work properly. if not set, then add these



	alias toserver='cd $BEA_HOME/' // BEA_VER and BEA_HOME is set in .bashrc

	alias todomain='cd $BEA_HOME/user_projects/domains/$WL_DOMAIN'



3. Copy the scripts given by akash in a folder. Make sure they all have execute permission. if not,run chmod +x filename

	

   Note that we only give major version 19.6, 19.7 and so on. not 19.6.0.3.

   To install 19.6 Generic OLTP and name it SRIKANTH_196_OLTP, run script as below:

		./importgenericschematovm.sh 19.6 db SRIKANTH_196

   Similarly, to install 19.6 Generic DW and name it SRIKANTH_196_DW

        ./importgenericschematovm.sh 19.6 dw UCARE_196

	The Script adds _OLTP or _DW to your given name based on the type of installation (db = OLTP, dw = DW)

	This script takes care of connecting to eng-fs and copy dump files, import into VM and changing passwords. The DB Passwords are defaulted to “Schmedge1234”



4. Install oracle sqldeveloper on your Citrix VM. This is to verify that OLTP and DW are installed properly. Alternatively, you can use sql commandline in Dev VM to verify.

	

	Create a new connection in sql developer with these details to verify

	username=SRIKANTH_196_OLTP

	password=Schmedge1234

	hostname=srao7.headquarters.healthedge.com

	port=1521

	SID=DB12C

	

	And for DW:	

	username=SRIKANTH_196_DW

	password=Schmedge1234

	hostname=srao7.headquarters.healthedge.com

	port=1521

	sid=DB12C

	

5. Once you have verified that the local database is started properly, we need to update the database configuration files.



	5.1 todomain

	5.2 cd config/jdbc

	5.3 vim oltp-jdbc.xml . Update the driver params - HOST, PORT, SERVICE_NAME (same as SID above), user. Password is unchanged - Schmedge1234

	<jdbc-driver-params>

    <url>jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS_LIST =  (ADDRESS = (PROTOCOL=TCP)(HOST=srao7)(PORT=1521)) ) (CONNECT_DATA = (SERVICE_NAME = DB12C)))</url>

    <driver-name>oracle.jdbc.OracleDriver</driver-name>

    <properties>

      <property>

        <name>user</name>

        <value>SRIKANTH_196_OLTP</value>

      </property>

    </properties>

    <password-encrypted>{AES}hQTSl93ClQsUoFRZcI211+ERnoL12mAhxOtCkkx6E+8=</password-encrypted>

    </jdbc-driver-params>

	

	5.4 vim dw-jdbc.xml and set the jdbc driver parameters for DW from values already given in #4.



6. Update wl.properties. These are the values after update.

	

	OLTP_PLATFORM=Oracle

	OLTP_HOST=srao7

	OLTP_PORT=1521

	OLTP_DB=DB12C

	OLTP_USER=SRIKANTH_OLTP_196

	OLTP_PASSWORD=Schmedge1234

	DEFAULT_PASSWORD=denovis10x

	DEFAULT_MBEAN_PASSWORD=denovis10x

	SDPF_HCC_UNIQUE_IDENTIFIER=PHIC

	DEFAULT_MBEAN_USER=weblogic

	DW_PLATFORM=Oracle

	DW_HOST=srao7

	DW_PORT=1521

	DW_DB=DB12C

	DW_USER=SRIKANTH_196_DW

	DW_PASSWORD=Schmedge1234

	DEFAULT_URL=t3://localhost:7001

	DEFAULT_USER=hcc_admin

	TRACE_FLAGS=integration



7. The script we ran in step 3 installed 19.6 version DB. Now we upgrade the DB to 19.6.0.3

	

	7.0 Kill weblogic server (either use ./stop_server or kill the process)

	7.1 cd ~/softwares/19603

	7.2 cd healthedge/ddl/oltp

	7.3 ./upgrade_oltp.sh

	7.4 cd ../dw

	7.5 ./upgrade_dw.sh



8. Start wls server

	8.1 todomain

	8.2 ./start_server



9. Settings for karaf server

	9.1 cd ~/cprime/health*/etc

	9.2 ll | grep oltp.cfg

	9.3 Update DB Params in that file

	url=jdbc:oracle:thin:@srao7:1521/DB12C

	username=SRIKANTH_196_DB

	password=Schmedge1234

	9.4 ll | grep dw.cfg

	9.5 I am not sure if we need to update this file. Right now it has some place holders only.

	

10. Restart karaf server

	10.1 cd ~/cprime/healthrules-connector-server-4.0.4/bin

	10.2 ./stop

	10.3 ./start

	

	






