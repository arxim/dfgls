package df.bean.obj.util;

public class Variables {
    static private String userID = "sys";
    static private String password = "";
    static private String hospitalCode = "";
    static public final String ACTIVE = "1";
    static public final boolean IS_WINDOWS = true;
    static public final boolean IS_TEST = true;
    //static public String os = "windows"; //"windows" or "linux"
    //static public String phase = "test"; //"production" or "test"
    
    //for import text file to System
    //public static final String upload_file = "C:\\Upload";
    //for report
    //server
    //public static final String URL = "http://192.168.10.68:8080/doctorfee/reports/"; //report source file location
    //public static final String file_download = "http://192.168.10.68:8080/doctorfee/reports/output/"; //location for report generate file
    //public static final String gen_file_report = "C:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/doctorfee/reports/output/";
    
    public Variables() {
    }

    static public String getUserID() {
        return userID;
    }

    static public void setUserID(String str) {
        userID = str;
    }

    static public String getPassword() {
        return password;
    }

    static public void setPassword(String str) {
        password = str;
    }

    static public String getHospitalCode() {
        return hospitalCode;
    }

    static public void setHospitalCode(String str) {
        hospitalCode = str;
    }
    
}
