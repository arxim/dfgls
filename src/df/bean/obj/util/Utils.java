package df.bean.obj.util;

import java.util.ArrayList;
import java.util.List;
import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;

public class Utils {
    static final private String TH_BATH = "บาท";
    static final private String TH_0 = "ศูนย์";
    static final private String TH_STANG = "สตางค์";
    static final private String TH_LIMITED = "ถ้วน";
    static final private String TH_1 = "หนึ่ง";
    static final private String TH_2 = "สอง";
    static final private String TH_3 = "สาม";
    static final private String TH_4 = "สี่";
    static final private String TH_5 = "ห้า";
    static final private String TH_6 = "หก";
    static final private String TH_7 = "เจ็ด";
    static final private String TH_8 = "แปด";
    static final private String TH_9 = "เก้า";
    static final private String TH_10 = "สิบ";
    static final private String TH_100 = "ร้อย";
    static final private String TH_1000 = "พัน";
    static final private String TH_10000 = "หมื่น";
    static final private String TH_100000 = "แสน";
    static final private String TH_1000000 = "ล้าน";
    static final private String TH_MINUS = "ลบ";
    static final private String TH_ONE = "หนึ่ง";
    static final private String TH_ED = "เอ็ด";
    static final private String TH_YEE = "ยี่";
    
    public Utils() {
    }
    static public String replAheadWith(String replByStr, String str, int length) {
        String ret = str;
        for (int i=str.length(); i<length; i++) {
            ret = replByStr + ret;
        }
        
        return ret;
    }
    static public String replFollowWith(String replByStr, String str, int length) {
        String ret = str;
        for (int i=str.length(); i<length; i++) {
            ret = ret + replByStr;
        }
        
        return ret;
    }
    static public String removeString(String remStr, String str) {
        String ret = "";
        str = str + ".00";
        String [] b = str.split("["+remStr+"]");

        if(b[1].length()== 1){
            ret = b[0]+b[1]+"0";
        }else{
            ret = b[0]+b[1];
        }

        return ret;
    }
    // call from outside
    static public String toThaiMoney(Double num) {
        return toThaiMoney(num.toString());
    }
    static public String toThaiMoney(String num) {
        String ret = "";
        String bath = "", rest = "";
        int pointPos = num.length();
        for (int i=0; i<num.length(); i++ ) {
            if (num.substring(i, i+1).equals(".")) { pointPos = i; }
        }
        bath = num.substring(0, pointPos);
        if (Integer.parseInt(bath) == 0) { bath = TH_0; }
        else if (bath.length() > 0) {
            bath = toThaiWord(bath);
        }
        
        if (pointPos != num.length()) {
            rest = num.substring(pointPos + 1, num.length());
            if (rest.length() > 0) {            
                if (rest.length() == 1) { rest = rest + "0"; }
                rest = toThaiWord(rest);
            } 
        }
        
        if (!bath.equals("")) { bath = bath + TH_BATH; }
        if (!rest.equals("")) { rest = rest + TH_STANG; } else { rest = TH_LIMITED; }
        ret = bath + rest;
        return ret;
    }
    
    static private String toThaiWord(String num) {
        String ret = "";
        String sign = "";
        List number = new ArrayList();
        number.add("");
        number.add(TH_1);         number.add(TH_2);        number.add(TH_3);
        number.add(TH_4);        number.add(TH_5);        number.add(TH_6);
        number.add(TH_7);        number.add(TH_8);        number.add(TH_9);      
        
        List unit = new ArrayList();
        unit.add("");        
        unit.add(TH_10);         unit.add(TH_100);        unit.add(TH_1000);
        unit.add(TH_10000);        unit.add(TH_100000);        unit.add(TH_1000000);

        if (num.substring(0,1).equals("-")) {
            sign = TH_MINUS;
            num = num.substring(1, num.length());
        }
        
        String revStr = "";
        for (int i=0; i<num.length(); i++ ) {
            revStr = num.charAt(i) + revStr;
        }
        
        for (int i=0; i<revStr.length(); i++) {
            int numberPos = i % 6;
            int unitPos = i % 7;
            String nnum = "", uunit = "";
            if (i > 6) { unitPos = unitPos + 1; }
            nnum = "" + number.get(Integer.parseInt(revStr.substring(i, i+1)));
            
            if ((numberPos == 0) && (revStr.substring(i, i+1).equals("1")) && (i == revStr.length()-1)) {
                nnum = TH_ONE; 
            } else if ((numberPos == 0) && (revStr.substring(i, i+1).equals("1"))) {
                nnum = TH_ED;
            } else if ((numberPos == 1) && (revStr.substring(i, i+1).equals("1"))) {
                nnum = "";
            } else if ((numberPos == 1) && (revStr.substring(i, i+1).equals("2"))) {
                nnum = TH_YEE;
            } 

            if (!revStr.substring(i, i+1).equals("0")) { uunit = "" + unit.get(unitPos); }
            if (unitPos == 6) { uunit = "" + unit.get(unitPos); }
            ret = nnum + uunit + ret;
        }
        ret = sign + ret;
        return ret;
    }
    
    public static String getInfoPage(String filenameage, String language, DBConnection conn){
        conn.connectToLocal();
        String main = "";
        String menu = "";
        String parentcode = "";
        ResultSet rs =  null;
        try{
            String sql_get_menu = "select PARENT_CODE, MENU_"+language+" from STP_MENU where LINK_PAGE like '%/"+filenameage+"' and HOSPITAL_CODE = '"+conn.getHospitalCode()+"';";
            rs = conn.executeQuery(sql_get_menu);
            while(rs.next()){
                menu = rs.getString("MENU_"+language);
                parentcode = rs.getString("PARENT_CODE");
            }
            rs = null;
            String sql_get_main = "select MENU_"+language+" from STP_MENU where CODE='"+parentcode+"' AND HOSPITAL_CODE = '"+conn.getHospitalCode()+"';";
            main = conn.executeQueryString(sql_get_main) ;
            //rs.close();
            //return "" + main + " :- " + menu;
        }catch(Exception err){
            return err.getMessage();
        }finally{
            conn.Close();
        }
        return "" + main + " > " + menu;
    }
	public static String Join(ArrayList<String> coll, String delimiter){
	    if (coll.isEmpty())
		return "";
	    StringBuilder sb = new StringBuilder();
	    for (String x : coll)
		sb.append(x + delimiter);
	    sb.delete(sb.length()-delimiter.length(), sb.length());
	    return sb.toString();
	}

}