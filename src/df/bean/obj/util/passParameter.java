package df.bean.obj.util;

public class passParameter {
    public static String param;
    public static String param2, conditionPara;
    public static String sqlSearch;             //keep SQL Command String for Search 
    public static String[] sqlTitle;
    public static String[] sqlList;             //keep Item for Single List
    public static String[][] sqlList2D;         //keep Item & Description for ListBox
    public static String[][] dataTable;         //keep Data from Database Table
    public static String user;
    public static String password;
    public static String level;
    //public static String hospitalCode = "02";
    public static String hospitalName;
    public static String hospitalCode;
    public static String menuName;
    public static String whereActive;
    public static String whereAll;
    public static String where;
    public static String where2;
    public static String andCondition = "0";
    public static String whereCondition;
    public static String errorCause;
    public static String checkSelect;
    public static boolean loginPass;
    passParameter(){
        //user = "sys";
        //hospitalName = "Phyathai2";
    }

    public void setPara(String s){
        param = s;
    }
    public String getPara(){
        return param;
    }
}
