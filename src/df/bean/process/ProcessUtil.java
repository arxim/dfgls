/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.process;

//import java.sql.Array;
import df.bean.obj.util.JDate;

/**
 *
 * @author USER
 */
public class ProcessUtil {

    public String selectMM(String type,String name,String Def){
        String strReturn = "";        
        if("T".equals(type.toString())){
            strReturn = this.selectMMTH(name, Def);
        }else{
            strReturn = this.selectMMEN(name, Def);
        }
        return strReturn;
    }
    private String selectMMTH(String name,String Def){
        String[] mthTh = {"มกราคม","กุมภาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฎาคม","สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันวาคม"};
        String strReturn = "";        
        strReturn = "<select id=\""+ name +"\" name=\""+ name +"\" class=\"medium\">";
        int valueMth = 1;
        for(int i = 0;i < mthTh.length ; i++){
           String varluMthStr = "" + valueMth;
           if(varluMthStr.length() == 1)varluMthStr = "0" + varluMthStr;
           if(varluMthStr.equals(Def.toString())){
                strReturn+="<option value=\"" + varluMthStr + "\" selected>" + mthTh[i] + "</option>";
           }else{
                strReturn+="<option value=\"" + varluMthStr + "\">" + mthTh[i] + "</option>";
           }
           valueMth++;
        }
        strReturn+= "</select>";
        return strReturn;
    }
    public String selectMonth(String name, String Def){
        String[] mthTh = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        String strReturn = "";        
        strReturn = "<select id=\""+ name +"\" name=\""+ name +"\" class=\"short\">";
        int valueMth = 1;
        try{
        for(int i = 0;i < mthTh.length ; i++){
           String varluMthStr = "" + valueMth;
           if(varluMthStr.length() == 1)varluMthStr = "0" + varluMthStr;
           if(varluMthStr.equals(Def.toString())){
                strReturn+="<option value=\"" + varluMthStr + "\" selected>" + mthTh[i] + "</option>";
           }else{
                strReturn+="<option value=\"" + varluMthStr + "\">" + mthTh[i] + "</option>";
           }
           valueMth++;
        }
        }catch (Exception e){
        	System.out.println(e);
        }
        strReturn+= "</select>";
        return strReturn;
    }   

    private String selectMMEN(String name,String Def){
        String[] mthTh = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        String strReturn = "";        
        strReturn = "<select id=\""+ name +"\" name=\""+ name +"\" class=\"medium\">";
        int valueMth = 1;
        for(int i = 0;i < mthTh.length ; i++){
           String varluMthStr = "" + valueMth;
           if(varluMthStr.length() == 1)varluMthStr = "0" + varluMthStr;
           if(varluMthStr.equals(Def.toString())){
                strReturn+="<option value=\"" + varluMthStr + "\" selected>" + mthTh[i] + "</option>";
           }else{
                strReturn+="<option value=\"" + varluMthStr + "\">" + mthTh[i] + "</option>";
           }
           valueMth++;
        }
        strReturn+= "</select>";
        return strReturn;
    }   
    public String selectMMS(String type,String name,String Def){
        String strReturn = "";        
        if("T".equals(type.toString())){
            strReturn = this.selectMMTHS(name, Def);
        }else{
            strReturn = this.selectMMENS(name, Def);
        }
        return strReturn;
    }
    private String selectMMTHS(String name,String Def){
        String[] mthTh = {"มกราคม","กุมภาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฎาคม","สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันวาคม"};
        String strReturn = "";        
        strReturn = "<select id=\""+ name +"\" name=\""+ name +"\" class=\"medium\">";
        strReturn+= "<option value=\"\">ทั้งหมด</option>";
        int valueMth = 1;
        for(int i = 0;i < mthTh.length ; i++){
           String varluMthStr = "" + valueMth;
           if(varluMthStr.length() == 1)varluMthStr = "0" + varluMthStr;
           if(varluMthStr.equals(Def.toString())){
                strReturn+="<option value=\"" + varluMthStr + "\" selected>" + mthTh[i] + "</option>";
           }else{
                strReturn+="<option value=\"" + varluMthStr + "\">" + mthTh[i] + "</option>";
           }
           valueMth++;
        }
        strReturn+= "</select>";
        return strReturn;
    }
    private String selectMMENS(String name,String Def){
        String[] mthTh = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        String strReturn = "";        
        strReturn = "<select id=\""+ name +"\" name=\""+ name +"\" class=\"medium\">";
        strReturn+="<option value=\"\">Select</option>";
        int valueMth = 1;
        for(int i = 0;i < mthTh.length ; i++){
           String varluMthStr = "" + valueMth;
           if(varluMthStr.length() == 1)varluMthStr = "0" + varluMthStr;
           if(varluMthStr.equals(Def.toString())){
                strReturn+="<option value=\"" + varluMthStr + "\" selected>" + mthTh[i] + "</option>";
           }else{
                strReturn+="<option value=\"" + varluMthStr + "\">" + mthTh[i] + "</option>";
           }
           valueMth++;
        }
        strReturn+="</select>";
        return strReturn;
    }   
    
