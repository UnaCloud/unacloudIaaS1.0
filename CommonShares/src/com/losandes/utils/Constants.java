package com.losandes.utils;

import java.io.File;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for providing the UnaCloud Server constant values
 */
public class Constants {
    //UnaCloud communication and operation constants

    public static final String OK_MESSAGE = "Ok: ";
    public static final String SUCCESSFUL_OPERATION = "Successful operation";
    public static final String ERROR_MESSAGE = "Error: ";
    public static final String UNSUCCESSFUL_OPERATION = "Unsuccessful operation";
    public static final String FATAL_ERROR_MESSAGE = "Fatal Error: ";
    public static final String NOTHING_AVAILABLE = "N/A";
    public static final String CLIENT_ALIVE_MESSAGE = "ALIVE";
    public static final int DGRAM_LENGTH = 1024*10;

    public static final int CLOUDER_CLIENT_TIMEOUT= 1500; //milliseconds
    public static final String MESSAGE_SEPARATOR_TOKEN = "%%";
    //UnaCloud Client executing modes constants
    public static final int TURN_ON = 1;
    public static final int TURN_OFF = 2;
    public static final int LOGIN = 3;
    public static final int LOGOUT = 4;
    public static final String STATE_OFF="Off";
    public static final String STATE_ON="On";
    public static final String STATE_LOCK="Lock";
    public static final String STATE_EXECUTION="In Execution";
    //UnaCloud queries constants
    public static final int ADMIN_USER = 1;
    public static final int CLOUD_USER = 2;
    public static final int GRID_USER = 3;
    public static final int CLOUD_TEMPLATE = 1;
    public static final int GRID_TEMPLATE = 2;
    //UnaCloud Entities states
    public static final int OFF_STATE = 0;
    public static final int ON_STATE = 1;
    public static final int LOCK_STATE= 2;
    public static final int DEPLOYING_STATE= 4;
    public static final int COPYING_STATE= 5;
    public static final int ERROR_STATE= 6;

    //UnaCloud Server operation request constants
    public static final int VIRTUAL_MACHINE_OPERATION = 1;
    public static final int PHYSICAL_MACHINE_OPERATION = 2;
    //UnaCloud Client operation request constants
    public static final int DATABASE_OPERATION = 1;
    public static final int REGISTRATION_OPERATION = 2;
    public static final int ARTHUR_OPERATION = 3;
    public static final int UPDATE_OPERATION = 4;
    public static final int VIRTUAL_MACHINE_CONFIGURATION=5;
    public static final int STOP_CLIENT=6;
    public static final int GET_VERSION=7;
    //UnaCloud Client database operation request constants
    public static final int TURN_OFF_DB = 0;
    public static final int TURN_ON_DB = 1;
    public static final int LOGIN_DB = 2;
    public static final int LOGOUT_DB = 3;
    public static final int REPORT_DB = 4;
    public static final int VIRTUAL_MACHINE_STATE_DB = 5;
    public static final int VIRTUAL_MACHINE_CPU_STATE = 6;
    //UnaCloud Client registration operation request constants
    public static final int NEW_REG = 0;
    public static final int UPDATE_REG = 1;
    public static final int ERASE_REG = 2;
    //UnaCloud Server virtual machine operation request constants
    public static final int VM_TURN_ON = 1;
    public static final int VM_TURN_OFF = 2;
    public static final int VM_RESTART = 3;
    public static final int VM_STATE = 4;
    public static final int VM_TIME = 5;
    //UnaCloud Server physical machine operation request constants
    public static final int PM_INIT = 1;
    public static final int PM_TURN_OFF = 1;
    public static final int PM_RESTART = 2;
    public static final int PM_LOGOUT = 3;
    public static final int PM_MONITOR = 4;
    public static final int PM_WRITE_FILE = 6;
    public static final int PM_TURN_ON = 7;
    public static final int PM_RETRIEVE_FOLDER = 8;

    public static final int PM_WRITE_FILE_MULTICAST = 0;//*
    public static final int PM_WRITE_FILE_UNICAST = 1;
    public static final int PM_WRITE_FILE_TREE_DISB = 2;
    public static final int PM_DELETE_FILE = 3;
    
    //UnaCloud Client server socket communication constants
    public static int POOL_THREAD_SIZE = 5;
    //UnaCloud Client location constants
    public static final String BIN="bin";
    public static final String SERVER_PARAMETERS="server_parameters";
    public static final String LOCAL_PARAMETERS="local_parameters";
    public static final String KEY="key";
    public static final String PATH_SPACE=" ";
    public static final String DOUBLE_QUOTE = "\"";
    public static String SEPARATOR = File.pathSeparator;
    public static String PATH_SEPARATOR = File.separator;
    public static String JAR_NAME="/UnaCloudClient.jar";
    //UnaCloud Client VMware Workstation constants
    public static final int VMW=1;
    public static final int PLAYER=2;
    public static final String VMW_RUN_FILE="\\vmrun.exe";
    public static final String VMW_VMX_EXTENSION=".vmx";
    public static final String VMW_TURN_ON="start";
    public static final String VMW_TURN_OFF="stop";
    public static final String VMW_RESTART="reset";
    public static final String VMW_LIST="list";
    public static final String VMW_VMX_ENCONDING = "windows-1252";
    public static final String VMW_VMX_CPU = "numvcpus";
    public static final String VMW_VMX_MEMORY = "memsize";
    public static final String VMW_VMX_HW = "virtualHW.version";
    public static final String VMW_VMX_HW_VER = "7";
    //UnaCloud Client operating system operations constants
    public static String WINDOWS_TURN_OFF_COMMAND = "c:\\windows\\system32\\shutdown.exe -s -t 60";
    public static String WINDOWS_RESTART_COMMAND = "c:\\windows\\system32\\shutdown.exe -r -t 30";
    public static String WINDOWS_LOGOUT_COMMAND = "c:\\windows\\system32\\shutdown.exe -l -f";
    public static String LINUX_TURN_OFF_FILE_COMMAND = "turnOffLinux.sh ";
    public static String LINUX_RESTART_FILE_COMMAND = "restartLinux.sh ";
    public static String LINUX_LOGOUT_FILE_COMMAND = "logoutLinux.sh ";
    public static String MAC_PERL_COMMAND = "/usr/bin/perl";
    public static String MAC_TURN_OFF_FILE_COMMAND = "turnOffMac.sh";
    public static String MAC_RESTART_FILE_COMMAND = "restartMac.sh";
    public static String MAC_LOGOUT_FILE_COMMAND = "logoutMac.pl";
    //UnaCloud Client tools constants
    public static String WOL_CMD = "WolCmd";

    public static final String MULTICAST_GET_LOSED="MULTICAST_GET_LOSED";

}//end of Constants