    public String selectYY(String name,String def){
        String strReturn = "";
        int yy = Integer.parseInt(JDate.getYear());
        int startYY = 2009;
        int endYY = yy + 5;
        strReturn = "<select id=\""+ name +"\" name=\""+ name +"\" class=\"medium\">";
        for(int i = startYY;i <= endYY ; i++){
           if( i == Integer.parseInt(def)){
                strReturn+="<option value=\"" + i + "\" selected>" + i + "</option>";
           }else{
                strReturn+="<option value=\"" + i + "\">" + i + "</option>";
           }
        }
        strReturn+= "</select>";
        return strReturn;
    }
    public String selectYear(String name,String def){
        String strReturn = "";
        int yy = Integer.parseInt(JDate.getYear());
        int startYY = 2009;
        int endYY = yy + 5;
        strReturn = "<select id=\""+ name +"\" name=\""+ name +"\" class=\"shortMax\">";
        for(int i = startYY;i <= endYY ; i++){
           if( i == Integer.parseInt(def)){
                strReturn+="<option value=\"" + i + "\" selected>" + i + "</option>";
           }else{
                strReturn+="<option value=\"" + i + "\">" + i + "</option>";
           }
        }
        strReturn+= "</select>";
        return strReturn;
    }

    
    public String selectMM(String type,String name,String Def, Boolean rollback){
        String strReturn = "";        
        if("T".equals(type.toString())){
            strReturn = this.selectMMTH(name, Def, rollback);
        }else{
            strReturn = this.selectMMEN(name, Def, rollback);
        }
        return strReturn;
    }    
    private String selectMMTH(String name,String Def, Boolean rollback){
        String[] mthTh = {"มกราคม","กุมภาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฎาคม","สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันวาคม"};
        //String[] mthTh = {"���Ҥ�","����Ҿѹ��","�չҤ�","����¹","����Ҥ�","�Զع�¹","�á�Ҥ�","�ԧ�Ҥ�","�ѹ��¹","���Ҥ�","��Ȩԡ�¹","�ѹ�Ҥ�"};
        String strReturn = "";        
        strReturn = "<select id=\""+ name +"\" name=\""+ name +"\" class=\"medium\">";
        int valueMth = 1;
        for(int i = 0;i < mthTh.length ; i++){
           String varluMthStr = "" + valueMth;
           if(varluMthStr.length() == 1)varluMthStr = "0" + varluMthStr;
           if(varluMthStr.equals(Def.toString())){
                strReturn+="<option value=\"" + varluMthStr + "\" selected>" + mthTh[i] + "</option>";
           }else{
                strReturn+="<option value=\"" + varluMthStr + "\">" + mthTh[i] + "</option>";
           }
           valueMth++;
        }
        strReturn+= "</select>";
        return strReturn;
    }    
    private String selectMMEN(String name,String Def, Boolean rollback){
        String[] mthTh = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        String strReturn = "";        
        strReturn = "<select id=\""+ name +"\" name=\""+ name +"\" class=\"medium\">";
        int valueMth = 1;
        for(int i = 0;i < mthTh.length ; i++){
           String varluMthStr = "" + valueMth;
           if(varluMthStr.length() == 1)varluMthStr = "0" + varluMthStr;
           if(varluMthStr.equals(Def.toString())){
                strReturn+="<option value=\"" + varluMthStr + "\" selected>" + mthTh[i] + "</option>";
           }
           valueMth++;
        }
        strReturn+= "</select>";
        return strReturn;
    } 
    
    public String selectYY(String name,String def, Boolean rollback){
        String strReturn = "";
        int yy = Integer.parseInt(JDate.getYear());
        int startYY = yy - 5;
        int endYY = yy + 1;
        strReturn = "<select id=\""+ name +"\" name=\""+ name +"\" class=\"medium\">";
        for(int i = startYY;i <= endYY ; i++){
           if( i == Integer.parseInt(def)){
                strReturn+="<option value=\"" + i + "\" selected>" + i + "</option>";
           }
        }
        strReturn+= "</select>";
        return strReturn;
    }
}
